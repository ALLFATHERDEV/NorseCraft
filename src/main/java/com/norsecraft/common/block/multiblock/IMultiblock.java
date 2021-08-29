package com.norsecraft.common.block.multiblock;

import com.norsecraft.common.block.dwarfforge.DwarfForgeMultiblock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface IMultiblock extends Cloneable {

    Identifier getId();

    MultiblockShape getShape();

    void set(BlockPos controllerPos, Direction blockDirection, World world);

    void update(BlockPos pos);

    void unset(BlockPos pos);

    NbtCompound write();

    IMultiblock cloneMultiblock();

    default void setShape(MultiblockShape shape) {

    }

    static <T extends IMultiblock> T read(NbtCompound nbt) {
        String id = nbt.getString("ID");
        if(id.equals(DwarfForgeMultiblock.ID.getPath())) {
            DwarfForgeMultiblock dfmb = new DwarfForgeMultiblock();
            MultiblockShape shape = MultiblockShape.read(nbt.getCompound("Shape"));
            dfmb.setShape(shape);
            return (T) dfmb;
        }
        return null;
    }

}
