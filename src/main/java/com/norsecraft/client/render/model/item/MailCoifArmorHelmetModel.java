package com.norsecraft.client.render.model.item;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.item.MailCoifArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MailCoifArmorHelmetModel extends AnimatedGeoModel<MailCoifArmorItem> {
    @Override
    public Identifier getModelLocation(MailCoifArmorItem object) {
        return NorseCraftMod.geoModel("mail_coif.geo.json");
    }

    @Override
    public Identifier getTextureLocation(MailCoifArmorItem object) {
        return NorseCraftMod.ncTex("item/mail_coif.png");
    }

    @Override
    public Identifier getAnimationFileLocation(MailCoifArmorItem animatable) {
        return null;
    }
}
