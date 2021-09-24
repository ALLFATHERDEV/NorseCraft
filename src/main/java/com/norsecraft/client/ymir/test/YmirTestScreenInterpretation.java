package com.norsecraft.client.ymir.test;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.interpretation.SimpleGuiInterpretation;
import com.norsecraft.client.ymir.screen.TexturedBackgroundPainter;
import com.norsecraft.client.ymir.widget.YmirButton;
import com.norsecraft.client.ymir.widget.YmirPlainPanel;
import com.norsecraft.client.ymir.widget.data.Insets;
import com.norsecraft.client.ymir.widget.data.Texture;
import net.minecraft.util.Identifier;

public class YmirTestScreenInterpretation extends SimpleGuiInterpretation {

    private static final Identifier BACKGROUND_TEXTURE = NorseCraftMod.ncTex("gui/npc_gui_talk.png");
    private static final Identifier COMPONENTS = NorseCraftMod.ncTex("gui/gui_components.png");

    public YmirTestScreenInterpretation() {
        YmirPlainPanel root = new YmirPlainPanel();
        root.setInsets(Insets.NONE);
        root.setBackgroundPainter(new TexturedBackgroundPainter(new Texture(BACKGROUND_TEXTURE, 0, 0, 458, 355, 1024F, 512F)));

        YmirButton button1 = new YmirButton(new Texture(COMPONENTS, 0, 0, 34, 35, 1024F, 512F));
        button1.setOnClick(() -> {
            NorseCraftMod.LOGGER.info("CLICKED 1 :-)");
        });
        root.add(button1, 10, 0, 36, 36);
        this.setRootPanel(root);
        root.validate(this);

    }
}
