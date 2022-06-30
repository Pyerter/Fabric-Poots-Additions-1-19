package net.pyerter.pootsadditions.item.custom;

import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.pyerter.pootsadditions.item.custom.engineering.AbstractEngineeredTool;

public class EngineeredPickaxe extends AbstractEngineeredTool {

    public EngineeredPickaxe(float attackDamage, float attackSpeed, ToolMaterial material, Settings settings) {
        super(attackDamage, attackSpeed, material, BlockTags.PICKAXE_MINEABLE, settings);

        super.registerTool(this, material, ToolType.PICKAXE);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return super.useOnBlock(context);
    }
}
