package net.pyerter.pootsadditions.item.custom;

import net.minecraft.item.AxeItem;
import net.pyerter.pootsadditions.item.ModToolMaterials;

public class StarmetalAxe extends AxeItem {
    public StarmetalAxe(float attackDamage, float attackSpeed, Settings settings) {
        super(ModToolMaterials.STARMETAL, attackDamage, attackSpeed, settings);
    }
}
