package com.norsecraft.common.entity;

import com.norsecraft.common.dialog.DialogGroup;
import net.minecraft.entity.LivingEntity;

/**
 * If a entity has a dialog option, it have to implement this interface
 *
 * @param <T> the entity type
 */
public interface IDialogEntity<T extends LivingEntity> {

    /**
     * @return the dialog group for the entity
     */
    DialogGroup getDialogGroup();

    /**
     * @return the dialog entity
     */
    T getDialogEntity();

}
