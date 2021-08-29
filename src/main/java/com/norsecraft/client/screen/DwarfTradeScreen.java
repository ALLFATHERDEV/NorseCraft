package com.norsecraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.network.c2s.SelectMerchantRecipeIndexPacketC2S;
import com.norsecraft.common.screenhandler.DwarfTradeScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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

    private static final Identifier MERCHANT_GUI_TEXTURE = NorseCraftMod.ncTex("gui/trader_gui_trade.png");

    private int selectedMerchantRecipe;
    private final WidgetButtonPage[] offersButton = new WidgetButtonPage[7];
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

    @Override
    protected void init() {
        super.init();
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        int k = j + 24;

        for (int l = 0; l < 7; ++l) {
            this.offersButton[l] = this.addDrawableChild(new WidgetButtonPage(i + 5, k, l, (button) -> {
                if (button instanceof WidgetButtonPage) {
                    this.selectedMerchantRecipe = ((WidgetButtonPage) button).getIndex() + this.indexStartOffset;
                    this.syncRecipeIndex();
                }
            }));
            k += 20;
        }

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
            int k = j + 16 + 1;
            int l = i + 5 + 5;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, MERCHANT_GUI_TEXTURE);
            this.renderScrollbar(matrices, i, j, offers);
            int m = 0;
            Iterator<TradeOffer> iterator = offers.iterator();
            while (true) {
                TradeOffer offer;
                while (iterator.hasNext()) {
                    offer = iterator.next();
                    if (this.canScroll(offers.size()) && (m < this.indexStartOffset || m >= 7 + this.indexStartOffset))
                        ++m;
                    else {
                        ItemStack itemstack = offer.getOriginalFirstBuyItem();
                        ItemStack itemstack2 = offer.getAdjustedFirstBuyItem();
                        ItemStack itemstack3 = offer.getSecondBuyItem();
                        ItemStack itemstack4 = offer.getSellItem();
                        this.itemRenderer.zOffset = 100.0F;
                        int n = k + 7;
                        this.renderFirstBuyItem(matrices, itemstack2, itemstack, l, n);
                        if (!itemstack3.isEmpty()) {
                            this.itemRenderer.renderInGui(itemstack3, i + 5 + 35, n);
                            this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemstack3, i + 5 + 35, n);
                        }

                        this.renderArrow(matrices, offer, i, n);
                        this.itemRenderer.renderInGui(itemstack4, i + 5 + 68, n);
                        this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemstack4, i + 5 + 68, n);
                        this.itemRenderer.zOffset = 0.0F;
                        k += 20;
                        ++m;
                    }
                }

                int o = this.selectedMerchantRecipe;
                offer = offers.get(o);

                WidgetButtonPage[] buttons = this.offersButton;
                int p = buttons.length;

                for (int q = 0; q < p; ++q) {
                    WidgetButtonPage button = buttons[q];
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
        drawTexture(matrices, i, j, this.getZOffset(), 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 512);
    }

    private void renderScrollbar(MatrixStack matix, int x, int y, TradeOfferList offers) {
        int i = offers.size() + 1 - 7;
        if (i > 1) {
            int j = 139 - (27 + (i - 1) * 139 / i);
            int k = 1 + j / i + 139 / i;
            int m = Math.min(113, this.indexStartOffset * k);
            if (this.indexStartOffset == i - 1) {
                m = 113;
            }

            drawTexture(matix, x + 94, y + 18 + m, this.getZOffset(), 0.0F, 199.0F, 6, 27, 256, 512);
        } else {
            drawTexture(matix, x + 94, y + 18, this.getZOffset(), 6.0F, 199.0F, 6, 27, 256, 512);
        }
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
            int j = i - 7;
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
            int l = i - 7;
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

    @Environment(EnvType.CLIENT)
    class WidgetButtonPage extends ButtonWidget {

        final int index;

        public WidgetButtonPage(int x, int y, int index, PressAction onPress) {
            super(x, y, 89, 20, LiteralText.EMPTY, onPress);
            this.index = index;
        }


        public int getIndex() {
            return index;
        }

        @Override
        public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
            if (this.hovered && ((DwarfTradeScreenHandler) DwarfTradeScreen.this.handler).getOffers().size() > this.index + DwarfTradeScreen.this.indexStartOffset) {
                ItemStack itemStack3;
                if (mouseX < this.x + 20) {
                    itemStack3 = ((TradeOffer) ((DwarfTradeScreenHandler) DwarfTradeScreen.this.handler).getOffers().get(this.index + DwarfTradeScreen.this.indexStartOffset)).getAdjustedFirstBuyItem();
                    DwarfTradeScreen.this.renderTooltip(matrices, itemStack3, mouseX, mouseY);
                } else if (mouseX < this.x + 50 && mouseX > this.x + 30) {
                    itemStack3 = ((TradeOffer) ((DwarfTradeScreenHandler) DwarfTradeScreen.this.handler).getOffers().get(this.index + DwarfTradeScreen.this.indexStartOffset)).getSecondBuyItem();
                    if (!itemStack3.isEmpty()) {
                        DwarfTradeScreen.this.renderTooltip(matrices, itemStack3, mouseX, mouseY);
                    }
                } else if (mouseX > this.x + 65) {

                    itemStack3 = ((TradeOffer) ((DwarfTradeScreenHandler) DwarfTradeScreen.this.handler).getOffers().get(this.index + DwarfTradeScreen.this.indexStartOffset)).getSellItem();
                    DwarfTradeScreen.this.renderTooltip(matrices, itemStack3, mouseX, mouseY);
                }
            }
        }
    }

}
