package com.norsecraft.client.render.item;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.render.model.item.MailCoifArmorHelmetModel;
import com.norsecraft.common.item.MailCoifArmorItem;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.GeoArmorRendererFactory;

public class MailCoifArmorRenderer extends GeoArmorRenderer<MailCoifArmorItem> {

    public MailCoifArmorRenderer(GeoArmorRendererFactory.Context ctx) {
        super(new MailCoifArmorHelmetModel(), ctx, new EntityModelLayer(NorseCraftMod.nc("mail_coif_armor"), "mail_coif_armor"));
        this.headBone = "armorHead";
    }
}
