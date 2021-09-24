package com.norsecraft.client.ymir.widget.icon;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

public interface Icon {

    @Environment(EnvType.CLIENT)
    void paint(MatrixStack matrices, int x, int y, int size);

}
