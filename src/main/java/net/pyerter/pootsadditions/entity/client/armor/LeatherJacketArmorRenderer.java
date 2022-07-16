package net.pyerter.pootsadditions.entity.client.armor;

import net.pyerter.pootsadditions.item.custom.armor.LeatherJacketArmorItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class LeatherJacketArmorRenderer extends GeoArmorRenderer<LeatherJacketArmorItem> {
    public LeatherJacketArmorRenderer() {
        super(new LeatherJacketArmorModel());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorRightLeg";
        this.leftLegBone = "armorLeftLeg";
        this.rightBootBone = "armorRightBoot";
        this.leftBootBone = "armorLeftBoot";
    }
}
