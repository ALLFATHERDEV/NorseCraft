package com.norsecraft.client.ymir.widget.slot;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.widget.YmirItemSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.Objects;
import java.util.function.Predicate;

public class ValidatedSlot extends Slot {

    private final int slotNumber;
    private boolean insertingAllowed = true;
    private boolean takingAllowed = true;
    private Predicate<ItemStack> filter;
    protected final Multimap<YmirItemSlot, YmirItemSlot.ChangeListener> listeners = HashMultimap.create();
    private boolean visible = true;

    public ValidatedSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        if(inventory == null)
            throw new IllegalArgumentException("Can't make a itemslot from a null inventory!");
        this.slotNumber = index;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return insertingAllowed && inventory.isValid(slotNumber, stack) && filter.test(stack);
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return takingAllowed && inventory.canPlayerUse(playerEntity);
    }

    @Override
    public void markDirty() {
        listeners.forEach((slot, listener) -> listener.onStackChanged(slot, inventory, getInventoryIndex(), getStack()));
        super.markDirty();
    }

    public int getInventoryIndex() {
        return this.slotNumber;
    }

    public boolean isInsertingAllowed() {
        return insertingAllowed;
    }

    public void setInsertingAllowed(boolean insertingAllowed) {
        this.insertingAllowed = insertingAllowed;
    }

    public boolean isTakingAllowed() {
        return takingAllowed;
    }

    public void setTakingAllowed(boolean takingAllowed) {
        this.takingAllowed = takingAllowed;
    }

    public Predicate<ItemStack> getFilter() {
        return filter;
    }

    public void setFilter(Predicate<ItemStack> filter) {
        this.filter = filter;
    }

    public void addChangeListener(YmirItemSlot owner, YmirItemSlot.ChangeListener listener) {
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(listener, "listener");
        listeners.put(owner, listener);
    }

    @Override
    public boolean isEnabled() {
        return this.isVisible();
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
