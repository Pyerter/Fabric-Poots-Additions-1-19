package net.pyerter.pootsadditions.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import net.pyerter.pootsadditions.block.ModBlockTags;
import net.pyerter.pootsadditions.item.ModToolMaterials;

public class StarmetalAxe extends AxeItem {
    public StarmetalAxe(float attackDamage, float attackSpeed, Settings settings) {
        super(ModToolMaterials.STARMETAL, attackDamage, attackSpeed, settings);
    }
}
