package com.norsecraft.common.util;

import com.norsecraft.NorseCraftMod;
import net.minecraft.util.Identifier;

/**
 * This class represent an id for our mod. I would prefer that everywhere we want to use an id, we use this class.
 *
 * The id has the same format as the normal mc identifier.
 */
public class NCID {

    /**
     * If you want to give your id a additional path like:
     */
    private final String additionalPath;
    private final String id;

    private NCID(String additionalPath, String id) {
        this.additionalPath = additionalPath;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static NCID of(String additionalPath, String id) {
        return new NCID(additionalPath, id);
    }

    public static NCID of(String id) {
        return new NCID("NONE", id);
    }

    public static NCID of(Identifier id) {
        return new NCID("NONE", id.getPath()) {
            @Override
            public String toString() {
                return id.getNamespace() + ":" + id.getPath();
            }
        };
    }

    @Override
    public String toString() {
        return NorseCraftMod.MOD_ID + ":" + (additionalPath.equals("NONE") ? id : String.format("%s/%s", additionalPath, id));
    }

    public Identifier toMc() {
        return new Identifier(NorseCraftMod.MOD_ID, additionalPath.equals("NONE") ? id : String.format("%s/%s", additionalPath, id));
    }

}
