package com.norsecraft.common.util.nbt;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.Map;
import java.util.Set;

/**
 * Nbt helper class
 */
public class NbtHelper {

    /**
     * This method checks if the first and second nbt compounds are equal (same keys and same elements);
     *
     * @param first the first nbt compound
     * @param second the second nbt compound
     * @return true if they are equals or false if not
     */
    public static boolean equals(NbtCompound first, NbtCompound second) {
        if(first == null || second == null)
            return false;
        Map<String, NbtElement> firstEntries = getEntryMap(first);
        Map<String, NbtElement> secondEntries = getEntryMap(second);
        if(firstEntries.isEmpty() || secondEntries.isEmpty())
            return false;
        if(firstEntries.size() != secondEntries.size())
            return false;
        return firstEntries.entrySet().stream().allMatch(e -> e.getValue().equals(secondEntries.get(e.getKey())));
    }

    private static Map<String, NbtElement> getEntryMap(NbtCompound nbt) {
        Map<String, NbtElement> map = Maps.newHashMap();
        if(nbt == null)
            return map;
        Set<String> keys = nbt.getKeys();
        if(keys == null)
            return map;
        for(String key : keys)
            map.put(key, nbt.get(key));
        return map;
    }

}
