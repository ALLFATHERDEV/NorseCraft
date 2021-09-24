package com.norsecraft.client.screen.widget.dialog;

import com.google.common.collect.Lists;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.render.TextureSprite;
import com.norsecraft.client.screen.FenrirDrawHelper;
import com.norsecraft.client.screen.widget.WidgetBounds;
import com.norsecraft.common.util.CheckUtil;
import com.norsecraft.common.util.shifter.Shifter;
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
    private final List<DialogLineHolder> topQueue = Lists.newArrayList();
    private final List<DialogLineHolder> addedLines = Lists.newArrayList();
    private int currentScrollIndex = 0;
    private Shifter<DialogLineHolder> shifter;

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
        this.shifter = new Shifter<>(this.dialogLineHolders);
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
            DialogLineHolder newLine = this.dialogLineHolders[this.dialogLineHolders.length - 1];
            newLine.setText(text);
            newLine.setColor(color);
            this.shifter.addItemToRight(newLine);
            /*this.addedLines.add(newLine);
            this.scrollDown(newLine);
            */
            return;
        }
        DialogLineHolder holder = this.dialogLineHolders[holderIndex];
        holder.setText(text);
        holder.setColor(color);
        if (holderIndex > 0) {
            DialogLineHolder prev = this.dialogLineHolders[holderIndex - 1];
            int lineAmount = prev.getLineCount();
            holder.setY(bounds.y + y + (lineAmount * 9));
        } else {
            holder.setY(this.bounds.y + y);
        }
        y += 16;
        this.shifter.update(holderIndex, holder);
    }

    public void scrollUp() {
        this.shifter.goLeft();
        /*if (topQueue.isEmpty())
            return;
        DialogLineHolder current = topQueue.remove(topQueue.size() - 1);
        DialogLineHolder[] copy = this.dialogLineHolders;
        for (int i = 1; i < dialogLineHolders.length; i++) {
            this.dialogLineHolders[i].set(copy[i - 1]);
        }
        this.dialogLineHolders[0].set(current);
        */
        NorseCraftMod.LOGGER.info("Scrolled up");
    }


    public void scrollDown(DialogLineHolder newLine) {
        /*DialogLineHolder first = this.dialogLineHolders[0];
        this.topQueue.add(first);
        DialogLineHolder[] copy = this.dialogLineHolders;
        for (int i = 0; i < this.dialogLineHolders.length - 1; i++) {
            this.dialogLineHolders[i].set(copy[i + 1]);
        }
        this.dialogLineHolders[this.dialogLineHolders.length - 1].set(newLine);
        NorseCraftMod.LOGGER.info("Scrolled down");
    */
    }


    public void handleScrollbar(double amount) {
        if (amount == -1.0) {
            this.shifter.goRight();
            //Scrolling down
            /*if (this.addedLines.isEmpty())
                return;
            this.scrollDown(this.addedLines.remove(currentScrollIndex));
            currentScrollIndex += Math.abs(amount);
        */} else {
            //Scrolling up
            /*this.scrollUp();
            currentScrollIndex -= Math.abs(amount);*/
            this.shifter.goLeft();
        }
        //currentScrollIndex = MathHelper.clamp(currentScrollIndex, 0, this.addedLines.size() - 1);
    }

    public int getWidth() {
        return this.bounds.width;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        //Rendering dialog lines#

        this.shifter.forEach(holder -> {
            if(!holder.isEmpty())
                holder.render(matrices);
        });

        /*for (DialogLineHolder holder : this.shifter.getBaseArray()) {
            if (!holder.isEmpty()) {
                holder.render(matrices);
            }
        }*/
        //Rendering scrollbar
        if (this.scrollbarTexture != null)
            FenrirDrawHelper.drawSprite(matrices, texture, scrollbarBounds.x, scrollbarBounds.y, scrollbarTexture);
    }


}
