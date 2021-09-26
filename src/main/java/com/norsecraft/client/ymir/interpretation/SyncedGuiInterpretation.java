package com.norsecraft.client.ymir.interpretation;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.EmptyInventory;
import com.norsecraft.client.ymir.PropertyDelegateHolder;
import com.norsecraft.client.ymir.screen.BackgroundPainter;
import com.norsecraft.client.ymir.test.ImplementedInventory;
import com.norsecraft.client.ymir.widget.*;
import com.norsecraft.client.ymir.widget.data.HorizontalAlignment;
import com.norsecraft.client.ymir.widget.data.Insets;
import com.norsecraft.client.ymir.widget.data.Vec2i;
import com.norsecraft.client.ymir.widget.slot.TradeOutputSlot;
import com.norsecraft.client.ymir.widget.slot.ValidatedSlot;
import com.norsecraft.common.entity.NorseCraftMerchant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkSide;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.MerchantInventory;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Supplier;

public class SyncedGuiInterpretation extends ScreenHandler implements GuiInterpretation {

    protected Inventory blockInventory;
    protected PlayerInventory playerInventory;
    protected World world;
    protected PropertyDelegate propertyDelegate;

    protected YmirPanel rootPanel = new YmirGridPanel().setInsets(Insets.ROOT_PANEL);
    protected int titleColor = YmirLabel.DEFAULT_TEXT_COLOR;
    protected boolean fullscreen = false;
    protected boolean titleVisible = true;
    protected HorizontalAlignment titleAlignment = HorizontalAlignment.LEFT;

    protected YmirWidget focus;
    private Vec2i titlePos = new Vec2i(8, 6);

    public SyncedGuiInterpretation(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, BackgroundPainter painter) {
        super(type, syncId);
        this.blockInventory = null;
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.world;
        this.propertyDelegate = null;
        this.rootPanel.setBackgroundPainter(painter);
    }

    public SyncedGuiInterpretation(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, @Nullable Inventory blockInventory, @Nullable PropertyDelegate delegate, BackgroundPainter painter) {
        super(type, syncId);
        this.blockInventory = blockInventory;
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.world;
        this.propertyDelegate = delegate;
        if (propertyDelegate != null && propertyDelegate.size() > 0) this.addProperties(propertyDelegate);
        if (blockInventory != null)
            blockInventory.onOpen(playerInventory.player);
        this.rootPanel.setBackgroundPainter(painter);
    }

    @Override
    public YmirPanel getRootPanel() {
        return this.rootPanel;
    }

    @Override
    public int getTitleColor() {
        return this.titleColor;
    }

    @Override
    public GuiInterpretation setRootPanel(YmirPanel panel) {
        this.rootPanel = panel;
        return this;
    }

    @Override
    public GuiInterpretation setTitleColor(int color) {
        this.titleColor = color;
        return this;
    }

    @Override
    public GuiInterpretation setPropertyDelegate(PropertyDelegate delegate) {
        this.propertyDelegate = delegate;
        return this;
    }

    @Override
    public void addSlotPeer(ValidatedSlot slot) {
        this.addSlot(slot);
    }

    @Override
    public void addOutputSlot(TradeOutputSlot slot) {
        this.addSlot(slot);
    }

    @Override
    public void onSlotClick(int slotNumber, int button, SlotActionType actionType, PlayerEntity player) {
        if (actionType == SlotActionType.QUICK_MOVE) {
            if (slotNumber < 0)
                return;

            if (slotNumber >= this.slots.size())
                return;
            Slot slot = this.slots.get(slotNumber);
            if (!slot.canTakeItems(player))
                return;

            ItemStack remaining = ItemStack.EMPTY;
            if (slot.hasStack()) {
                ItemStack toTransfer = slot.getStack();
                remaining = toTransfer.copy();

                if(blockInventory instanceof MerchantInventory) {
                    if(slot instanceof TradeOutputSlot) {
                        if(!this._insertItem(toTransfer, this.playerInventory, true, player))
                            return;


                        slot.onQuickTransfer(toTransfer, remaining);
                    }

                    if(toTransfer.isEmpty())
                        slot.setStack(ItemStack.EMPTY);
                    else
                        slot.markDirty();

                    if(toTransfer.getCount() == remaining.getCount())
                        return;

                    slot.onTakeItem(player, toTransfer);
                    return;
                }

                if (blockInventory != null) {
                    if (slot.inventory == blockInventory) {
                        if (!this._insertItem(toTransfer, this.playerInventory, true, player))
                            return;
                    } else if (!this._insertItem(toTransfer, this.blockInventory, false, player))
                        return;
                } else {
                    if (!swapHotbar(toTransfer, slotNumber, this.playerInventory, player))
                        return;
                }

                if (toTransfer.isEmpty())
                    slot.setStack(ItemStack.EMPTY);
                else
                    slot.markDirty();
            }
        } else {
            super.onSlotClick(slotNumber, button, actionType, player);
        }
    }

