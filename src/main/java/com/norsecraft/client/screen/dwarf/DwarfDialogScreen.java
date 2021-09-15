package com.norsecraft.client.screen.dwarf;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.render.TextureSprite;
import com.norsecraft.client.screen.FenrirDrawHelper;
import com.norsecraft.client.screen.widget.ImageButton;
import com.norsecraft.client.screen.widget.Label;
import com.norsecraft.client.screen.widget.TextButton;
import com.norsecraft.client.screen.widget.WidgetBounds;
import com.norsecraft.client.screen.widget.dialog.DialogField;
import com.norsecraft.common.dialog.Dialog;
import com.norsecraft.common.dialog.DialogGroup;
import com.norsecraft.common.entity.IDialogEntity;
import com.norsecraft.common.entity.dwarf.DwarfBlacksmithEntity;
import com.norsecraft.common.network.c2s.OpenScreenPacketC2S;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Optional;

public class DwarfDialogScreen extends Screen {

    private static final Identifier BACKGROUND_TEXTURE = NorseCraftMod.ncTex("gui/npc_gui_talk.png");
    private int posX;
    private int posY;
    private final TextureSprite backgroundSprite = new TextureSprite(143, 0, 335, 315);
    private final TextureSprite leftInfoSprite = new TextureSprite(0, 0, 178, 142);
    private final TextureSprite[] reputationSymbols = new TextureSprite[]{
            new TextureSprite(459, 0, 24, 24), //Good
            new TextureSprite(459, 26, 24, 24),//Neutral
            new TextureSprite(459, 52, 24, 24) //Bad
    };
    private final TextureSprite[] buttons = new TextureSprite[]{
            new TextureSprite(459, 78, 34, 34),
            new TextureSprite(494, 114, 34, 34),
            new TextureSprite(494, 150, 34, 34)
    };

    private final TextureSprite[] hoverButtons = new TextureSprite[]{
            new TextureSprite(529, 78, 34, 34),
            new TextureSprite(459, 114, 34, 34),
            new TextureSprite(459, 150, 34, 34)
    };
    private final IDialogEntity<DwarfBlacksmithEntity> dialogEntity;
    private final DialogGroup dialogGroup;

    private Dialog currentDialog;
    private final AnswerButton[] answerButtons = new AnswerButton[3];
    private DialogField dialogField;

    private final int maxTextWidth = 196;

