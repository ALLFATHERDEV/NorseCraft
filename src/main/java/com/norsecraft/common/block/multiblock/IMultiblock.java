package com.norsecraft.common.block.multiblock;

import com.norsecraft.common.block.dwarfforge.DwarfForgeMultiblock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/**
 * Every multiblock type class has to implement this interface
 */
public interface IMultiblock extends Cloneable {

    /**
     * @return the multiblock id
     */
    Identifier getId();

    /**
     * @return the multiblock shape
     */
    MultiblockShape getShape();

    /**
     * Sets the multiblock to his form
     *
     * @param controllerPos  the block position of the block that was right clicked
     * @param blockDirection the player facing direction
     * @param world          the world
     */
    void set(BlockPos controllerPos, Direction blockDirection, World world);

    /**
     * If the multiblock got updated, this method will be called
     *
     * @param pos the pos where it got updated
     */
    void update(BlockPos pos);

    /**
     * If the player destroys one block of the structure, this method is called
     *
     * @param pos the pos where the player destroyed the block
     */
    void unset(BlockPos pos);

    /**
     * Saves the multiblock structure into a nbt compound
     *
     * @return the finished multiblock compound
     */
    NbtCompound write();

    /**
     * @return the cloned multiblock
     */
    IMultiblock cloneMultiblock();

    /**
     * Sets the shape, if it is needed
     *
     * @param shape the new shape
     */
    default void setShape(MultiblockShape shape) {

    }

    /**
     * This method reads a mutliblock type out of a {@link NbtCompound}
     *
     * @param nbt the nbt compound
     * @param <T> the multiblock
     * @return the multiblock or null if the id was not found
     */
    static <T extends IMultiblock> T read(NbtCompound nbt) {
        String id = nbt.getString("ID");
        if (id.equals(DwarfForgeMultiblock.ID.getPath())) {
            DwarfForgeMultiblock dfmb = new DwarfForgeMultiblock();
            MultiblockShape shape = MultiblockShape.read(nbt.getCompound("Shape"));
            dfmb.setShape(shape);
            return (T) dfmb;
        }
        return null;
    }

}
