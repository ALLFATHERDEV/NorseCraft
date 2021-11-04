package com.norsecraft.client.ymir;

import net.minecraft.screen.PropertyDelegate;

/**
 * Implement this class to a {@link net.minecraft.block.entity.BlockEntity} or another object that holds a {@link PropertyDelegate}
 * PropertyDelegates are from mc, and very useful to sync integer data form server to client for guis
 */
public interface PropertyDelegateHolder {

    /**
     * @return the property delegate
     */
    PropertyDelegate getPropertyDelegate();

}
