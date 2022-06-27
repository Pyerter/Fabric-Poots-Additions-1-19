package net.pyerter.pootsadditions.item.custom;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class EngineersRepairKit extends AbstractEngineersItem {
    public EngineersRepairKit(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        ItemStack oppositeHand = hand == Hand.MAIN_HAND ? user.getStackInHand(Hand.OFF_HAND) : user.getStackInHand(Hand.MAIN_HAND);

        if (oppositeHand.isEmpty() || oppositeHand.getDamage() == 0 || !(oppositeHand.getItem() instanceof AbstractEngineeredTool))
            return TypedActionResult.pass(stack);

        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 24;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack oppositeHand = user.getMainHandStack().isItemEqual(stack) ? user.getOffHandStack() : user.getMainHandStack();
        if (!oppositeHand.isEmpty() && oppositeHand.getDamage() > 0 && oppositeHand.getItem() instanceof AbstractEngineeredTool) {
            AbstractEngineeredTool tool = (AbstractEngineeredTool) oppositeHand.getItem();
            tool.tryRepairItem(oppositeHand);
            stack.decrement(1);
            return stack;
        }
        return stack;
    }

    @Override
    public ENGINEERS_CRAFT_TYPE getEngineersCraftType() {
        return ENGINEERS_CRAFT_TYPE.REPAIR;
    }
}
