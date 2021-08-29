package com.norsecraft.common.entity.dwarf.reputation;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public enum ReputationDecayType {

    MAJOR_NEGATIVE("major_negative", -5, 100, 10, 10),
    MINOR_NEGATIVE("minor_negative", -1, 200, 20, 20),
    MINOR_POSITIVE("minor_positive", 1, 200, 1, 5),
    MAJOR_POSITIVE("major_positive", 5, 100, 0, 100);

    public final String id;
    public final int weight;
    public final int max;
    public final int decayPerDay;
    public final int decayPerTransfer;
    private static final Map<String, ReputationDecayType> BY_ID = Stream.of(values()).collect(ImmutableMap.toImmutableMap((val) -> {
        return val.id;
    }, Function.identity()));

    private ReputationDecayType(String id, int weight, int max, int decayPerDay, int decayPerTransfer) {
        this.id = id;
        this.weight = weight;
        this.max = max;
        this.decayPerDay = decayPerDay;
        this.decayPerTransfer = decayPerTransfer;
    }

    @Nullable
    public static ReputationDecayType byId(String id) {
        return BY_ID.get(id);
    }

}
