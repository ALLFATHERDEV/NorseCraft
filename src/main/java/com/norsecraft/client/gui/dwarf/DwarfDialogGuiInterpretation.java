package com.norsecraft.client.gui.dwarf;

import com.google.common.collect.Lists;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.interpretation.SimpleGuiInterpretation;
import com.norsecraft.client.ymir.screen.TexturedBackgroundPainter;
import com.norsecraft.client.ymir.widget.*;
import com.norsecraft.client.ymir.widget.data.InputResult;
import com.norsecraft.client.ymir.widget.data.Texture;
import com.norsecraft.common.dialog.Dialog;
import com.norsecraft.common.dialog.DialogGroup;
import com.norsecraft.common.entity.IDialogEntity;
import com.norsecraft.common.entity.dwarf.DwarfBlacksmithEntity;
import com.norsecraft.common.network.c2s.OpenScreenPacketC2S;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class DwarfDialogGuiInterpretation extends SimpleGuiInterpretation {

    private static final Identifier BACKGROUND_TEXTURE = NorseCraftMod.ncTex("gui/npc_gui_talk.png");

    private final IDialogEntity<DwarfBlacksmithEntity> dialogEntity;
    private final DialogGroup dialogGroup;
    private Dialog currentDialog;
    private AnswerWidget answer1;
    private AnswerWidget answer2;
    private AnswerWidget answer3;
    private final List<String> messages = Lists.newArrayList();

    public DwarfDialogGuiInterpretation(IDialogEntity<DwarfBlacksmithEntity> dialogEntity) {
        this.dialogEntity = dialogEntity;
        this.dialogGroup = dialogEntity.getDialogGroup();
        this.currentDialog = this.dialogGroup.getCurrentDialog();

        YmirPlainPanel panel = new YmirPlainPanel();
        panel.setBackgroundPainter(new TexturedBackgroundPainter(new Texture(BACKGROUND_TEXTURE, 0, 0, 458, 355, 1024F, 512F)));

        YmirButton dialogButton = new YmirButton(Texture.component(35, 0, 34, 36));
        dialogButton.setHovered(Texture.component(0, 0, 34, 36));
        panel.add(dialogButton, 50, 29, 34, 36);

        YmirButton tradingButton = new YmirButton(Texture.component(70, 35, 34, 36));
        tradingButton.setOnClick(() -> {
            DwarfBlacksmithEntity dwarf = dialogEntity.getDialogEntity();
            MinecraftClient.getInstance().setScreen(null);
            ClientPlayNetworking.send(OpenScreenPacketC2S.OPEN_SCREEN_PACKET_ID, new OpenScreenPacketC2S(dwarf.getId()).write());
        });
        panel.add(tradingButton, 50, 48, 34, 36);

        YmirButton questButton = new YmirButton(Texture.component(35, 72, 34, 36));
        questButton.setHovered(Texture.component(0, 72, 34, 36));
        questButton.setOnClick(() -> {
            NorseCraftMod.LOGGER.info("Quest");
        });
        panel.add(questButton, 50, 68, 34, 36);

        DialogField dialogField = new DialogField();
        dialogField.addMessage(this.currentDialog.getMessage());


        BiConsumer<String, Text> onAnswered = (nextAnswerId, text) -> {
            Optional<Dialog> dialog = NorseCraftMod.getDialogManager().findDialogById(nextAnswerId);
            if (dialog.isEmpty()) {
                NorseCraftMod.LOGGER.warn("Tried to find a dialog with id: {}, dialog not found", nextAnswerId);
                return;
            }
            dialogField.addMessage(text.asString());
            nextDialog(dialog.get());
        };

        this.answer1 = new AnswerWidget(160, 15, onAnswered);
        this.answer1.setEnabled(false);
        this.answer2 = new AnswerWidget(160, 15, onAnswered);
        this.answer2.setEnabled(false);
        this.answer3 = new AnswerWidget(160, 15, onAnswered);
        this.answer3.setEnabled(false);

        Dialog.DialogAnswer[] answers = this.currentDialog.getAnswers();
        for (int i = 0; i < answers.length; i++) {
            if (i == 0) {
                this.answer1.setNextAnswerId(answers[i].getReferenceDialogId());
                this.answer1.setMessage(new LiteralText(answers[i].getMessage()));
                this.answer1.setEnabled(true);
            } else if (i == 1) {
                this.answer2.setNextAnswerId(answers[i].getReferenceDialogId());
                this.answer2.setMessage(new LiteralText(answers[i].getMessage()));
                this.answer2.setEnabled(true);
            } else if (i == 2) {
                this.answer3.setNextAnswerId(answers[i].getReferenceDialogId());
                this.answer3.setMessage(new LiteralText(answers[i].getMessage()));
                this.answer3.setEnabled(true);
            } else {
                break;
            }
        }

        panel.add(dialogField, 79, 6, 146, 111);
        panel.add(answer1, 79, 120);
        panel.add(answer2, 79, 135);
        panel.add(answer3, 79, 150);

        setRootPanel(panel);
        panel.validate(this);
    }

    private void nextDialog(Dialog dialog) {
        messages.add(dialog.getMessage());
        Dialog.DialogAnswer[] answers = dialog.getAnswers();
        answer1.setEnabled(false);
        answer2.setEnabled(false);
        answer3.setEnabled(false);

        for (int i = 0; i < answers.length; i++) {
            if (i == 0) {
                this.answer1.setNextAnswerId(answers[i].getReferenceDialogId());
                this.answer1.setMessage(new LiteralText(answers[i].getMessage()));
                this.answer1.setEnabled(true);
            } else if (i == 1) {
                this.answer2.setNextAnswerId(answers[i].getReferenceDialogId());
                this.answer2.setMessage(new LiteralText(answers[i].getMessage()));
                this.answer2.setEnabled(true);
            } else if (i == 2) {
                this.answer3.setNextAnswerId(answers[i].getReferenceDialogId());
                this.answer3.setMessage(new LiteralText(answers[i].getMessage()));
                this.answer3.setEnabled(true);
            } else {
                break;
            }
        }

    }

    public static class AnswerWidget extends YmirWidget {

        private String nextAnswerId;
        private final BiConsumer<String, Text> onAnswered;
        private Text message;
        private boolean enabled = true;
        private YmirText text;

        public AnswerWidget(int width, int height, BiConsumer<String, Text> onAnswered) {
            this.setSize(width, height);
            this.onAnswered = onAnswered;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void setNextAnswerId(String nextAnswerId) {
            this.nextAnswerId = nextAnswerId;
        }

        public void setMessage(Text message) {
            if (this.text == null)
                this.text = new YmirText(message).setColor(Color.WHITE.getRGB());
            else
                this.text.setText(message);
            this.text.setSize(this.width, this.height);
            this.message = message;
        }

        @Override
        public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
            if (enabled)
                this.text.paint(matrices, x, y, mouseX, mouseY);
        }

        @Override
        public InputResult onClick(int x, int y, int button) {
            if (isWithinBounds(x, y) && enabled) {
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                onAnswered.accept(this.nextAnswerId, message);
                return InputResult.PROCESSED;
            }
            return InputResult.IGNORED;
        }
    }

}
