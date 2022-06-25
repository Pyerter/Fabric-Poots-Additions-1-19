package net.pyerter.pootsadditions.item.custom;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.pyerter.pootsadditions.item.ModToolMaterials;

public class StarmetalSword extends SwordItem {
    public StarmetalSword(int attackDamage, float attackSpeed, Settings settings) {
        super(ModToolMaterials.STARMETAL, attackDamage, attackSpeed, settings);
    }
}
