package com.norsecraft.common.block.multiblock;

import com.google.common.collect.Lists;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.block.dwarfforge.BaseDwarfForgeDoubleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MultiblockShape {

    private final BlockPos size;
    private final Supplier<Collection<Block>> validBlocks;
    private List<ShapeBlockData> shapeBlocKData = Lists.newArrayList();
    private BlockPos controllerPos;
    private boolean formed;
    private final MultiblockPrefabMatrix matrix;

    public MultiblockShape(BlockPos size, Supplier<Collection<Block>> validBlocks, MultiblockPrefabMatrix matrix) {
        this.size = size;
        this.validBlocks = validBlocks;
        this.formed = false;
        this.matrix = matrix;
    }

    public boolean isShapeValid(BlockPos controllerPos, Direction blockDirection, World world) {
        if(this.controllerPos == null)
            this.controllerPos = controllerPos;
        if(blockDirection == Direction.UP || blockDirection == Direction.DOWN)
            return false;
        this.shapeBlocKData.clear();
        if(validBlocks == null && this.shapeBlocKData != null)
            return true;
        Collection<Block> blocks = validBlocks.get();
        Direction facing = blockDirection.getOpposite();
        Direction facingYCCW = facing.rotateYCounterclockwise();
        Map<BlockPos, MultiblockPrefabMatrix.MatrixEntry> matrixEnties = this.matrix.getEntries();
        int counter = 0;
        int invalidBlocks = 0;
        for (int z = 0; z < size.getZ(); z++) {
            for (int y = 0; y < size.getY(); y++) {
                for (int x = 0; x < size.getX(); x++) {
                    BlockPos pos = controllerPos.offset(facingYCCW, z).add(0, y, 0).offset(facing, x);
                    BlockPos normal = new BlockPos(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    if(blocks.contains(state.getBlock())) {
                        if(state.getProperties().contains(BaseDwarfForgeDoubleBlock.HALF)) {
                            BlockHalf half = state.get(BaseDwarfForgeDoubleBlock.HALF);
                            BlockPos other = half == BlockHalf.BOTTOM ? pos.up() : pos.down();
                            if(matrixEnties.containsKey(normal) && !existDoubledPos(other) && !existDoubledPos(pos)) {
                                MultiblockPrefabMatrix.MatrixEntry entry = matrixEnties.get(normal);
                                if(entry.doubleBlock) {
                                    if(normal.equals(entry.pos) && entry.block.get() == state.getBlock()) {
                                        this.shapeBlocKData.add(new ShapeBlockData(state, pos));
                                        this.shapeBlocKData.add(new ShapeBlockData(state, other));
                                        counter++;
                                    }
                                }
                            }
                        } else {
                            if(matrixEnties.containsKey(normal) && !existDoubledPos(pos)) {
                                MultiblockPrefabMatrix.MatrixEntry entry = matrixEnties.get(normal);
                                if(normal.equals(entry.pos) && entry.block.get() == state.getBlock()) {
                                    this.shapeBlocKData.add(new ShapeBlockData(state, pos));
                                    counter++;
                                }
                            }
                        }
                    } else {
                        if(state.getBlock() != Blocks.AIR)
                            invalidBlocks++;
                    }
                }
            }
        }
        return counter == matrixEnties.size() && invalidBlocks == 0;
    }

    public void setFormed(boolean formed) {
        this.formed = formed;
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    public boolean isFormed() {
        return formed;
    }

    private boolean existDoubledPos(BlockPos pos) {
        return this.shapeBlocKData.stream().anyMatch((sbd -> sbd.pos.equals(pos)));
    }

    public List<ShapeBlockData> getShapeBlocKData() {
        return shapeBlocKData;
    }

    public NbtCompound write() {
        NbtCompound nbt = new NbtCompound();
        NbtList list = new NbtList();
        for (ShapeBlockData data : shapeBlocKData) {
            NbtCompound c = new NbtCompound();
            c.put("Pos", NbtHelper.fromBlockPos(data.pos));
            c.put("State", NbtHelper.fromBlockState(data.state));
            list.add(c);
        }
        nbt.put("Size", NbtHelper.fromBlockPos(this.size));
        if (this.controllerPos != null)
            nbt.put("ControllerPos", NbtHelper.fromBlockPos(this.controllerPos));
        nbt.putBoolean("Formed", this.formed);
        nbt.put("ShapeData", list);
        nbt.putString("MatrixID", this.matrix.getId().getPath());
        return nbt;
    }

    public static MultiblockShape read(NbtCompound nbt) {
        BlockPos size = NbtHelper.toBlockPos(nbt.getCompound("Size"));
        boolean formed = nbt.getBoolean("Formed");
        NbtList list = nbt.getList("ShapeData", NbtCompound.COMPOUND_TYPE);
        List<ShapeBlockData> shapeBlockData = Lists.newArrayList();
        list.forEach(inbt -> {
            NbtCompound c = (NbtCompound) inbt;
            ShapeBlockData data = new ShapeBlockData(NbtHelper.toBlockState(c.getCompound("State")), NbtHelper.toBlockPos(c.getCompound("Pos")));
            shapeBlockData.add(data);
        });
        MultiblockShape shape = new MultiblockShape(size, null, MultiblockPrefabMatrix.MATRIX_MAP.get(NorseCraftMod.nc(nbt.getString("MatrixID"))));
        shape.shapeBlocKData = shapeBlockData;
        if (nbt.contains("ControllerPos"))
            shape.controllerPos = NbtHelper.toBlockPos(nbt.getCompound("ControllerPos"));
        shape.formed = formed;
        return shape;
    }

    public static class ShapeBlockData {

        public final BlockState state;
        public final BlockPos pos;

        public ShapeBlockData(BlockState state, BlockPos pos) {
            this.state = state;
            this.pos = pos;
        }

    }

}
