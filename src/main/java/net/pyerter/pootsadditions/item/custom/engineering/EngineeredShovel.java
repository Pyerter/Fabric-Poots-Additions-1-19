package net.pyerter.pootsadditions.item.custom.engineering;

import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.pyerter.pootsadditions.item.custom.engineering.AbstractEngineeredTool;

public class EngineeredShovel extends AbstractEngineeredTool {

    public EngineeredShovel(float attackDamage, float attackSpeed, ToolMaterial material, Settings settings) {
        super(attackDamage, attackSpeed, material, BlockTags.SHOVEL_MINEABLE, settings);

        toolType = ToolType.SHOVEL;
        super.registerTool(this, material, ToolType.SHOVEL);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (anitcipatedUseOnBlockResult(context) == ActionResult.FAIL)
            return ActionResult.PASS;

        return super.useShovelActionOnBlock(context);
    }
}
