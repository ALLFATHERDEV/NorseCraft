package com.norsecraft.client.ymir.widget.data;

import net.minecraft.util.Identifier;

import java.util.Objects;

public record Texture(Identifier image, float u1, float v1, float u2, float v2, float textureWidth, float textureHeight) {

    public Texture {
        Objects.requireNonNull(image, "image");
    }

    public Texture(Identifier image) {
        this(image, 0, 0, 1, 1, 1, 1);
    }

}
