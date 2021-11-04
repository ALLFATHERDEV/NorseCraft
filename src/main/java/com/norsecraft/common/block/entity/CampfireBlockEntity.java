package com.norsecraft.common.block.entity;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.render.model.flex.FlexModelStateProvider;
import com.norsecraft.common.gui.CampfireGuiInterpretation;
import com.norsecraft.common.network.NetworkHelper;
import com.norsecraft.common.network.s2c.CampfireFlexModelStateS2C;
import com.norsecraft.common.recipe.campfire.CampfireRecipe;
import com.norsecraft.common.registry.NCBlockEntities;
import com.norsecraft.common.registry.NCRecipeTypes;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Map;

public class CampfireBlockEntity extends InventoryBlockEntity implements IAnimatable, FlexModelStateProvider {

    private static final int[] BOTTOM_SLOTS = new int[]{2, 1};
    private static final int[] SIDE_SLOTS = new int[]{1};
    private static final int[] TOP_SLOTS = new int[]{0};

    private final AnimationFactory factory = new AnimationFactory(this);

    private State modelState = State.DEFAULT;
    private State prevModelState = State.DEFAULT;

    int burnTime;
    int fuelTime;
    int cookTime;
    int cookTimeTotal;
    protected final PropertyDelegate propertyDelegate;
    private final Object2IntOpenHashMap<Identifier> recipesUsed;

    private boolean stopNext = false;
    private boolean stopBack = false;

    public CampfireBlockEntity(BlockPos pos, BlockState state) {
        super(NCBlockEntities.campfireBlockEntity, pos, state, 4);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> burnTime;
                    case 1 -> fuelTime;
                    case 2 -> cookTime;
                    case 3 -> cookTimeTotal;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> burnTime = value;
                    case 1 -> fuelTime = value;
                    case 2 -> cookTime = value;
                    case 3 -> cookTimeTotal = value;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
        this.recipesUsed = new Object2IntOpenHashMap<>();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.burnTime = nbt.getShort("BurnTime");
        this.cookTime = nbt.getShort("CookTime");
        this.cookTimeTotal = nbt.getShort("CookTimeTotal");
        this.fuelTime = this.getFuelTime(this.inventory.get(1));
        NbtCompound nbtCompound = nbt.getCompound("RecipesUsed");

        for (String s : nbtCompound.getKeys()) {
            this.recipesUsed.put(new Identifier(s), nbt.getInt(s));
        }

    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putShort("BurnTime", (short) this.burnTime);
        nbt.putShort("CookTime", (short) this.cookTime);
        nbt.putShort("CookTimeTotal", (short) this.cookTimeTotal);
        NbtCompound nbtCompound = new NbtCompound();
        this.recipesUsed.forEach(((identifier, integer) -> nbtCompound.putInt(identifier.toString(), integer)));
        nbt.put("RecipeUsed", nbtCompound);
        return nbt;
    }

