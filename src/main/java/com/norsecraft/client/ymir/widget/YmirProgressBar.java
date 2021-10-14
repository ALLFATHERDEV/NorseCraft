package com.norsecraft.client.ymir.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.widget.data.Texture;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A bar that displays int values from a PropertyDelegate
 *
 * <p>Bars can be used for all kinds of bars including
 * progress bars (and progress arrows) and energy bars.
 */
public class YmirProgressBar extends YmirWidget {

    /**
     * The bar texture. If not null, it will be
     * drawn to represent the current field.
     * <p>
     * NOTE: Our own textures are double sized as the mc textures. But for the progress bar we need the normal mc sized textures.
     * So, if you're adding a bar, but the existing is double sized as the default mc texture, you have to write Mattii and he will
     * create the right texture size
     */
    protected final Texture bar;

    /**
     * The ID of the displayed property in the {@link #properties}.
     */
    protected final int field;

    /**
     * The ID of the property representing the maximum value of the {@link #field}.
     *
     * <p>If {@code max} is negative, the {@link #maxValue} constant will be used instead.
     */
    protected final int max;

    /**
     * The constant maximum value of the {@link #field}.
     *
     * <p>This constant will only be used if {@link #max} is negative.
     */
    protected int maxValue;

    /**
     * The properties used for painting this bar.
     *
     * <p>The current value is read from the property with ID {@link #field},
     * and the maximum value is usually read from the property with ID {@link #max}.
     */
    protected PropertyDelegate properties;
    private boolean manuallySetProperties = false;

    /**
     * The direction of this bar, representing where the bar will grow
     * when the field increases.
     */
    protected final YmirProgressBar.Direction direction;

    /**
     * The translation key of the tooltip.
     */
    protected String tooltipLabel;

    /**
     * A tooltip text component. This can be used instead of {@link #tooltipLabel},
     * or together with it. In that case, this component will be drawn after the other label.
     */
    protected Text tooltipTextComponent;

    /**
     * An integer for getting the current progress
     * If nothing is burned, it will be -1
     */
    protected int currentProgress = -1;

    public YmirProgressBar(Texture bar, int field, int maxField) {
        this(bar, field, maxField, YmirProgressBar.Direction.UP);
    }

    public YmirProgressBar(Texture bar, int field, int maxField, YmirProgressBar.Direction dir) {
        this.bar = bar;
        this.field = field;
        this.max = maxField;
        this.maxValue = 0;
        this.direction = dir;
    }

    public YmirProgressBar(Identifier bar, int field, int maxField) {
        this(bar, field, maxField, YmirProgressBar.Direction.UP);
    }

    public YmirProgressBar(Identifier bar, int field, int maxField, YmirProgressBar.Direction dir) {
        this(new Texture(bar), field, maxField, dir);
    }

    @Override
    public boolean canResize() {
        return true;
    }

    /**
     * Adding a tooltip with the current progress
     *
     * @param label the label function. The integer parameter is the current progress of the bar
     *              the return value will be the displayed string
     * @return the bar
     */
    public YmirProgressBar withTooltip(Function<Integer, String> label) {
        this.tooltipLabel = label.apply(this.currentProgress);
        return this;
    }

    public YmirProgressBar withTooltip(String label) {
        this.tooltipLabel = label;
        return this;
    }

    public YmirProgressBar withToolTip(Text label) {
        this.tooltipTextComponent = label;
        return this;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        int maxVal = max >= 0 ? properties.get(max) : maxValue;
        if (maxVal == 0)
            maxVal = 200; //If the max property is 0, we set it to 200. Bcs this is the default value

        int barSize = direction == Direction.UP || direction == Direction.DOWN ? getHeight() : getWidth();
        int percent = properties.get(field) * barSize / maxVal;
        if (percent > 0)
            currentProgress = percent;
        else
            currentProgress = -1;

        /*
            Using default mc render system here.
            If we used our render system, the bar position where not correct
         */
        RenderSystem.setShader(GameRenderer::getPositionTexShader); //Telling mc which shader will be used
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F); //Set the shader color to white
        RenderSystem.setShaderTexture(0, GuiInterpretation.COMPONENTS); //Setting the texture

        switch (direction) {
            case UP -> Screen.drawTexture(matrices, x, y - percent, bar.u1(), bar.v2() - percent, getWidth(), percent + 1, 1024, 512);
            case DOWN -> Screen.drawTexture(matrices, x, y - percent, bar.u1(), bar.v1() + percent, getWidth(), percent - 1, 1024, 512);
            case LEFT -> Screen.drawTexture(matrices, x - percent, y, bar.u2() - percent, bar.v1(), percent + 1, getHeight(), 1024, 512);
            case RIGHT -> Screen.drawTexture(matrices, x - percent, y, bar.u1() + percent, bar.v1(), percent - 1, getHeight(), 1024, 512);
        }

    }

    @Override
    public void validate(GuiInterpretation host) {
        super.validate(host);
        if (properties == null || !manuallySetProperties) properties = host.getPropertyDelegate();
    }

    @Override
    public void addTooltip(TooltipBuilder builder) {
        if (tooltipLabel != null) {
            int value = (field >= 0) ? properties.get(field) : 0;
            int valMax = (max >= 0) ? properties.get(max) : maxValue;
            builder.add(new TranslatableText(tooltipLabel, value, valMax));
        }
        if (tooltipTextComponent != null) {
            try {
                builder.add(tooltipTextComponent);
            } catch (Throwable t) {
                builder.add(new LiteralText(t.getLocalizedMessage()));
            }
        }
    }

    @Nullable
    public PropertyDelegate getProperties() {
        return properties;
    }

    public YmirProgressBar setProperties(PropertyDelegate properties) {
        this.properties = properties;
        this.manuallySetProperties = properties != null;
        return this;
    }

    public static YmirProgressBar withConstantMaximum(Identifier bar, int field, int maxValue, YmirProgressBar.Direction dir) {
        YmirProgressBar result = new YmirProgressBar(bar, field, -1, dir);
        result.maxValue = maxValue;
        return result;
    }

    public static YmirProgressBar withConstantMaximum(Texture bar, int field, int maxValue, YmirProgressBar.Direction dir) {
        YmirProgressBar result = new YmirProgressBar(bar, field, -1, dir);
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
