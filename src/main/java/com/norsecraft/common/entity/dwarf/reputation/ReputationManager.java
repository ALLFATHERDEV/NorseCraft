package com.norsecraft.common.entity.dwarf.reputation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.util.Util;
import net.minecraft.util.dynamic.DynamicSerializableUuid;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ReputationManager {

    private final Map<UUID, Reputations> uniqueReputationsMap = Maps.newHashMap();

    public void tick() {
        Iterator<Reputations> iterator = this.uniqueReputationsMap.values().iterator();

        while (iterator.hasNext()) {
            Reputations reputations = iterator.next();
            reputations.decay();
            if (reputations.isReputationsTypeMapEmpty())
                iterator.remove();
        }
    }

    private Stream<ReputationEntry> getReputationEntries() {
        return this.uniqueReputationsMap.entrySet().stream().flatMap((uniqueReputationsEntry) -> uniqueReputationsEntry.getValue().unpack(uniqueReputationsEntry.getKey()));
    }

    private Reputations getOrCreate(UUID identifier) {
        return this.uniqueReputationsMap.computeIfAbsent(identifier, (id) -> new Reputations());
    }

    public int getReputation(UUID identifier, Predicate<ReputationDecayType> type) {
        Reputations reputations = this.uniqueReputationsMap.get(identifier);
        return reputations != null ? reputations.weightedValue(type) : 0;
    }

    public void add(UUID identifier, ReputationDecayType type, int value) {
        Reputations reputations = this.getOrCreate(identifier);
        reputations.reputationDecayTypeMap.mergeInt(type, value, (existing, additive) -> this.mergeValuesForAddition(type, existing, additive));
        reputations.putReputationDecayType(type);
        if (reputations.isReputationsTypeMapEmpty())
            this.uniqueReputationsMap.remove(identifier);
    }

    private int mergeValuesForAddition(ReputationDecayType type, int existing, int additive) {
        int i = existing + additive;
        return i > type.max ? Math.max(type.max, existing) : i;
    }

    public <T> Dynamic<T> write(DynamicOps<T> dynamic) {
        return new Dynamic<>(dynamic, dynamic.createList(this.getReputationEntries().map((entry) -> entry.write(dynamic)).map(Dynamic::getValue)));
    }

    public void read(Dynamic<?> dynamic) {
        dynamic.asStream().map(ReputationEntry::read).flatMap((res) -> Util.stream(res.result()))
                .forEach((entry) -> this.getOrCreate(entry.target).reputationDecayTypeMap.put(entry.type, entry.value));
    }

    static class ReputationEntry {

        public final UUID target;
        public final ReputationDecayType type;
        public final int value;

        public ReputationEntry(UUID target, ReputationDecayType type, int value) {
            this.target = target;
            this.type = type;
            this.value = value;
        }

        public int weightedValue() {
            return this.value * this.type.weight;
        }

        public <T> Dynamic<T> write(DynamicOps<T> dynamic) {
            return new Dynamic<>(dynamic, dynamic.createMap(ImmutableMap.of(dynamic.createString("Target"), DynamicSerializableUuid.CODEC.encodeStart(dynamic, this.target).result().orElseThrow(RuntimeException::new),
                    dynamic.createString("Type"), dynamic.createString(this.type.id), dynamic.createString("Value"), dynamic.createInt(this.value))));
        }

        public static DataResult<ReputationEntry> read(Dynamic<?> dynamic) {
            return DataResult.unbox(DataResult.instance().group(dynamic.get("Target").read(DynamicSerializableUuid.CODEC),
                    dynamic.get("Type").asString().map(ReputationDecayType::byId), dynamic.get("Value").asNumber().map(Number::intValue)).apply(DataResult.instance(),
                    ReputationEntry::new));
        }
    }

    static class Reputations {

        private final Object2IntMap<ReputationDecayType> reputationDecayTypeMap = new Object2IntOpenHashMap<>();

        private Reputations() {

        }

        public int weightedValue(Predicate<ReputationDecayType> type) {
            return this.reputationDecayTypeMap.object2IntEntrySet().stream().filter((entry) -> type.test(entry.getKey()))
                    .mapToInt((func) -> func.getIntValue() * (func.getKey()).weight).sum();
        }

        public Stream<ReputationEntry> unpack(UUID identifier) {
            return this.reputationDecayTypeMap.object2IntEntrySet().stream().map((func) -> new ReputationEntry(identifier, func.getKey(), func.getIntValue()));
        }

        public void decay() {
            ObjectIterator<Object2IntMap.Entry<ReputationDecayType>> objectIterator = this.reputationDecayTypeMap.object2IntEntrySet().iterator();

            while (objectIterator.hasNext()) {
                Object2IntMap.Entry<ReputationDecayType> entry = objectIterator.next();
                int i = entry.getIntValue() - (entry.getKey()).decayPerDay;
                if (i < 2) {
                    objectIterator.remove();
                } else
                    entry.setValue(i);
            }
        }

        public boolean isReputationsTypeMapEmpty() {
            return this.reputationDecayTypeMap.isEmpty();
        }

        public void putReputationDecayType(ReputationDecayType type) {
            int i = this.reputationDecayTypeMap.getInt(type);
            if (i > type.max)
                this.reputationDecayTypeMap.put(type, type.max);
            if (i < 2)
                this.removeReputationDecayType(type);
        }

        public void removeReputationDecayType(ReputationDecayType type) {
            this.reputationDecayTypeMap.removeInt(type);
        }

    }

}
