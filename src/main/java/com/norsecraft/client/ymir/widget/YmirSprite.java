package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.widget.data.Texture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class YmirSprite extends YmirWidget {

    protected int currentFrame;
    protected long currentFrameTime = 0;
    protected Texture[] frames;
    protected int frameTime;
    protected long lastFrame;
    protected boolean singleImage = false;
    protected int tint = 0xFFFFFFFF;

    public YmirSprite(Texture texture) {
        this.frames = new Texture[]{texture};
        this.singleImage = true;
    }

    public YmirSprite(Identifier image) {
        this(new Texture(image));
    }


    public YmirSprite(Identifier image, float u1, float v1, float u2, float v2, float textureWidth, float textureHeight) {
        this(new Texture(image, u1, v1, u2, v2, textureWidth, textureHeight));
    }

    public YmirSprite(int frameTime, Identifier... frames) {
        this.frameTime = frameTime;
        this.frames = new Texture[frames.length];

        for (int i = 0; i < frames.length; i++) {
            this.frames[i] = new Texture(frames[i]);
        }

        if (frames.length==1) this.singleImage = true;
    }

    public YmirSprite(int frameTime, Texture... frames) {
        this.frameTime = frameTime;
        this.frames = frames;
        if (frames.length==1) this.singleImage = true;
    }
    public YmirSprite setImage(Identifier image) {
        return setImage(new Texture(image));
    }

    public YmirSprite setFrames(Identifier... frames) {
        Texture[] textures = new Texture[frames.length];
        for (int i = 0; i < frames.length; i++) {
            textures[i] = new Texture(frames[i]);
        }
        return setFrames(textures);
    }

    public YmirSprite setImage(Texture image) {
        this.frames = new Texture[]{image};
        this.singleImage = true;
        this.currentFrame = 0;
        this.currentFrameTime = 0;
        return this;
    }


    public YmirSprite setFrames(Texture... frames) {
        this.frames = frames;
        if (frames.length==1) singleImage = true;
        if (currentFrame>=frames.length) {
            currentFrame = 0;
            currentFrameTime = 0;
        }
        return this;
    }


    public YmirSprite setTint(int tint) {
        this.tint = tint;
        return this;
    }


    public YmirSprite setOpaqueTint(int tint) {
        this.tint = tint | 0xFF000000;
        return this;
    }


    public YmirSprite setUv(float u1, float v1, float u2, float v2) {
        Texture[] newFrames = new Texture[frames.length];
        for (int i = 0; i < frames.length; i++) {
            newFrames[i] = frames[i].withUv(u1, v1, u2, v2, (int) frames[i].textureWidth(), (int) frames[i].textureHeight());
        }

        return setFrames(newFrames);
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        if (singleImage) {
            paintFrame(matrices, x, y, frames[0]);
        } else {
            long now = System.nanoTime() / 1_000_000L;

            boolean inBounds = (currentFrame >= 0) && (currentFrame < frames.length);
            if (!inBounds) currentFrame = 0;
            Texture currentFrameTex = frames[currentFrame];
            paintFrame(matrices, x, y, currentFrameTex);

            long elapsed = now - lastFrame;
            currentFrameTime += elapsed;
            if (currentFrameTime >= frameTime) {
                currentFrame++;
                if (currentFrame >= frames.length - 1) {
                    currentFrame = 0;
                }
                currentFrameTime = 0;
            }

            this.lastFrame = now;
        }
    }


    @Environment(EnvType.CLIENT)
    protected void paintFrame(MatrixStack matrices, int x, int y, Texture texture) {
        YmirScreenDrawing.texturedRect(matrices, x, y, getWidth(), getHeight(), texture, tint);
    }

}
