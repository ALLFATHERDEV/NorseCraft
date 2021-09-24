package com.norsecraft.common.util;

import com.norsecraft.NorseCraftMod;

public class CheckUtil {

    public static void notNull(Object object, String msg) {
        if (object == null)
            NorseCraftMod.LOGGER.error(msg, new NullPointerException());
    }

    public static void checkTrue(boolean statement, String msg) {
        if(!statement)
            throw new RuntimeException(msg);
    }

    public static void isBetweenWithEquals(int value, int min, int max) {
        if (!(value >= min && value <= max))
            throw new IndexOutOfBoundsException(String.format("Index %d is not in range. [Min: %d, Max: %d]", value, min, max));
    }

    public static void isBetween(int value, int min, int max) {
        if(!(value > min && value <= max))
            throw new IndexOutOfBoundsException(String.format("Index %d is not in range. [Min: %d, Max: %d]", value, min, max));
    }

}
