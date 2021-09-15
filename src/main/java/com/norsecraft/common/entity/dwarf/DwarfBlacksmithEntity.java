package com.norsecraft.common.entity.dwarf;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.dialog.DialogGroup;
import com.norsecraft.common.entity.IDialogEntity;
import com.norsecraft.common.screenhandler.DwarfTradeScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DwarfBlacksmithEntity extends DwarfEntity implements Merchant, NamedScreenHandlerFactory, IDialogEntity<DwarfBlacksmithEntity> {

    private static final TradeOfferList OFFERS = new TradeOfferList();
    private PlayerEntity customer;

    public DwarfBlacksmithEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createDwarfBlacksmithAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D).add(EntityAttributes.GENERIC_MAX_HEALTH, 40)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 40.0);
    }

    @Override
    public void setCurrentCustomer(@Nullable PlayerEntity customer) {
        this.customer = customer;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        this.setCurrentCustomer(null);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.world.isClient && !OFFERS.isEmpty()) {
            ExtendedScreenHandlerFactory factory = new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                    buf.writeInt(DwarfBlacksmithEntity.this.getId());
                }

                @Override
                public Text getDisplayName() {
                    return new LiteralText("Dwarf Trade");
                }

                @Nullable
                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return DwarfBlacksmithEntity.this.createMenu(syncId, inv, player);
                }
            };
            this.setCurrentCustomer(player);
            player.openHandledScreen(factory);
        }
        return super.interactMob(player, hand);
    }

    @Override
    public DialogGroup getDialogGroup() {
        return NorseCraftMod.getDialogManager().getDialogGroupByName("dialog_0").orElseThrow();
    }

    @Override
    public DwarfBlacksmithEntity getDialogEntity() {
        return this;
    }

    @Nullable
    @Override
    public PlayerEntity getCurrentCustomer() {
        return this.customer;
    }

    @Override
    public TradeOfferList getOffers() {
        return OFFERS;
    }

    @Override
    public void setOffersFromServer(TradeOfferList offers) {
    }

    @Override
    public void trade(TradeOffer offer) {
        offer.use();
    }

    @Override
    public void onSellingItem(ItemStack stack) {
        if (!this.world.isClient && this.ambientSoundChance > -this.getMinAmbientSoundDelay() + 20) {
            this.ambientSoundChance = -this.getMinAmbientSoundDelay();
        }
    }

    @Override
    public World getMerchantWorld() {
        return this.world;
    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public void setExperienceFromServer(int experience) {

    }

    @Override
    public boolean isLeveledMerchant() {
        return false;
    }

    @Override
    public SoundEvent getYesSound() {
        return SoundEvents.ENTITY_VILLAGER_YES;
    }

    static {
        OFFERS.add(new TradeOffer(Items.NAME_TAG.getDefaultStack(), Items.DIAMOND.getDefaultStack(), 9999, 0, 0));
        OFFERS.add(new TradeOffer(Items.DIRT.getDefaultStack(), Items.EMERALD.getDefaultStack(), 9999, 0, 0));
        OFFERS.add(new TradeOffer(Items.IRON_INGOT.getDefaultStack(), Items.PUMPKIN.getDefaultStack(), Items.DIAMOND.getDefaultStack(), 9999, 0, 0));
        OFFERS.add(new TradeOffer(Items.IRON_INGOT.getDefaultStack(), Items.IRON_SWORD.getDefaultStack(), Items.EMERALD_BLOCK.getDefaultStack(), 9999, 0, 0));
        OFFERS.add(new TradeOffer(Items.IRON_INGOT.getDefaultStack(), Items.REDSTONE.getDefaultStack(), Items.LAPIS_BLOCK.getDefaultStack(), 9999, 0, 0));
        OFFERS.add(new TradeOffer(Items.STICK.getDefaultStack(), Items.DIAMOND.getDefaultStack(), 9999, 0, 0));
        OFFERS.add(new TradeOffer(Items.IRON_INGOT.getDefaultStack(), Items.ACACIA_BOAT.getDefaultStack(), 9999, 0, 0));
        OFFERS.add(new TradeOffer(Items.PAPER.getDefaultStack(), Items.ACACIA_LOG.getDefaultStack(), 9999, 0, 0));
        OFFERS.add(new TradeOffer(Items.IRON_INGOT.getDefaultStack(), Items.OAK_FENCE.getDefaultStack(), 9999, 0, 0));
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new DwarfTradeScreenHandler(syncId, inv, this);
    }
}