    public DwarfDialogScreen(IDialogEntity<DwarfBlacksmithEntity> dialogEntity) {
        super(new LiteralText("Dwarf Dialog"));
        this.dialogEntity = dialogEntity;
        this.dialogGroup = dialogEntity.getDialogGroup();
        this.currentDialog = this.dialogGroup.getCurrentDialog();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void setForNewDialog(Dialog dialog) {
        Dialog.DialogAnswer[] answers = dialog.getAnswers();
        if (answers.length <= 0 || answers.length > 3) {
            NorseCraftMod.LOGGER.error("There are mot then 3 answers", new RuntimeException());
            return;
        }

        int nextDialogIndex = this.dialogField.getNextDialogLineIndex();
        this.dialogField.setDialogLine(nextDialogIndex, dialog.getMessage(), Color.WHITE);
        for (AnswerButton answerButton : answerButtons) answerButton.visible = false;

        for (int i = 0; i < answers.length; i++) {
            this.answerButtons[i].setMessage(answers[i].getMessage());
            this.answerButtons[i].setNextAnswerId(answers[i].getReferenceDialogId());
            this.answerButtons[i].visible = true;
        }

    }

    private final ButtonWidget.PressAction answerAction = (button) -> {
        if (button instanceof AnswerButton answerButton) {
            Optional<Dialog> dialog = NorseCraftMod.getDialogManager().findDialogById(answerButton.nextAnswerId);
            if (dialog.isEmpty()) {
                NorseCraftMod.LOGGER.error("Dialog with id {} not found", answerButton.nextAnswerId, new IllegalComponentStateException());
                return;
            }
            this.dialogField.setDialogLine(this.dialogField.getNextDialogLineIndex(), answerButton.getMessage().asString(), Color.ORANGE);
            setForNewDialog(dialog.get());
        }
    };

    @Override
    protected void init() {
        this.posX = (this.width - 176) / 2;
        this.posY = (this.height - 166) / 2;

        this.addDrawableChild(new ImageButton(posX - 21, posY + 29, 17, 17, LiteralText.EMPTY, BACKGROUND_TEXTURE, buttons[0], hoverButtons[0], true, FenrirDrawHelper.EMPTY_ACTION));
        this.addDrawableChild(new ImageButton(posX - 21, posY + 49, 17, 17, LiteralText.EMPTY, BACKGROUND_TEXTURE, buttons[1], hoverButtons[1], true,
                (button) -> {
                    DwarfBlacksmithEntity dwarf = dialogEntity.getDialogEntity();
                    client.setScreen(null);
                    ClientPlayNetworking.send(OpenScreenPacketC2S.OPEN_SCREEN_PACKET_ID, new OpenScreenPacketC2S(dwarf.getId()).write());
                }));
        this.addDrawableChild(new ImageButton(posX - 21, posY + 69, 17, 17, LiteralText.EMPTY, BACKGROUND_TEXTURE, buttons[2], hoverButtons[2], true,
                (button) -> {
                    NorseCraftMod.LOGGER.info("Button 2 clicked...");
                }));

        this.addDrawable(new Label(posX - 62, posY + 11, this.dialogEntity.getDialogEntity().getDisplayName().asString()));

        this.answerButtons[0] = this.addDrawableChild(new AnswerButton(posX + 7, posY + 117, 9, answerAction));
        this.answerButtons[1] = this.addDrawableChild(new AnswerButton(posX + 7, posY + 133, 9, answerAction));
        this.answerButtons[2] = this.addDrawableChild(new AnswerButton(posX + 7, posY + 149, 9, answerAction));

        this.dialogField = new DialogField(new WidgetBounds(posX + 7, posY, maxTextWidth, 100), 8);
        this.dialogField.setScrollbar(new TextureSprite(484, 0, 9, 60), BACKGROUND_TEXTURE, new WidgetBounds(437, 17, 60, 9));
        this.dialogField.init();
        this.addDrawable(this.dialogField);

        this.setForNewDialog(this.currentDialog);

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        //Draw background
        FenrirDrawHelper.drawSprite(matrices, BACKGROUND_TEXTURE, posX, posY, backgroundSprite);
        //Draw left info
        FenrirDrawHelper.drawSprite(matrices, BACKGROUND_TEXTURE, posX - 70, posY, leftInfoSprite);
        //Reputation symbol
        this.renderReputationSymbol(matrices);

        super.render(matrices, mouseX, mouseY, delta);
    }

    private void renderReputationSymbol(MatrixStack matrices) {
        int reputation = this.dialogEntity.getDialogEntity().getPlayerReputation(this.client.player);
        TextureSprite sprite;
        if (reputation >= 50)
            sprite = reputationSymbols[0];
        else if (reputation >= -50)
            sprite = reputationSymbols[1];
        else
            sprite = reputationSymbols[2];

        FenrirDrawHelper.drawSprite(matrices, BACKGROUND_TEXTURE, posX - 17, posY + 7, sprite);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        this.dialogField.handleScrollbar(amount);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    class AnswerButton extends TextButton {

        String nextAnswerId;

        public AnswerButton(int x, int y, int height, PressAction onPress) {
            super(x, y, height, "", Color.WHITE, onPress);
        }

        public void setNextAnswerId(String nextAnswerId) {
            this.nextAnswerId = nextAnswerId;
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            FenrirDrawHelper.drawTextWithLength(matrices, x, y, this.getMessage(), maxTextWidth, 5, color);
        }
    }


}
