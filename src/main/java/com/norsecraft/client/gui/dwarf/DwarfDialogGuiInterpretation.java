package com.norsecraft.client.gui.dwarf;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.interpretation.SimpleGuiInterpretation;
import com.norsecraft.client.ymir.screen.TexturedBackgroundPainter;
import com.norsecraft.client.ymir.widget.YmirButton;
import com.norsecraft.client.ymir.widget.YmirPlainPanel;
import com.norsecraft.client.ymir.widget.data.Insets;
import com.norsecraft.client.ymir.widget.data.Texture;
import net.minecraft.util.Identifier;

public class DwarfDialogGuiInterpretation extends SimpleGuiInterpretation {

    private static final Identifier BACKGROUND_TEXTURE = NorseCraftMod.ncTex("gui/npc_gui_talk.png");

    public DwarfDialogGuiInterpretation() {
        YmirPlainPanel root = new YmirPlainPanel();
        root.setInsets(Insets.ROOT_PANEL);
        root.setBackgroundPainter(new TexturedBackgroundPainter(new Texture(BACKGROUND_TEXTURE, 0, 0, 458, 355, 1024F, 512F)));

        YmirButton dialog = new YmirButton(new Texture(COMPONENTS, 0, 0, 34, 35, 1024, 512F));
        root.add(dialog, 99, 58, 36, 36);

    }
    /*
     root.setInsets(Insets.ROOT_PANEL);
        root.setBackgroundPainter(new TexturedBackgroundPainter(new Texture(BACKGROUND_TEXTURE, 0, 0, 458, 355, 1024F, 512F)));

        /*YmirButton button1 = new YmirButton(new Texture(COMPONENTS, 0, 0, 34, 35, 1024F, 512F));
        button1.setOnClick(() -> {
            NorseCraftMod.LOGGER.info("CLICKED 1 :-)");
        });
        root.add(button1, 10, 0, 36, 36);
    List<String> l = Lists.newArrayList(
            "Crosses grow on Anzio " +
                    "Where no soldiers sleep " +
                    "And where hell’s six feet deep " +
                    "That death does wait " +
                    "There’s no debate " +
                    "So charge and attack " +
                    "Going to Hell and Back ",
            "My heart is high of clouds " +
                    "The presence of your love " +
                    "For drawn to you as tight as wind ",
            "’ll stand by you through it all " +
                    "The stillness of my heart " +
                    "Every breathe I take is all for you",
            "Fall into me " +
                    "With every part of you " +
                    "Share this world, the seas, the stars, eternity " +
                    "My Lady",
            "She walks in beauty’s light " +
                    "Her lips of softness " +
                    "Gracefully beautiful as she",
            "Ihr braucht Salus für Gesundheit, Ares für die Kriege " +
                    "Loki für das Feuer, Aphrodite für die Liebe",
            "Ihr braucht Flora für den Frühling, Kratos für Macht " +
                    "Und Allah für all das, was keinen Spaß macht " +
                    "Pan für die Wälder, Nox für die Nacht"
    );
    YmirListPanel<String, YmirText> panel = new YmirListPanel<>(l, () -> new YmirText(new LiteralText("")),
            (str, line) -> {
                line.setText(new LiteralText(str));
            });
        panel.setListItemHeight(50);
        root.add(panel, 70, 0, 148, 109);
        this.setRootPanel(root);
        root.validate(this);
     */

}
