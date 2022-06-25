package net.pyerter.pootsadditions.item.custom;

import net.minecraft.item.HoeItem;
import net.pyerter.pootsadditions.item.ModToolMaterials;

public class StarmetalHoe extends HoeItem {
    public StarmetalHoe(int attackDamage, float attackSpeed, Settings settings) {
        super(ModToolMaterials.STARMETAL, attackDamage, attackSpeed, settings);
    }
}
