package net.pyerter.pootsadditions.item.custom;

import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.pyerter.pootsadditions.item.ModToolMaterials;

public class StarmetalPickaxe extends PickaxeItem {
    public StarmetalPickaxe(int attackDamage, float attackSpeed, Settings settings) {
        super(ModToolMaterials.STARMETAL, attackDamage, attackSpeed, settings);
    }
}