    /**
     * WILL MODIFY toInsert! Returns true if anything was inserted.
     */
    private boolean insertIntoExisting(ItemStack toInsert, Slot slot, PlayerEntity player) {
        ItemStack curSlotStack = slot.getStack();
        if (!curSlotStack.isEmpty() && ItemStack.canCombine(toInsert, curSlotStack) && slot.canInsert(toInsert)) {
            int combinedAmount = curSlotStack.getCount() + toInsert.getCount();
            int maxAmount = Math.min(toInsert.getMaxCount(), slot.getMaxItemCount(toInsert));
            if (combinedAmount <= maxAmount) {
                toInsert.setCount(0);
                curSlotStack.setCount(combinedAmount);
                slot.markDirty();
                return true;
            } else if (curSlotStack.getCount() < maxAmount) {
                toInsert.decrement(maxAmount - curSlotStack.getCount());
                curSlotStack.setCount(maxAmount);
                slot.markDirty();
                return true;
            }
        }
        return false;
    }

    /**
     * WILL MODIFY toInsert! Returns true if anything was inserted.
     */
    private boolean insertIntoEmpty(ItemStack toInsert, Slot slot) {
        ItemStack curSlotStack = slot.getStack();
        if (curSlotStack.isEmpty() && slot.canInsert(toInsert)) {
            if (toInsert.getCount() > slot.getMaxItemCount(toInsert)) {
                slot.setStack(toInsert.split(slot.getMaxItemCount(toInsert)));
            } else {
                slot.setStack(toInsert.split(toInsert.getCount()));
            }

            slot.markDirty();
            return true;
        }

        return false;
    }

    protected boolean _insertItem(ItemStack toInsert, Inventory inventory, boolean walkBackwards, PlayerEntity player) {
        ArrayList<Slot> inventorySlots = new ArrayList<>();
        for (Slot slot : slots)
            if (slot.inventory == inventory) inventorySlots.add(slot);

        if (inventorySlots.isEmpty())
            return false;

        boolean inserted = false;
        if (walkBackwards) {
            for (int i = inventorySlots.size() - 1; i >= 0; i--) {
                Slot curSlot = inventorySlots.get(i);
                if (insertIntoExisting(toInsert, curSlot, player)) inserted = true;
                if (toInsert.isEmpty())
                    break;
            }
        } else {
            for (int i = 0; i < inventorySlots.size(); i++) {
                Slot curSlot = inventorySlots.get(i);
                if (insertIntoExisting(toInsert, curSlot, player)) inserted = true;
                if (toInsert.isEmpty())
                    break;
            }
        }

        if (!toInsert.isEmpty()) {
            if (walkBackwards) {
                for (int i = inventorySlots.size() - 1; i >= 0; i--) {
                    Slot curSlot = inventorySlots.get(i);
                    if (insertIntoEmpty(toInsert, curSlot)) inserted = true;
                    if (toInsert.isEmpty())
                        break;
                }
            } else {
                for (int i = 0; i < inventorySlots.size(); i++) {
                    Slot curSlot = inventorySlots.get(i);
                    if (insertIntoEmpty(toInsert, curSlot)) inserted = true;
                    if (toInsert.isEmpty()) break;
                }
            }
        }
        return inserted;
    }

    private boolean swapHotbar(ItemStack toInsert, int slotNumber, Inventory inventory, PlayerEntity player) {
        ArrayList<Slot> storageSlots = new ArrayList<>();
        ArrayList<Slot> hotbarSlots = new ArrayList<>();
        boolean swapToStorage = true;
        boolean inserted = false;

        for (Slot slot : slots) {
            if (slot.inventory == inventory && slot instanceof ValidatedSlot) {
                int index = ((ValidatedSlot) slot).getInventoryIndex();
                if (PlayerInventory.isValidHotbarIndex(index))
                    hotbarSlots.add(slot);
                else {
                    storageSlots.add(slot);
                    if (slot.id == slotNumber) swapToStorage = false;
                }
            }
        }

        if (storageSlots.isEmpty() || hotbarSlots.isEmpty()) return false;

        if (swapToStorage) {
            for (int i = 0; i < storageSlots.size(); i++) {
                Slot curSlot = storageSlots.get(i);
                if (insertIntoExisting(toInsert, curSlot, player)) inserted = true;
                if (toInsert.isEmpty()) break;
            }
            if (!toInsert.isEmpty()) {
                for (int i = 0; i < storageSlots.size(); i++) {
                    Slot curSlot = storageSlots.get(i);
                    if (insertIntoEmpty(toInsert, curSlot)) inserted = true;
                    if (toInsert.isEmpty())
                        break;
                }
            }
        } else {
            for (int i = 0; i < hotbarSlots.size(); i++) {
                Slot curSlot = hotbarSlots.get(i);
                if (insertIntoExisting(toInsert, curSlot, player)) inserted = true;
                if (toInsert.isEmpty()) break;
            }
            if (!toInsert.isEmpty())
                for (int i = 0; i < hotbarSlots.size(); i++) {
                    Slot curSlot = hotbarSlots.get(i);
                    if (insertIntoEmpty(toInsert, curSlot)) inserted = true;
                    if (toInsert.isEmpty())
                        break;
                }
        }
        return inserted;
    }

