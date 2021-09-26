package com.norsecraft.common.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.village.Merchant;
import net.minecraft.village.MerchantInventory;

public interface NorseCraftMerchant<E extends LivingEntity & Merchant> extends Merchant {

    E getMerchantEntity();

    MerchantInventory getMerchantInventory();

}
