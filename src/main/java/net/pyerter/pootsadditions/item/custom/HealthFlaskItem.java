package net.pyerter.pootsadditions.item.custom;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.pyerter.pootsadditions.item.ModItems;
import net.pyerter.pootsadditions.item.custom.engineering.IChargeable;
import net.pyerter.pootsadditions.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

public class HealthFlaskItem extends Item {
    public static final int MAX_CHARGES = 3;
    public static final String CHARGE_NBT_ID = "pootsadditions.charges";
    public static final float HEAL_AMOUNT = 20;

    public HealthFlaskItem(Settings settings) {
        super(settings);
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!world.isClient) {
            user.heal(HEAL_AMOUNT);
        }

        if (playerEntity != null) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!playerEntity.getAbilities().creativeMode) {
                addCharge(stack, -1);
            }
        }

        user.emitGameEvent(GameEvent.DRINK);
        return stack;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 24;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (getCharge(user.getStackInHand(hand)) > 0)
            return ItemUsage.consumeHeldItem(world, user, hand);

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    public boolean hasGlint(ItemStack stack) {
        return getCharge(stack) > 0;
    }

    public int addCharge(ItemStack stack, int charge) {
        return setCharge(stack, charge + getCharge(stack));
    }

    public int setCharge(ItemStack stack, int charge) {
        charge = Util.clamp(charge, 0, MAX_CHARGES);
        if (stack.hasNbt()) {
            stack.getNbt().putInt(CHARGE_NBT_ID, charge);
        } else {
            NbtCompound nbtData = new NbtCompound();
            nbtData.putInt(CHARGE_NBT_ID, charge);
            stack.setNbt(nbtData);
        }

        return charge;
    }

    public int getCharge(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(CHARGE_NBT_ID)) {
            return stack.getNbt().getInt(CHARGE_NBT_ID);
        } else {
            return setCharge(stack, 0);
        }
    }

    public float getOverridePredicateChargeValue(ItemStack stack) {
        switch (getCharge(stack)) {
            case 3: return 0.751f;
            case 2: return 0.501f;
            case 1: return 0.251f;
            default: case 0: return 0;
        }
    }

    public static class FlaskChargePredicateProvider implements UnclampedModelPredicateProvider {

        public BiFunction<ItemStack, HealthFlaskItem, Float> flaskChargeFunction;
        public FlaskChargePredicateProvider(BiFunction<ItemStack, HealthFlaskItem, Float> flaskChargeFunction) {
            this.flaskChargeFunction = flaskChargeFunction;
        }

        @Override
        public float call(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
            if (stack.getItem() instanceof HealthFlaskItem) {
                HealthFlaskItem healthFlaskItem = (HealthFlaskItem) stack.getItem();
                return flaskChargeFunction.apply(stack, healthFlaskItem);
            }
            return 0;
        }

        @Override
        public float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
            if (stack.getItem() instanceof HealthFlaskItem) {
                HealthFlaskItem healthFlaskItem = (HealthFlaskItem) stack.getItem();
                return flaskChargeFunction.apply(stack, healthFlaskItem);
            }
            return 0;
        }
    }
}
