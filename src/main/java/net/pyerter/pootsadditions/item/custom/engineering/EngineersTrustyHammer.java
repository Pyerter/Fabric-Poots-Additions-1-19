package net.pyerter.pootsadditions.item.custom.engineering;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.item.custom.engineering.AbstractEngineeredTool;
import net.pyerter.pootsadditions.util.Util;

public class EngineersTrustyHammer extends AbstractEngineeredTool {

    public EngineersTrustyHammer(float attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, ToolMaterials.IRON, Util.<TagKey<Block>>arrayListOf(BlockTags.PICKAXE_MINEABLE, BlockTags.SHOVEL_MINEABLE), settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (willNotBreak(stack) && isSuitableFor(state)) {
            tryBreakAdjacentBlocks(stack, world, state, pos, miner);
        }

        return super.postMine(stack, world, state, pos, miner);
    }

    private void tryBreakAdjacentBlocks(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        for (Direction dir: Direction.values()) {
            BlockPos adjacentPos = new BlockPos(pos.getX() + dir.getOffsetX(), pos.getY() + dir.getOffsetY(), pos.getZ() + dir.getOffsetZ());
            BlockState adjacentBlock = world.getBlockState(adjacentPos);
            if (isSuitableFor(adjacentBlock) && adjacentBlock.getBlock() instanceof OreBlock) {
                world.breakBlock(adjacentPos, true, miner);
            }
        }
    }
}
