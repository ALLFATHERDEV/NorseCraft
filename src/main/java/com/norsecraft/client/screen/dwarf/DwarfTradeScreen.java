package com.norsecraft.client.screen.dwarf;

import com.mojang.blaze3d.systems.RenderSystem;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.render.TextureSprite;
import com.norsecraft.client.screen.FenrirDrawHelper;
import com.norsecraft.client.screen.widget.ImageButton;
import com.norsecraft.client.screen.widget.Label;
import com.norsecraft.common.network.c2s.SelectMerchantRecipeIndexPacketC2S;
import com.norsecraft.common.screenhandler.DwarfTradeScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;

import java.util.Iterator;

public class DwarfTradeScreen extends HandledScreen<DwarfTradeScreenHandler> {

    private static final Identifier MERCHANT_GUI_TEXTURE = NorseCraftMod.ncTex("gui/npc_gui_trade.png");

    private int selectedMerchantRecipe;
    private final TradeButton[] offersButton = new TradeButton[5];
    private final TextureSprite backgroundSprite = new TextureSprite(143, 0, 332, 577);
    int indexStartOffset;
    private boolean scrolling;

    public DwarfTradeScreen(DwarfTradeScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 275;
        this.backgroundHeight = 165;
    }

    private void syncRecipeIndex() {
        this.handler.setCurrentRecipeIndex(this.selectedMerchantRecipe);
        this.handler.switchTo(this.selectedMerchantRecipe);
        ClientPlayNetworking.send(SelectMerchantRecipeIndexPacketC2S.SELECT_MERCHANT_RECIPE_INDEX, new SelectMerchantRecipeIndexPacketC2S(this.selectedMerchantRecipe).write());
    }

    private final TextureSprite[] buttons = new TextureSprite[]{
            new TextureSprite(756, 0, 34, 34),
            new TextureSprite(721, 36, 34, 34),
            new TextureSprite(756, 72, 34, 34)
    };

    private final TextureSprite[] hoverButtons = new TextureSprite[]{
            new TextureSprite(721, 0, 34, 34),
            null,
            new TextureSprite(721, 72, 34, 34)
    };

    private final ButtonWidget.PressAction[] actions = new ButtonWidget.PressAction[]{
            (button) -> {
                MinecraftClient.getInstance().setScreen(new DwarfDialogScreen(handler.getMerchant()));
            },
            (button) -> {

            },
            (button) -> {
                NorseCraftMod.LOGGER.info("DEBUG_3");
            }
    };

