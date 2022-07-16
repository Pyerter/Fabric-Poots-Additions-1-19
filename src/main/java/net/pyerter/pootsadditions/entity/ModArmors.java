package net.pyerter.pootsadditions.entity;

import net.pyerter.pootsadditions.entity.client.armor.LeatherJacketArmorRenderer;
import net.pyerter.pootsadditions.item.ModItems;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class ModArmors {

    public static void registerArmorRenderers() {
        GeoArmorRenderer.registerArmorRenderer(new LeatherJacketArmorRenderer(), ModItems.LEATHER_JACKET_ARMOR_ITEM);
    }

}
