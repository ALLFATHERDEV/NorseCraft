package com.norsecraft.client.screen.dwarf;

import com.norsecraft.NorseCraftMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class DwarfDialogScreen extends Screen {

    private static final Identifier BACKGROUND_TEXTURE = NorseCraftMod.ncTex("gui/");

    public DwarfDialogScreen() {
        super(LiteralText.EMPTY);
    }


}
