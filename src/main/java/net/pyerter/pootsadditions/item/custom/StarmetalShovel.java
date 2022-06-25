package net.pyerter.pootsadditions.item.custom;

import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.pyerter.pootsadditions.item.ModToolMaterials;

public class StarmetalShovel extends ShovelItem {
    public StarmetalShovel(float attackDamage, float attackSpeed, Settings settings) {
        super(ModToolMaterials.STARMETAL, attackDamage, attackSpeed, settings);
    }
}
