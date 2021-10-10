package com.norsecraft.common.util;

import com.google.common.collect.Lists;
import com.norsecraft.client.ymir.YmirScreenDrawing;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.util.List;

public class VisualLogger {

    static {
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            render(matrices);
        });
    }

    private static final List<Text> WARNING = Lists.newArrayList();

    private final Logger logger;
    private final Class<?> clazz;

    public VisualLogger(Class<?> clazz) {
        this.logger = LogManager.getLogger(clazz);
        this.clazz = clazz;
    }

    public void error(String message, Object... params) {
        log(message, params, Level.ERROR, Formatting.RED);
    }

    public void warn(String message, Object... params) {
        log(message, params, Level.WARN, Formatting.GOLD);
    }

    private void log(String message, Object[] params, Level level, Formatting formatting) {
        logger.log(level, message, params);
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            var text = new LiteralText(clazz.getSimpleName() + '/');
            text.append(new LiteralText(level.name()).formatted(formatting));
            text.append(new LiteralText(": " + ParameterizedMessage.format(message, params)));

            WARNING.add(text);
        }
    }

    public static void render(MatrixStack matrices) {
        if (WARNING.isEmpty())
            return;
        var client = MinecraftClient.getInstance();
        var textRenderer = client.textRenderer;
        int width = client.getWindow().getScaledWidth();
        List<OrderedText> lines = Lists.newArrayList();

        for (Text warning : WARNING) {
            lines.addAll(textRenderer.wrapLines(warning, width));
        }

        int fontHeight = textRenderer.fontHeight;
        int y = 0;
        for (var line : lines) {
            YmirScreenDrawing.coloredRect(matrices, 2, 2 + y, textRenderer.getWidth(line), fontHeight, 0x88_000000);
            textRenderer.draw(matrices, line, 2, 2 + y, 0xFF_FFFFFF);
            y += fontHeight;
        }

    }

    public static void reset() {
        WARNING.clear();
    }

}
