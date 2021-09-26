package com.norsecraft.client.gui.dwarf;

import com.norsecraft.client.ymir.widget.YmirListPanel;
import com.norsecraft.client.ymir.widget.YmirText;
import net.minecraft.text.LiteralText;

import java.awt.*;
import java.util.ArrayList;

public class DialogField extends YmirListPanel<String, YmirText> {

    private boolean entity = true;

    public DialogField() {
        super(new ArrayList<>(), () -> new YmirText(new LiteralText("")), null);
        this.configurator = (s, ymirText) -> {
            if (entity) {
                ymirText.setColor(Color.WHITE.getRGB());
                entity = false;
            } else {
                ymirText.setColor(Color.ORANGE.getRGB());
                entity = true;
            }
            ymirText.setText(new LiteralText(s));
        };
    }

    public void addMessage(String message) {
        this.data.add(message);
        this.layout();
    }

}
