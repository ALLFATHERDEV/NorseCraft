package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.widget.data.Texture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

/**
 * Reminder for me (odin):
 * <p>
 * A bar that displays int values from a PropertyDelegate
 *
 * <p>Bars can be used for all kinds of bars including
 * progress bars (and progress arrows) and energy bars.
 */
public class YmirBar extends YmirWidget {

    @Nullable
    protected final Texture bg;
    @Nullable
    protected final Texture bar;
    protected final int field;
    protected final int max;
    protected int maxValue;
    protected PropertyDelegate properties;
    private boolean manuallySetProperties = false;
    protected final Direction direction;
    protected String tooltipLabel;
    protected Text tooltipTextComponent;

    public YmirBar(@Nullable Texture bg, @Nullable Texture bar, int field, int maxField) {
        this(bg, bar, field, maxField, Direction.UP);
    }

    public YmirBar(@Nullable Texture bg, @Nullable Texture bar, int field, int maxField, Direction dir) {
        this.bg = bg;
        this.bar = bar;
        this.field = field;
        this.max = maxField;
        this.maxValue = 0;
        this.direction = dir;
    }

    public YmirBar(Identifier bg, Identifier bar, int field, int maxField) {
        this(bg, bar, field, maxField, Direction.UP);
    }

    public YmirBar(Identifier bg, Identifier bar, int field, int maxField, Direction dir) {
        this(new Texture(bg), new Texture(bar), field, maxField, dir);
    }


    public YmirBar withTooltip(String label) {
        this.tooltipLabel = label;
        return this;
    }

    public YmirBar withToolTip(Text label) {
        this.tooltipTextComponent = label;
        return this;
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        if (bg != null) {
            YmirScreenDrawing.texturedRect(matrices, x, y, getWidth(), getHeight(), bg, 0xFFFFFFFF);
        } else {
           // YmirScreenDrawing.coloredRect(matrices, x, y, 32, 32, YmirScreenDrawing.colorAtOpacity(0x000000, 0.25F));
        }

        int maxVal = max >= 0 ? properties.get(max) : maxValue;
        float percent = properties.get(field) / (float) maxVal;
        if (percent < 0)
            percent = 0f;
        if (percent > 1)
            percent = 1f;

        int barMax = getWidth();
        if (direction == Direction.DOWN || direction == Direction.UP) barMax = getHeight();
        percent = ((int) (percent * barMax)) / (float) barMax;

        int barSize = (int) (barMax * percent);
        if(barSize <= 0)
            return;

        switch (direction) {
            case UP -> {
                int left = x;
                int top = y + getHeight();
                top -= barSize;
                if (bar != null) {
                    YmirScreenDrawing.texturedGuiRect(matrices, left, top, getWidth(), barSize, bar);
                } else {
                    YmirScreenDrawing.coloredRect(matrices, left, top, getWidth(), barSize, YmirScreenDrawing.colorAtOpacity(0xFFFFFF, 0.5f));
                }
            }

            case RIGHT -> {
                if (bar != null) {
                    YmirScreenDrawing.texturedGuiRect(matrices, x, y, barSize, getHeight(), bar);
                } else {
                    YmirScreenDrawing.coloredRect(matrices, x, y, barSize, getHeight(), YmirScreenDrawing.colorAtOpacity(0xFFFFFF, 0.5f));
                }
            }

            case DOWN -> {
                if (bar != null) {
                    YmirScreenDrawing.texturedGuiRect(matrices, x, y, getWidth(), barSize, bar);
                } else {
                    YmirScreenDrawing.coloredRect(matrices, x, y, getWidth(), barSize, YmirScreenDrawing.colorAtOpacity(0xFFFFFF, 0.5f));
                }
            }

            case LEFT -> {
                int left = x + getWidth();
                int top = y;
                left -= barSize;
                if (bar != null) {
                    //bar.u1 = MathHelper.lerp(percent, bar.u2, bar.u1);
                    YmirScreenDrawing.texturedGuiRect(matrices, left, top, barSize, getHeight(), bar);
                } else {
                    YmirScreenDrawing.coloredRect(matrices, left, top, barSize, getHeight(), YmirScreenDrawing.colorAtOpacity(0xFFFFFF, 0.5f));
                }
            }
        }
    }

    @Override
    public void addTooltip(TooltipBuilder builder) {
        if(tooltipLabel != null) {
            int value = (field >= 0) ? properties.get(field) : 0;
            int valMax = (max >= 0) ? properties.get(max) : maxValue;
            builder.add(new TranslatableText(tooltipLabel, value, valMax));
        }
        if(tooltipTextComponent != null) {
            try {
                builder.add(tooltipTextComponent);
            } catch (Throwable t) {
                builder.add(new LiteralText(t.getLocalizedMessage()));
            }
        }
    }

    @Override
    public void validate(GuiInterpretation host) {
        super.validate(host);
        if(properties == null || !manuallySetProperties) properties = host.getPropertyDelegate();
    }

    @Nullable
    public PropertyDelegate getProperties() {
        return properties;
    }

    public YmirBar setProperties(PropertyDelegate properties) {
        this.properties = properties;
        this.manuallySetProperties = properties != null;
        return this;
    }

    public static YmirBar withConstantMaximum(Identifier bg, Identifier bar, int field, int maxValue, Direction dir) {
        YmirBar result = new YmirBar(bg, bar, field, -1, dir);
        result.maxValue = maxValue;
        return result;
    }

    public static YmirBar withConstantMaximum(Texture bg, Texture bar, int field, int maxValue, Direction dir) {
        YmirBar result = new YmirBar(bg, bar, field, -1, dir);
        result.maxValue = maxValue;
        return result;
    }

    public enum Direction {

        UP,
        RIGHT,
        DOWN,
        LEFT

    }

}
