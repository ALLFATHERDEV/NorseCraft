package com.norsecraft.client.ymir.widget;

import com.google.common.collect.Lists;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.widget.slot.ValidatedSlot;
import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.screen.BackgroundPainter;
import com.norsecraft.client.ymir.widget.data.InputResult;
import com.norsecraft.client.ymir.widget.data.NarrationMessages;
import com.norsecraft.client.ymir.widget.icon.Icon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A widget that displays an item that can be interacted with.
 *
 * <p>Item slot widgets can contain multiple visual slots themselves.
 * For example, a slot widget might be 5x3 with 15 visual slots in total.
 *
 * <p>Item slots are handled with so-called peers in the background.
 * They are instances of {@link ValidatedSlot} that handle the interactions
 * between the player and the widget.
 *
 * <h2>Filters</h2>
 * Item slots can have filters that check whether a player is allowed to insert an item or not.
 * The filter can be set with {@link #setFilter(Predicate)}. For example:
 *
 * <pre>
 * {@code
 * // Only sand in this slot!
 * slot.setFilter(stack -> stack.getItem() == Items.SAND);
 * }
 * </pre>
 *
 * <h2>Listeners</h2>
 * Slot change listeners are instances of {@link YmirItemSlot.ChangeListener} that can handle changes
 * to item stacks in slots. For example:
 *
 * <pre>
 * {@code
 * slot.addChangeListener((slot, inventory, index, stack) -> {
 *     if (stack.isEmpty() || stack.getCount() < stack.getMaxCount()) {
 *         System.out.println("I'm not full yet!");
 *     }
 * });
 * }
 * </pre>
 */
public class YmirItemSlot extends YmirWidget {

    private static final Predicate<ItemStack> DEFAULT_FILTER = stack -> true;
    private final List<ValidatedSlot> peers = Lists.newArrayList();
    @Nullable
    @Environment(EnvType.CLIENT)
    private BackgroundPainter backgroundPainter = null;
    @Nullable
    private Icon icon = null;
    private Inventory inventory;
    private int startIndex = 0;
    private int slotsWide = 1;
    private int slotsHigh = 1;
    private boolean big = false;
    private boolean insertingAllowed = true;
    private boolean takingAllowed = true;
    private int focusedSlot = -1;
    private int hoveredSlot = -1;
    private Predicate<ItemStack> filter = DEFAULT_FILTER;
    private final Set<ChangeListener> listeners = new HashSet<>();

    public YmirItemSlot(Inventory inventory, int startIndex, int slotsWide, int slotsHigh, boolean big) {
        this();
        this.inventory = inventory;
        this.startIndex = startIndex;
        this.slotsWide = slotsWide;
        this.slotsHigh = slotsHigh;
        this.big = big;
    }

    private YmirItemSlot() {
        this.hovered.addListener((property, from, to) -> {
            assert to != null;
            if (!to) hoveredSlot = -1;
        });
    }

    public static YmirItemSlot of(Inventory inventory, int index) {
        YmirItemSlot w = new YmirItemSlot();
        w.inventory = inventory;
        w.startIndex = index;
        return w;
    }

    public static YmirItemSlot of(Inventory inventory, int startIndex, int slotsWide, int slotsHigh) {
        YmirItemSlot w = new YmirItemSlot();
        w.inventory = inventory;
        w.startIndex = startIndex;
        w.slotsWide = slotsWide;
        w.slotsHigh = slotsHigh;
        return w;
    }

    public static YmirItemSlot outputOf(Inventory inventory, int index) {
        YmirItemSlot w = new YmirItemSlot();
        w.inventory = inventory;
        w.startIndex = index;
        w.big = true;
        return w;
    }

    public static YmirItemSlot ofPlayerStorage(Inventory inventory) {
        YmirItemSlot w = new YmirItemSlot() {
            @Override
            public @Nullable Text getNarrationName() {
                return inventory instanceof PlayerInventory inv ? inv.getDisplayName() : NarrationMessages.INVENTORY;
            }
        };
        w.inventory = inventory;
        w.startIndex = 9;
        w.slotsWide = 9;
        w.slotsHigh = 3;
        return w;
    }

    @Override
    public int getWidth() {
        return slotsWide * 18;
    }

    @Override
    public int getHeight() {
        return slotsHigh * 18;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    public boolean isBigSlot() {
        return big;
    }

    @Nullable
    public Icon getIcon() {
        return icon;
    }

    public YmirItemSlot setIcon(@Nullable Icon icon) {
        this.icon = icon;

        if (icon != null && (slotsWide * slotsHigh) > 1) {
            NorseCraftMod.LOGGER.warn("Setting icon {} for item slot {} with more than 1 slot ({})", icon, this, slotsWide * slotsHigh);
        }
        return this;
    }

    public boolean isModifiable() {
        return takingAllowed || insertingAllowed;
    }

    public YmirItemSlot setModifiable(boolean modifiable) {
        this.insertingAllowed = modifiable;
        this.takingAllowed = modifiable;
        for (ValidatedSlot peer : peers) {
            peer.setInsertingAllowed(modifiable);
            peer.setTakingAllowed(modifiable);
        }
        return this;
    }

    public boolean isInsertingAllowed() {
        return insertingAllowed;
    }

    public YmirItemSlot setInsertingAllowed(boolean insertingAllowed) {
        this.insertingAllowed = insertingAllowed;
        for (ValidatedSlot peer : peers)
            peer.setInsertingAllowed(insertingAllowed);
        return this;
    }

    public boolean isTakingAllowed() {
        return takingAllowed;
    }

    public YmirItemSlot setTakingAllowed(boolean takingAllowed) {
        this.takingAllowed = takingAllowed;
        for (ValidatedSlot peer : peers) {
            peer.setTakingAllowed(takingAllowed);
        }
        return this;
    }

    public int getFocusedSlot() {
        return focusedSlot;
    }

    @Override
    public void validate(GuiInterpretation host) {
        super.validate(host);
        peers.clear();
        int index = startIndex;

        for (int y = 0; y < slotsHigh; y++) {
            for (int x = 0; x < slotsWide; x++) {
                ValidatedSlot slot = createSlotPeer(inventory, index, this.getAbsoluteX() + (x * 18) + 1, this.getAbsoluteY() + (y * 18) + 1);
                slot.setInsertingAllowed(insertingAllowed);
                slot.setTakingAllowed(takingAllowed);
                slot.setVisible(true);
                slot.setFilter(filter);
                for (ChangeListener listener : listeners) {
                    slot.addChangeListener(this, listener);
                }
                peers.add(slot);
                host.addSlotPeer(slot);
                index++;
            }
        }
    }

    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        if (isActivationKey(ch) && host instanceof ScreenHandler && focusedSlot >= 0) {
            ScreenHandler handler = (ScreenHandler) host;
            MinecraftClient client = MinecraftClient.getInstance();

            ValidatedSlot peer = peers.get(focusedSlot);
            client.interactionManager.clickSlot(handler.syncId, peer.id, 0, SlotActionType.PICKUP, client.player);
        }
    }

    protected ValidatedSlot createSlotPeer(Inventory inventory, int index, int x, int y) {
        return new ValidatedSlot(inventory, index, x, y);
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public BackgroundPainter getBackgroundPainter() {
        return backgroundPainter;
    }

    @Environment(EnvType.CLIENT)
    public void setBackgroundPainter(@Nullable BackgroundPainter backgroundPainter) {
        this.backgroundPainter = backgroundPainter;
    }

    public Predicate<ItemStack> getFilter() {
        return filter;
    }

    public YmirItemSlot setFilter(Predicate<ItemStack> filter) {
        this.filter = filter;
        for (ValidatedSlot peer : peers)
            peer.setFilter(filter);
        return this;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        if (backgroundPainter != null)
            backgroundPainter.paintBackground(matrices, x, y, this);

        if (icon != null)
            icon.paint(matrices, x + 1, y + 1, 16);

    }

    @Override
    public @Nullable YmirWidget cycleFocus(boolean lookForwards) {
        if (focusedSlot < 0) {
            focusedSlot = lookForwards ? 0 : (slotsWide * slotsHigh - 1);
            return this;
        }
        if (lookForwards) {
            focusedSlot++;
            if (focusedSlot >= slotsWide * slotsHigh) {
                focusedSlot = -1;
                return null;
            } else {
                return this;
            }
        } else {
            focusedSlot--;
            return focusedSlot >= 0 ? this : null;
        }
    }

    public void addChangeListener(ChangeListener listener) {
        Objects.requireNonNull(listener, "listener");
        listeners.add(listener);

        for (ValidatedSlot peer : peers)
            peer.addChangeListener(this, listener);
    }

    @Override
    public void onShown() {
        for (ValidatedSlot peer : peers)
            peer.setVisible(true);
    }

    @Override
    public InputResult onMouseMove(int x, int y) {
        int slotX = x / 18;
        int slotY = y / 18;
        hoveredSlot = slotX + slotY * slotsWide;
        return InputResult.PROCESSED;
    }

    @Override
    public void onHidden() {
        super.onHidden();
        for (ValidatedSlot peer : peers)
            peer.setVisible(false);
    }

    @Override
    public void addPainters() {
        //backgroundPainter = BackgroundPainter.SLOT;
    }

    @Override
    public void addNarrations(NarrationMessageBuilder builder) {
        List<Text> parts = Lists.newArrayList();
        Text name = getNarrationName();
        if (name != null) parts.add(name);

        if (focusedSlot >= 0)
            parts.add(new TranslatableText(NarrationMessages.ITEM_SLOT_TITLE_KEY, focusedSlot + 1, slotsWide * slotsHigh));
        else if (hoveredSlot >= 0)
            parts.add(new TranslatableText(NarrationMessages.ITEM_SLOT_TITLE_KEY, hoveredSlot + 1, slotsWide * slotsHigh));

        builder.put(NarrationPart.TITLE, parts.toArray(new Text[0]));
    }

    @Nullable
    public Text getNarrationName() {
        return null;
    }

    @FunctionalInterface
    public interface ChangeListener {

        void onStackChanged(YmirItemSlot slot, Inventory inventory, int index, ItemStack stack);

    }

}
