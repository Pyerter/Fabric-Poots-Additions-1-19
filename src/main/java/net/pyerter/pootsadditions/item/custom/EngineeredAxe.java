package net.pyerter.pootsadditions.item.custom;

import net.minecraft.block.Block;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;

public class EngineeredAxe extends AbstractEngineeredTool {
    public EngineeredAxe(float attackDamage, float attackSpeed, ToolMaterial material, Settings settings) {
        super(attackDamage, attackSpeed, material, BlockTags.AXE_MINEABLE, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return super.useAxeActionOnBlock(context);
    }
}