    protected int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty())
            return 0;
        else {
            Item item = fuel.getItem();
            return AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(item, 0);
        }
    }

    @Environment(EnvType.CLIENT)
    public void setModelState(State modelState) {
        this.prevModelState = this.modelState;
        this.modelState = modelState;
        if (this.prevModelState.weight < this.modelState.weight)
            stopNext = false;
        else
            stopBack = false;
    }

    public State getModelState() {
        return modelState;
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected Text getContainerName() {
        return Text.of("Campfire");
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        super.setStack(slot, stack);
        if (slot == 3) {
            if (this.hasBowl()) {
                this.setModelState(State.WITH_BOWL_STAND);
                NetworkHelper.broadcastToClient((ServerWorld) world, CampfireFlexModelStateS2C.PACKET_ID, new CampfireFlexModelStateS2C(pos, State.WITH_BOWL_STAND));
            } else {
                this.setModelState(State.WITH_STAND);
                NetworkHelper.broadcastToClient((ServerWorld) world, CampfireFlexModelStateS2C.PACKET_ID, new CampfireFlexModelStateS2C(pos, State.WITH_STAND));
            }
        }

        if (slot == 0 && !(stack.isEmpty() && stack.isItemEqualIgnoreDamage(this.inventory.get(slot)) && ItemStack.areNbtEqual(stack, this.inventory.get(slot)))) {
            this.cookTimeTotal = getCookTime(this.world, this);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    public boolean hasBowl() {
        return !this.inventory.get(3).isEmpty();
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new CampfireGuiInterpretation(syncId, playerInventory, ScreenHandlerContext.create(this.world, this.pos), this.propertyDelegate);
    }

    @Override
    public void setStopNext(boolean stopNext) {
        this.stopNext = stopNext;
    }

    @Override
    public void setStopBack(boolean stopBack) {
        this.stopBack = stopBack;
    }

    @Override
    public boolean shouldPickNextModel() {
        return !this.stopNext;
    }

    @Override
    public boolean shouldPickBackModel() {
        return !stopBack;
    }

    @Override
    public Identifier[] getModels() {
        return new Identifier[]{
                NorseCraftMod.geoModel("campfire.geo.json"),
                NorseCraftMod.geoModel("campfire_with_stand_and_bowl.geo.json"),
                NorseCraftMod.geoModel("campfire_with_stand.geo.json")};
    }

    @Override
    public Identifier[] getTextures() {
        return new Identifier[]{
                NorseCraftMod.ncTex("block/campfire/campfire.png"),
                NorseCraftMod.ncTex("block/campfire/campfire_with_stand_and_bowl.png"),
                NorseCraftMod.ncTex("block/campfire/campfire_with_stand.png")
        };
    }

    @Override
    public Identifier[] getAnimations() {
        return null;
    }


    private boolean isBurning() {
        return this.burnTime > 0;
    }

    public static void tick(World world, BlockPos pos, BlockState state, CampfireBlockEntity blockEntity) {
        boolean bl = blockEntity.isBurning();
        boolean bl2 = false;
        if (bl) {
            --blockEntity.burnTime;
        }

        ItemStack itemStack = blockEntity.inventory.get(1);
        if (!blockEntity.isBurning() && (itemStack.isEmpty() || blockEntity.inventory.get(0).isEmpty())) {
            if (blockEntity.cookTime > 0)
                blockEntity.cookTime = MathHelper.clamp(blockEntity.cookTime - 2, 0, blockEntity.cookTimeTotal);
        } else {
            Recipe<?> recipe = world.getRecipeManager().getFirstMatch(NCRecipeTypes.campfire, blockEntity, world).orElse(null);
            int i = blockEntity.getMaxCountPerStack();
            if (!blockEntity.isBurning() && canAcceptRecipeOutput(recipe, blockEntity.inventory, i)) {
                blockEntity.burnTime = blockEntity.getFuelTime(itemStack);
                blockEntity.fuelTime = blockEntity.burnTime;
                if (blockEntity.isBurning()) {
                    bl2 = true;
                    if (!itemStack.isEmpty()) {
                        Item item = itemStack.getItem();
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            Item item2 = item.getRecipeRemainder();
                            blockEntity.inventory.set(1, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
                        }
                    }
                }
            }

            if (blockEntity.isBurning() && canAcceptRecipeOutput(recipe, blockEntity.inventory, i)) {
                ++blockEntity.cookTime;
                if (blockEntity.cookTime == blockEntity.cookTimeTotal) {
                    blockEntity.cookTime = 0;
                    blockEntity.cookTimeTotal = getCookTime(world, blockEntity);
                    if (craftRecipe(recipe, blockEntity.inventory, i))
                        blockEntity.setLastRecipe(recipe);

                    bl2 = true;
                }
            } else {
                blockEntity.cookTime = 0;
            }
        }

        if (bl2)
            markDirty(world, pos, state);

    }

    public void setLastRecipe(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            Identifier identifier = recipe.getId();
            this.recipesUsed.addTo(identifier, 1);
        }
    }

    private static boolean isNonFlammableWood(Item item) {
        return ItemTags.NON_FLAMMABLE_WOOD.contains(item);
    }

    private static void addFuel(Map<Item, Integer> fuelTimes, Tag<Item> tag, int fuelTime) {
        for (Item item : tag.values())
            if (!isNonFlammableWood(item))
                fuelTimes.put(item, fuelTime);
    }

    private static void addFuel(Map<Item, Integer> fuelTimes, ItemConvertible item, int fuelTime) {
        Item it = item.asItem();
        if (!isNonFlammableWood(it))
            fuelTimes.put(it, fuelTime);
    }

    private static boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (!(slots.get(0).isEmpty()) && recipe != null) {
            ItemStack itemStack = recipe.getOutput();
            if (itemStack.isEmpty())
                return false;
            else {
                ItemStack itemStack1 = slots.get(2);
                if (itemStack1.isEmpty())
                    return true;
                else if (!itemStack1.isItemEqualIgnoreDamage(itemStack))
                    return false;
                else if (itemStack1.getCount() < count && itemStack1.getCount() < itemStack1.getMaxCount())
                    return true;
                else
                    return itemStack1.getCount() < itemStack.getMaxCount();
            }
        }
        return false;
    }

    private static boolean craftRecipe(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (recipe != null && canAcceptRecipeOutput(recipe, slots, count)) {
            ItemStack itemStack = slots.get(0);
            ItemStack itemStack2 = recipe.getOutput();
            ItemStack itemStack3 = slots.get(2);
            if (itemStack3.isEmpty())
                slots.set(2, itemStack2.copy());
            else if (itemStack3.isOf(itemStack2.getItem()))
                itemStack3.increment(1);

            if (itemStack.isOf(Blocks.WET_SPONGE.asItem()) && !(slots.get(1).isEmpty()) && slots.get(1).isOf(Items.BUCKET))
                slots.set(1, new ItemStack(Items.WATER_BUCKET));

            itemStack.decrement(1);
            return true;
        }
        return false;
    }

    private static int getCookTime(World world, Inventory inventory) {
        return world.getRecipeManager().getFirstMatch(NCRecipeTypes.campfire, inventory, world).map(CampfireRecipe::getCookTime).orElse(200);
    }

    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN)
            return BOTTOM_SLOTS;
        return side == Direction.UP ? TOP_SLOTS : SIDE_SLOTS;
    }

    public boolean canInsert(int slot, ItemStack itemStack) {
        return this.isValid(slot, itemStack);
    }

    public boolean canExtract(int slot, ItemStack itemStack, Direction dir) {
        if (dir == Direction.DOWN && slot == 1)
            return itemStack.isOf(Items.WATER_BUCKET) || itemStack.isOf(Items.BUCKET);
        else
            return true;
    }

    public enum State {

        NONE(0),
        DEFAULT(1),
        WITH_STAND(2),
        WITH_BOWL_STAND(3);

        final int weight;

        State(int weight) {
            this.weight = weight;
        }

    }
}
