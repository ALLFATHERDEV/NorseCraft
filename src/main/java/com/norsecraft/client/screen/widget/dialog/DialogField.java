package com.norsecraft.client.screen.widget.dialog;

import com.google.common.collect.Lists;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.render.TextureSprite;
import com.norsecraft.client.screen.FenrirDrawHelper;
import com.norsecraft.client.screen.widget.WidgetBounds;
import com.norsecraft.common.util.CheckUtil;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.List;

public class DialogField implements Drawable {

    private WidgetBounds scrollbarBounds;
    private Identifier texture;
    private TextureSprite scrollbarTexture;
    private final WidgetBounds bounds;
    private DialogLineHolder[] dialogLineHolders;
    private int y = 10;
    private final List<DialogLineHolder> queue = Lists.newArrayList();
    private final List<DialogLineHolder> addedLines = Lists.newArrayList();
    private int currentScrollIndex = 0;

    public DialogField(WidgetBounds bounds, int lineAmount) {
        CheckUtil.notNull(bounds, "Could not create a dialog field, bounds are null");
        this.bounds = bounds;
        this.dialogLineHolders = new DialogLineHolder[lineAmount];
    }

    public void init() {
        for (int i = 0; i < dialogLineHolders.length; i++) {
            WidgetBounds holderBounds = new WidgetBounds(this.bounds);
            this.dialogLineHolders[i] = new DialogLineHolder(holderBounds);
        }
    }

    public void setScrollbar(TextureSprite scrollbarTexture, Identifier texture, WidgetBounds scrollbarBounds) {
        CheckUtil.notNull(scrollbarTexture, "Scrollbar texture is null");
        CheckUtil.notNull(texture, "Texture is null");
        CheckUtil.notNull(scrollbarBounds, "Bounds are null");
        this.scrollbarTexture = scrollbarTexture;
        this.texture = texture;
        this.scrollbarBounds = scrollbarBounds;
    }

    public int getNextDialogLineIndex() {
        for (int i = 0; i < this.dialogLineHolders.length; i++)
            if (dialogLineHolders[i].isEmpty())
                return i;
        return -1;
    }

    public void setDialogLine(int holderIndex, String text, Color color) {
        if (holderIndex == -1) {
            DialogLineHolder last = this.dialogLineHolders[this.dialogLineHolders.length - 1];
            DialogLineHolder newLine = new DialogLineHolder(last.getBounds());
            newLine.setText(text);
            newLine.setColor(color);
            this.shiftDown(newLine);
            this.addedLines.add(newLine);
            return;
        }
        DialogLineHolder holder = this.dialogLineHolders[holderIndex];
        holder.setText(text);
        holder.setColor(color);
        if (holderIndex > 0) {
            DialogLineHolder prev = this.dialogLineHolders[holderIndex - 1];
            int lineAmount = prev.getLineCount();
            holder.setY(this.bounds.y + y + (lineAmount * 9));
            y += 16;
        } else {
            holder.setY(this.bounds.y + 10);
        }
    }

    public void shiftUp() {
        if(queue.isEmpty())
            return;
        DialogLineHolder first = queue.remove(queue.size() - 1);
        DialogLineHolder[] newArray = new DialogLineHolder[this.dialogLineHolders.length];
        newArray[0] = first;
        System.arraycopy(this.dialogLineHolders, 0, newArray, 1, newArray.length - 1);
        this.dialogLineHolders = newArray;
    }

    public void shiftDown(DialogLineHolder newLine) {
        DialogLineHolder first = this.dialogLineHolders[0];
        this.queue.add(first);
        DialogLineHolder[] newArray = new DialogLineHolder[this.dialogLineHolders.length];
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = i + 1 >= newArray.length ? null : this.dialogLineHolders[i + 1];
        }
        newArray[newArray.length - 1] = newLine;
        this.dialogLineHolders = newArray;
        NorseCraftMod.LOGGER.info("Shifted down...");
    }


    public void handleScrollbar(double amount) {
        if(amount == -1.0) {
            //Scrolling down
            if(this.addedLines.isEmpty())
                return;
            this.shiftDown(this.addedLines.get(currentScrollIndex));
            currentScrollIndex += Math.abs(amount);
        } else {
            //Scrolling up
            this.shiftUp();
        }
    }

    public int getWidth() {
        return this.bounds.width;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        //Rendering dialog lines
        for (DialogLineHolder holder : this.dialogLineHolders) {
            if (!holder.isEmpty()) {
                holder.render(matrices);
            }
        }
        //Rendering scrollbar
        if (this.scrollbarTexture != null)
            FenrirDrawHelper.drawSprite(matrices, texture, scrollbarBounds.x, scrollbarBounds.y, scrollbarTexture);
    }


}
