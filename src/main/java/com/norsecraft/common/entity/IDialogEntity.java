package com.norsecraft.common.entity;

import com.norsecraft.common.dialog.DialogGroup;
import net.minecraft.entity.LivingEntity;

public interface IDialogEntity<T extends LivingEntity> {

    DialogGroup getDialogGroup();

    T getDialogEntity();

}
