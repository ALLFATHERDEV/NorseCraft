package com.norsecraft.common.screenhandler;

import com.mojang.datafixers.util.Pair;
import com.norsecraft.common.registry.NCScreenHandlers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Normal i use the "Ymir" gui system, but this is a copy from the normal player inventory screen handler,
 * with some changes for our custom player inventory
 */
public class NorseCraftInventoryScreenHandler extends AbstractRecipeScreenHandler<CraftingInventory> {

    public static final Identifier LOCATION_BLOCKS_TEXTURE = new Identifier("textures/atlas/blocks.png");
    public static final Identifier EMPTY_ARMOR_SLOT_HELMET = new Identifier("item/empty_armor_slot_helmet");
    public static final Identifier EMPTY_ARMOR_SLOT_CHESTPLATE = new Identifier("item/empty_armor_slot_chestplate");
    public static final Identifier EMPTY_ARMOR_SLOT_LEGGINGS = new Identifier("item/empty_armor_slot_leggings");
    public static final Identifier EMPTY_ARMOR_SLOT_BOOTS = new Identifier("item/empty_armor_slot_boots");
    public static final Identifier EMPTY_ARMOR_SLOT_SHIELD = new Identifier("item/empty_armor_slot_shield");
    static final Identifier[] EMPTY_ARMOR_SLOT_TEXTURES;
    private static final EquipmentSlot[] EQUIPMENT_SLOTS_ORDER;
    private final CraftingInventory craftingInput = new CraftingInventory(this, 2, 2);
    private final CraftingResultInventory craftingResult = new CraftingResultInventory();
    public final boolean onServer;
    private final PlayerEntity owner;

    public NorseCraftInventoryScreenHandler(int id, PlayerInventory playerInventory) {
        super(NCScreenHandlers.norseCraftInventory, id);
        this.onServer = !playerInventory.player.world.isClient;
        this.owner = playerInventory.player;
        this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftingInput, this.craftingInput, 0, 146, 18));

        for (int n = 0; n < 2; ++n) {
            for (int m = 0; m < 2; ++m) {
                this.addSlot(new Slot(this.craftingInput, m + n * 2, 105 + m * 18, 8 + n * 18));
            }
        }

        for (int i = 0; i < 4; i++) {
            final EquipmentSlot equipmentSlot = EQUIPMENT_SLOTS_ORDER[i];
            this.addSlot(new Slot(playerInventory, 39 - i, 8, 8 + i * 18) {

                @Override
                public boolean canInsert(ItemStack stack) {
                    return equipmentSlot == MobEntity.getPreferredEquipmentSlot(stack);
                }

                @Override
                public boolean canTakeItems(PlayerEntity playerEntity) {
                    ItemStack itemStack = this.getStack();
                    return (itemStack.isEmpty() || playerEntity.isCreative() || !EnchantmentHelper.hasBindingCurse(itemStack)) && super.canTakeItems(playerEntity);
                }

                @Override
                public Pair<Identifier, Identifier> getBackgroundSprite() {
                    return Pair.of(LOCATION_BLOCKS_TEXTURE, EMPTY_ARMOR_SLOT_TEXTURES[equipmentSlot.getEntitySlotId()]);
                }
            });
        }

        for (int l = 0; l < 3; ++l) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + (l + 1) * 9, 8 + j * 18, 84 + l * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.addSlot(new Slot(playerInventory, 40, 80, 8) {

            @Nullable
            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(LOCATION_BLOCKS_TEXTURE, EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
    }

    public static boolean method_36211(int i) {
        return i >= 36 && i < 45 || i == 45;
    }

    public NorseCraftInventoryScreenHandler(int id, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(id, playerInventory);
    }

    @Override
    public void populateRecipeFinder(RecipeMatcher finder) {
        this.craftingInput.provideRecipeInputs(finder);
    }

    @Override
    public void clearCraftingSlots() {
        this.craftingResult.clear();
        this.craftingInput.clear();
    }

    @Override
    public boolean matches(Recipe<? super CraftingInventory> recipe) {
        return recipe.matches(this.craftingInput, this.owner.world);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        if (!this.owner.world.isClient) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) owner;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = this.owner.world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craftingInput, owner.world);
            if (optional.isPresent()) {
                CraftingRecipe craftingRecipe = optional.get();
                if (craftingResult.shouldCraftRecipe(owner.world, serverPlayerEntity, craftingRecipe)) {
                    itemStack = craftingRecipe.craft(craftingInput);
                }
            }

            craftingResult.setStack(0, itemStack);
            this.setPreviousTrackedSlot(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(this.syncId, this.nextRevision(), 0, itemStack));
        }
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.craftingResult.clear();
        if (!player.world.isClient)
            this.dropInventory(player, this.craftingInput);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);
            if (index == 0) {
                if (!this.insertItem(itemStack2, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (index >= 1 && index < 5) {
                if (!this.insertItem(itemStack2, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 5 && index < 9) {
                if (!this.insertItem(itemStack2, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR && !((Slot) this.slots.get(8 - equipmentSlot.getEntitySlotId())).hasStack()) {
                int i = 8 - equipmentSlot.getEntitySlotId();
                if (!this.insertItem(itemStack2, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentSlot == EquipmentSlot.OFFHAND && !((Slot) this.slots.get(45)).hasStack()) {
                if (!this.insertItem(itemStack2, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 9 && index < 36) {
                if (!this.insertItem(itemStack2, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 36 && index < 45) {
                if (!this.insertItem(itemStack2, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 9, 45, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
            if (index == 0) {
                player.dropItem(itemStack2, false);
            }
        }

        return itemStack;
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 0;
    }

    @Override
    public int getCraftingWidth() {
        return this.craftingInput.getWidth();
    }

    @Override
    public int getCraftingHeight() {
        return this.craftingInput.getHeight();
    }

    @Override
    public int getCraftingSlotCount() {
        return 5;
    }

    @Override
    public RecipeBookCategory getCategory() {
        return RecipeBookCategory.CRAFTING;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.craftingResult && super.canInsertIntoSlot(stack, slot);
    }

    public CraftingInventory getCraftingInput() {
        return craftingInput;
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        return index != this.getCraftingResultSlotIndex();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    static {
        EMPTY_ARMOR_SLOT_TEXTURES = new Identifier[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
        EQUIPMENT_SLOTS_ORDER = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    }
}
