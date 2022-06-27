package net.pyerter.pootsadditions.item.custom;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;

public class EngineersTrustyHammer extends AbstractEngineeredTool {

    public EngineersTrustyHammer(float attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, ToolMaterials.IRON, BlockTags.PICKAXE_MINEABLE, settings);
    }


}
