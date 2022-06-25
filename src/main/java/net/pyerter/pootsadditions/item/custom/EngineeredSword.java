package net.pyerter.pootsadditions.item.custom;

import net.minecraft.block.Block;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.pyerter.pootsadditions.block.ModBlockTags;

public class EngineeredSword extends AbstractEngineeredTool {
    public EngineeredSword(float attackDamage, float attackSpeed, ToolMaterial material, Settings settings) {
        super(attackDamage, attackSpeed, material, ModBlockTags.SWORD_MINEABLE, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return super.useOnBlock(context);
    }
}
