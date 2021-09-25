package com.norsecraft.client.ymir.widget.data;

import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import net.minecraft.util.Identifier;

import java.util.Objects;

public record Texture(Identifier image, float u1, float v1, float u2, float v2, float textureWidth, float textureHeight) {

    public Texture {
        Objects.requireNonNull(image, "image");
    }

    public Texture(Identifier image) {
        this(image, 0, 0, 1, 1, 1, 1);
    }

    public Texture withUv(float u1, float v1, float u2, float v2m, int textureWidth, int textureHeight) {
        return new Texture(image, u1, v1, u2, v2, textureWidth, textureHeight);
    }

    public static Texture component(float u1, float v1, float u2, float v2) {
        return new Texture(GuiInterpretation.COMPONENTS, u1, v1, u2, v2, 1024F, 512F);
    }

}
