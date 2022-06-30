package net.pyerter.pootsadditions.item.custom;

import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.pyerter.pootsadditions.item.custom.engineering.AbstractEngineeredTool;

public class EngineeredAxe extends AbstractEngineeredTool {
    public EngineeredAxe(float attackDamage, float attackSpeed, ToolMaterial material, Settings settings) {
        super(attackDamage, attackSpeed, material, BlockTags.AXE_MINEABLE, settings);

        super.registerTool(this, material, ToolType.AXE);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (anitcipatedUseOnBlockResult(context) == ActionResult.FAIL)
            return ActionResult.PASS;

        return super.useAxeActionOnBlock(context);
    }
}