    @Override
    public void addPainters() {

    }

    @Override
    public @Nullable PropertyDelegate getPropertyDelegate() {
        return this.propertyDelegate;
    }


    public YmirPlayerInvPanel createPlayerInventoryPanel() {
        return new YmirPlayerInvPanel(this.playerInventory);
    }

    public YmirPlayerInvPanel createPlayerInventoryPanel(int yOffset) {
        return new YmirPlayerInvPanel(this.playerInventory, yOffset);
    }

    public YmirPlayerInvPanel createPlayerInventoryPanel(int xOffset, int yOffset) {
        return new YmirPlayerInvPanel(this.playerInventory, xOffset, yOffset);
    }

    public static Inventory getBlockInventory(ScreenHandlerContext ctx) {
        return getBlockInventory(ctx, () -> EmptyInventory.INSTANCE);
    }

    public static Inventory getBlockInventory(ScreenHandlerContext ctx, int size) {
        return getBlockInventory(ctx, () -> new SimpleInventory(size));
    }

    public static Inventory getEntityInventory(Entity entity) {
        if (entity instanceof ImplementedInventory)
            return (Inventory) entity;
        if (entity instanceof NorseCraftMerchant)
            return ((NorseCraftMerchant<?>) entity).getMerchantInventory();
        NorseCraftMod.LOGGER.error("Could not find inventory for entity {}", entity);
        return null;
    }

    private static Inventory getBlockInventory(ScreenHandlerContext ctx, Supplier<Inventory> fallback) {
        return ctx.get((world, pos) -> {
            BlockState state = world.getBlockState(pos);
            Block b = state.getBlock();
            if (b instanceof InventoryProvider) {
                Inventory inventory = ((InventoryProvider) b).getInventory(state, world, pos);
                if (inventory != null)
                    return inventory;
            }

            BlockEntity be = world.getBlockEntity(pos);
            if (be != null) {
                if (be instanceof InventoryProvider) {
                    Inventory inventory = ((InventoryProvider) be).getInventory(state, world, pos);
                    if (inventory != null)
                        return inventory;
                } else if (be instanceof Inventory) {
                    return (Inventory) be;
                }
            }
            NorseCraftMod.LOGGER.info("Fallback :-(");
            return fallback.get();
        }).orElseGet(fallback);
    }

    public static PropertyDelegate getBlockPropertyDelegate(ScreenHandlerContext ctx) {
        return ctx.get((world, pos) -> {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof PropertyDelegateHolder) {
                return ((PropertyDelegateHolder) be).getPropertyDelegate();
            }

            return new ArrayPropertyDelegate(0);
        }).orElse(new ArrayPropertyDelegate(0));
    }

    public static PropertyDelegate getBlockPropertyDelegate(ScreenHandlerContext ctx, int size) {
        return ctx.get((world, pos) -> {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof PropertyDelegateHolder) {
                return ((PropertyDelegateHolder) be).getPropertyDelegate();
            }
            return new ArrayPropertyDelegate(size);
        }).orElse(new ArrayPropertyDelegate(size));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return blockInventory == null || blockInventory.canPlayerUse(player);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        if (blockInventory != null)
            blockInventory.onClose(player);
    }

    @Override
    public boolean isFocused(YmirWidget widget) {
        return focus == widget;
    }

    @Override
    public @Nullable YmirWidget getFocus() {
        return this.focus;
    }

    @Override
    public void requestFocus(YmirWidget widget) {
        if (focus == widget) return;
        if (!widget.canFocus()) return;
        if (focus != null) focus.onFocusLost();
        focus = widget;
        focus.onFocusGained();
    }

    @Override
    public void releaseFocus(YmirWidget widget) {
        if (focus == widget) {
            focus = null;
            widget.onFocusLost();
        }
    }

    @Override
    public boolean isFullscreen() {
        return this.fullscreen;
    }

    @Override
    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    @Override
    public boolean isTitleVisible() {
        return titleVisible;
    }

    @Override
    public void setTitleVisible(boolean titleVisible) {
        this.titleVisible = titleVisible;
    }

    @Override
    public HorizontalAlignment getTitleAlignment() {
        return titleAlignment;
    }

    @Override
    public void setTitleAlignment(HorizontalAlignment alignment) {
        this.titleAlignment = alignment;
    }

    @Override
    public Vec2i getTitlePos() {
        return this.titlePos;
    }

    @Override
    public void setTitlePos(Vec2i titlePos) {
        this.titlePos = titlePos;
    }

    public final NetworkSide getNetworkSide() {
        return world instanceof ServerWorld ? NetworkSide.SERVERBOUND : NetworkSide.CLIENTBOUND;
    }

    public final PacketSender getPacketSender() {
        if (getNetworkSide() == NetworkSide.SERVERBOUND)
            return ServerPlayNetworking.getSender((ServerPlayerEntity) playerInventory.player);
        else
            return getClientPacketSender();
    }

    @Environment(EnvType.CLIENT)
    private PacketSender getClientPacketSender() {
        return ClientPlayNetworking.getSender();
    }


}
