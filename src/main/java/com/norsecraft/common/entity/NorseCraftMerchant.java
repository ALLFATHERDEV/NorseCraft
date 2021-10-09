package com.norsecraft.common.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.village.Merchant;
import net.minecraft.village.MerchantInventory;

/**
 * A extend version of the default mc merchant class
 *
 * @param <E> the merchant entity
 */
public interface NorseCraftMerchant<E extends LivingEntity & Merchant> extends Merchant {

    /**
     * @return the merchant entity
     */
    E getMerchantEntity();

    /**
     * @return the merchatn inventory
     */
    MerchantInventory getMerchantInventory();

}
