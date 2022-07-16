package net.pyerter.pootsadditions.entity.client.armor;

import net.minecraft.util.Identifier;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.item.custom.armor.LeatherJacketArmorItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LeatherJacketArmorModel extends AnimatedGeoModel<LeatherJacketArmorItem> {
    @Override
    public Identifier getModelResource(LeatherJacketArmorItem object) {
        return new Identifier(PootsAdditions.MOD_ID, "geo/leather_jacket_armor.geo.json");
    }

    @Override
    public Identifier getTextureResource(LeatherJacketArmorItem object) {
        return new Identifier(PootsAdditions.MOD_ID, "textures/models/armor/leather_jacket_armor.png");
    }

    @Override
    public Identifier getAnimationResource(LeatherJacketArmorItem animatable) {
        return new Identifier(PootsAdditions.MOD_ID, "animations/armor_animations.json");
    }
}