    @Override
    protected void init() {
        super.init();
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        int k = j + 7;

        for (int l = 0; l < 5; ++l) {
            this.offersButton[l] = this.addDrawableChild(new TradeButton(i + 185, k, l, (button) -> {
                if (button instanceof TradeButton) {
                    this.selectedMerchantRecipe = ((TradeButton) button).getIndex() + this.indexStartOffset;
                    NorseCraftMod.LOGGER.info("Selected recipe index: {}", this.selectedMerchantRecipe);
                    this.syncRecipeIndex();
                }
            }));
            k += 28;
        }

        this.addDrawableChild(new ImageButton(i - 21, j + 29, 17, 17, LiteralText.EMPTY, MERCHANT_GUI_TEXTURE, buttons[0], hoverButtons[0], true,
                (button) -> {
                    //MinecraftClient.getInstance().setScreen(new DwarfDialogScreenNEW());
                    //MinecraftClient.getInstance().setScreen(new DwarfDialogScreen(this.handler.getMerchant()));
                }));
        this.addDrawableChild(new ImageButton(i - 21, j + 49, 17, 17, LiteralText.EMPTY, MERCHANT_GUI_TEXTURE, buttons[1], hoverButtons[1], true,
                (button) -> {
                }));
        this.addDrawableChild(new ImageButton(i - 21, j + 69, 17, 17, LiteralText.EMPTY, MERCHANT_GUI_TEXTURE, buttons[2], hoverButtons[2], true,
                (button) -> {
                }));


        this.addDrawable(new Label(i - 62, j + 11, this.handler.getMerchant().getDisplayName().asString()));
    }


    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        TradeOfferList offers = this.handler.getOffers();
        if (!offers.isEmpty()) {
            int i = (this.width - this.backgroundWidth) / 2;
            int j = (this.height - this.backgroundHeight) / 2;
            int k = j + 7;
            int l = i + 5 + 5 + 180;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, MERCHANT_GUI_TEXTURE);
            int m = 0;
            Iterator<TradeOffer> iterator = offers.iterator();
            while (true) {
                TradeOffer offer;
                while (iterator.hasNext()) {
                    offer = iterator.next();
                    if (this.canScroll(offers.size()) && (m < this.indexStartOffset || m >= 5 + this.indexStartOffset))
                        ++m;
                    else {
                        ItemStack itemstack = offer.getOriginalFirstBuyItem();
                        ItemStack itemstack2 = offer.getAdjustedFirstBuyItem();
                        ItemStack itemstack3 = offer.getSecondBuyItem();
                        ItemStack itemstack4 = offer.getSellItem();
                        this.itemRenderer.zOffset = 100.0F;
                        int n = k + 6;
                        this.renderFirstBuyItem(matrices, itemstack2, itemstack, l, n);
                        if (!itemstack3.isEmpty()) {
                            int o = 215;
                            this.itemRenderer.renderInGui(itemstack3, i + o, n);
                            this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemstack3, i + o, n);
                        }

                        int p = 250;
                        this.renderArrow(matrices, offer, i, n);
                        this.itemRenderer.renderInGui(itemstack4, i + p, n);
                        this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemstack4, i + p, n);
                        this.itemRenderer.zOffset = 0.0F;
                        k += 28;
                        ++m;
                    }
                }

                int o = this.selectedMerchantRecipe;
                offer = offers.get(o);

                TradeButton[] buttons = this.offersButton;
                int p = buttons.length;

                for (int q = 0; q < p; ++q) {
                    TradeButton button = buttons[q];
                    if (button.isHovered())
                        button.renderTooltip(matrices, mouseX, mouseY);

                    button.visible = button.index < this.handler.getOffers().size();
                }
                RenderSystem.enableDepthTest();
                break;
            }
        }

        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, MERCHANT_GUI_TEXTURE);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        FenrirDrawHelper.drawSprite(matrices, MERCHANT_GUI_TEXTURE, i, j, backgroundSprite, this.getZOffset());
        this.renderLeftInfo(matrices);
        this.renderReputationSymbols(matrices);
        this.renderScrollbar(matrices, i, j, this.handler.getOffers());
    }

    private final TextureSprite scrollBarNormal = new TextureSprite(722, 109, 58, 7);
    private final TextureSprite scrollBarHover = new TextureSprite(733, 109, 58, 7);

    private void renderScrollbar(MatrixStack matix, int x, int y, TradeOfferList offers) {
        int i = offers.size() + 1 - 5;
        if (i > 1) {
            int j = 139 - (27 + (i - 1) * 139 / i);
            int k = 1 + j / i + 139 / i;
            int m = Math.min(113, this.indexStartOffset * k);
            if (this.indexStartOffset == i - 1) {
                m = 106;
            }
            FenrirDrawHelper.drawSprite(matix, MERCHANT_GUI_TEXTURE, x + 277, y + 8 + m, scrollBarNormal, this.getZOffset(), 512, 256);
        } else {
            FenrirDrawHelper.drawSprite(matix, MERCHANT_GUI_TEXTURE, x + 277, y + 8, scrollBarHover, this.getZOffset(), 512, 256);
        }
    }

    private final TextureSprite leftInfo = new TextureSprite(0, 0, 178, 142);

    private void renderLeftInfo(MatrixStack matrixStack) {
        int i = (this.width - this.backgroundWidth) / 2 - 70;
        int j = (this.height - this.backgroundHeight) / 2;
        FenrirDrawHelper.drawSprite(matrixStack, MERCHANT_GUI_TEXTURE, i, j, leftInfo, 512, 256);
    }

    private final TextureSprite[] reputationSymbols = new TextureSprite[]{
            new TextureSprite(826, 0, 24, 24), //Good
            new TextureSprite(826, 25, 24, 24),//Neutral
            new TextureSprite(826, 50, 24, 24) //Bad
    };

    private void renderReputationSymbols(MatrixStack matrixStack) {
        int i = (this.width - this.backgroundWidth) / 2 - 17;
        int j = (this.height - this.backgroundHeight) / 2 + 7;
        int reputation = this.handler.getMerchant().getPlayerReputation(this.client.player);
        TextureSprite sprite;
        if (reputation >= 50)
            sprite = reputationSymbols[0];
        else if (reputation >= -50)
            sprite = reputationSymbols[1];
        else
            sprite = reputationSymbols[2];
        FenrirDrawHelper.drawSprite(matrixStack, MERCHANT_GUI_TEXTURE, i, j, sprite, 512, 256);
    }

    private void renderArrow(MatrixStack matrix, TradeOffer offer, int x, int y) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, MERCHANT_GUI_TEXTURE);
        if (offer.isDisabled()) {
            drawTexture(matrix, x + 5 + 35 + 20, y + 3, this.getZOffset(), 25.0F, 171.0F, 10, 9, 256, 512);
        } else {
            drawTexture(matrix, x + 5 + 35 + 20, y + 3, this.getZOffset(), 15.0F, 171.0F, 10, 9, 256, 512);
        }
    }

    private void renderFirstBuyItem(MatrixStack matrix, ItemStack adjustedFirstBuyItem, ItemStack originalFirstBuyItem, int x, int y) {
        this.itemRenderer.renderInGui(adjustedFirstBuyItem, x, y);
        if (originalFirstBuyItem.getCooldown() == adjustedFirstBuyItem.getCooldown()) {
            this.itemRenderer.renderGuiItemOverlay(this.textRenderer, adjustedFirstBuyItem, x, y);
        } else {
            this.itemRenderer.renderGuiItemOverlay(this.textRenderer, originalFirstBuyItem, x, y, originalFirstBuyItem.getCount() == 1 ? "1" : null);
            this.itemRenderer.renderGuiItemOverlay(this.textRenderer, adjustedFirstBuyItem, x + 14, y, adjustedFirstBuyItem.getCount() == 1 ? "1" : null);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, MERCHANT_GUI_TEXTURE);
            this.setZOffset(this.getZOffset() + 300);
            drawTexture(matrix, x + 7, y + 12, this.getZOffset(), 0.0F, 176.0F, 9, 2, 256, 512);
            this.setZOffset(this.getZOffset() - 300);
        }
    }

    private boolean canScroll(int listSize) {
        return listSize > 7;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int i = this.handler.getOffers().size();
        if (this.canScroll(i)) {
            int j = i - 5;
            this.indexStartOffset = (int) ((double) this.indexStartOffset - amount);
            this.indexStartOffset = MathHelper.clamp(this.indexStartOffset, 0, j);
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        int i = this.handler.getOffers().size();
        if (this.scrolling) {
            int j = this.y + 18;
            int k = j + 139;
            int l = i - 5;
            float f = ((float) mouseY - (float) j - 13.5F) / ((float) (k - j) - 27.0F);
            f = f * (float) l + 0.5F;
            this.indexStartOffset = MathHelper.clamp((int) f, 0, l);
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = false;
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        if (this.canScroll(this.handler.getOffers().size()) && mouseX > (double) (i + 94) && mouseX < (double) (i + 94 + 6) && mouseY > (double) (j + 18) && mouseY <= (double) (j + 18 + 139 + 1)) {
            this.scrolling = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    class TradeButton extends ImageButton {

        final int index;

        public TradeButton(int x, int y, int index, PressAction onPress) {
            super(x, y, 89, 27, LiteralText.EMPTY, MERCHANT_GUI_TEXTURE, new TextureSprite(721, 167, 56, 173),
                    new TextureSprite(721, 224, 56, 173), false, onPress);
            this.index = index;
        }

        @Override
        public boolean shouldCustomRender() {
            return true;
        }

        @Override
        public void renderCustom(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
            FenrirDrawHelper.drawSprite(matrixStack, MERCHANT_GUI_TEXTURE, this.x, this.y, this.sprite);
        }

        public int getIndex() {
            return index;
        }

        @Override
        public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
            if (this.hovered && DwarfTradeScreen.this.handler.getOffers().size() > this.index + DwarfTradeScreen.this.indexStartOffset) {
                ItemStack itemStack3;
                if (mouseX < this.x + 20) {
                    itemStack3 = DwarfTradeScreen.this.handler.getOffers().get(this.index + DwarfTradeScreen.this.indexStartOffset).getAdjustedFirstBuyItem();
                    DwarfTradeScreen.this.renderTooltip(matrices, itemStack3, mouseX, mouseY);
                } else if (mouseX < this.x + 50 && mouseX > this.x + 30) {
                    itemStack3 = DwarfTradeScreen.this.handler.getOffers().get(this.index + DwarfTradeScreen.this.indexStartOffset).getSecondBuyItem();
                    if (!itemStack3.isEmpty()) {
                        DwarfTradeScreen.this.renderTooltip(matrices, itemStack3, mouseX, mouseY);
                    }
                } else if (mouseX > this.x + 65) {
                    itemStack3 = DwarfTradeScreen.this.handler.getOffers().get(this.index + DwarfTradeScreen.this.indexStartOffset).getSellItem();
                    DwarfTradeScreen.this.renderTooltip(matrices, itemStack3, mouseX, mouseY);
                }
            }
        }
    }
}
