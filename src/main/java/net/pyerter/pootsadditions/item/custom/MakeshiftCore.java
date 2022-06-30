package net.pyerter.pootsadditions.item.custom;

import com.mojang.datafixers.kinds.IdF;
import com.sun.jna.platform.win32.WinDef;
import net.minecraft.block.Block;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.block.entity.CaptureChamberEntity;
import net.pyerter.pootsadditions.item.Chargeable;
import net.pyerter.pootsadditions.util.Util;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.Utilities;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class MakeshiftCore extends Item {

    public static final String CHARGE_NBT_ID = "pootsadditions.core_charge";
    public static final int MAX_CHARGE = 1210;


    public MakeshiftCore(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        MakeshiftCore.addCharge(user.getStackInHand(hand), 15);

        return super.use(world, user, hand);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.hasNbt();
    }

    public static int addCharge(ItemStack stack, int charge) {
        return setCharge(stack, charge + MakeshiftCore.getCharge(stack));
    }

    public static int setCharge(ItemStack stack, int charge) {
        charge = Util.clamp(charge, 0, MAX_CHARGE);
        if (stack.hasNbt()) {
            stack.getNbt().putInt(CHARGE_NBT_ID, charge);
        } else {
            NbtCompound nbtData = new NbtCompound();
            nbtData.putInt(CHARGE_NBT_ID, charge);
            stack.setNbt(nbtData);
        }

        return charge;
    }

    public static int getCharge(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(CHARGE_NBT_ID)) {
            return stack.getNbt().getInt("pootsadditions.core_charge");
        } else {
            return setCharge(stack, 0);
        }
    }

    public static int tryTransferCharge(ItemStack stack, ItemStack targetStack) {
        if (!targetStack.isEmpty() && targetStack.getItem() instanceof Chargeable) {
            Chargeable chargeable = (Chargeable) targetStack.getItem();
            int availablePower = Util.clamp(getCharge(stack), 0, CaptureChamberEntity.MAX_CHARGE_TRANSFER_RATE);
            int transferedPower = chargeable.tryCharge(targetStack, availablePower);
            addCharge(stack, -transferedPower);
            return transferedPower;
        }
        return 0;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isClient() && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            ItemStack oppositeStack = player.getMainHandStack().isItemEqual(stack) ? player.getMainHandStack() : player.getOffHandStack();
            tryTransferCharge(stack, oppositeStack);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(MutableText.of(new LiteralTextContent("Core Charge: " + getCharge(stack))).formatted(Formatting.AQUA));
        tooltip.add(MutableText.of(new LiteralTextContent("\"1.21 Gigawatts!\"")).formatted(Formatting.ITALIC));
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return super.getTooltipData(stack);
    }

    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    public int getItemBarStep(ItemStack stack) {
        return Math.round((float)MakeshiftCore.getCharge(stack) * 13.0F / (float)MakeshiftCore.MAX_CHARGE);
    }

    public int getItemBarColor(ItemStack stack) {
        float f = Math.max(0.0F, (float)MakeshiftCore.getCharge(stack) / (float)MAX_CHARGE);
        return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    public static float getOverridePredicateChargeValue(ItemStack stack) {
        // 0, 0.25, 0.5, and 0.75 are the thresholds
        // 0 is empty
        // 0.25 is less than 1/3 full charge
        // 0.5 is greater than 0.25, less than 2/3 full charge
        // 0.75 is greater than 0.5
        double filledPercent = (float)MakeshiftCore.getCharge(stack) / (float)MAX_CHARGE;
        if (filledPercent > 0.66)
            return 0.751f;
        if (filledPercent > 0.33)
            return 0.501f;
        if (filledPercent > 0)
            return 0.251f;
        return 0;
    }

    public static class CoreChargePredicateProvider implements UnclampedModelPredicateProvider {

        public Function<ItemStack, Float> coreChargeFunction;
        public CoreChargePredicateProvider(Function<ItemStack, Float> coreChargeFunction) {
            this.coreChargeFunction = coreChargeFunction;
        }

        @Override
        public float call(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
            return coreChargeFunction.apply(stack);
        }

        @Override
        public float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
            return coreChargeFunction.apply(stack);
        }
    }
}
