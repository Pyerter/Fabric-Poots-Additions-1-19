package net.pyerter.pootsadditions.item.custom.engineering;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SimpleAugment extends Augment {

    protected final int augmentMask;
    protected final String augmentID;

    protected float attackDamage = 0;
    protected float attackSpeed = 0;
    protected float miningSpeedMult = 1;
    protected PostMinePredicate postMinePredicate = null;

    protected SimpleAugment(Settings settings, String augmentID, int augmentMask) {
        super(settings);
        this.augmentID = augmentID;
        this.augmentMask = augmentMask;
    }

    protected SimpleAugment(Settings settings, String augmentID, int augmentMask, float attackDamage, float attackSpeed, float miningSpeedMult, PostMinePredicate postMinePredicate) {
        super(settings);
        this.augmentID = augmentID;
        this.augmentMask = augmentMask;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.miningSpeedMult = miningSpeedMult;
        this.postMinePredicate = postMinePredicate;
    }

    @Override
    public String getAugmentID() {
        return augmentID;
    }

    @Override
    public int getAugmentMask() {
        return augmentMask;
    }

    public float onGetAttackDamage(ItemStack stack, AbstractEngineeredTool tool) {
        return attackDamage;
    }

    public float getAugmentAttackDamage() {
        return attackDamage;
    }

    public float onGetMiningSpeedMultiplier(ItemStack stack, AbstractEngineeredTool tool) {
        return miningSpeedMult;
    }

    public float getAugmentMiningSpeedMultiplier() {
        return miningSpeedMult;
    }

    public float getAugmentAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public boolean onPostMine(ItemStack stack, AbstractEngineeredTool tool, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        return postMinePredicate != null ? postMinePredicate.onPostMine(stack, tool, world, state, pos, miner) : false;
    }

    public static class Builder {

        protected Settings settings;
        protected String augmentID;

        protected int augmentMask = 0;

        protected float attackDamage = 0;
        protected float attackSpeed = 0;
        protected float miningSpeedMult = 1;
        PostMinePredicate minePredicate = null;

        Builder(Settings settings, String augmentID) {
            this.settings = settings;
            this.augmentID = augmentID;
        }

        Builder addAttackDamage(float attackDamage) {
            if ((augmentMask & Augment.MASK_GET_ATTACK_DAMAGE) == 0)
                augmentMask += Augment.MASK_GET_ATTACK_DAMAGE;
            this.attackDamage += attackSpeed;
            return this;
        }

        Builder addAttackSpeed(float attackSpeed) {
            if ((augmentMask & Augment.MASK_GET_ATTACK_SPEED) == 0)
                augmentMask += Augment.MASK_GET_ATTACK_SPEED;
            this.attackSpeed += attackSpeed;
            return this;
        }

        Builder addMiningSpeedMult(float mult) {
            if ((augmentMask & Augment.MASK_GET_MINING_SPEED) == 0)
                augmentMask += Augment.MASK_GET_MINING_SPEED;
            this.miningSpeedMult += mult;
            return this;
        }

        Builder setMinePredicate(PostMinePredicate predicate) {
            if ((augmentMask & Augment.MASK_POST_MINE) == 0)
                augmentMask += Augment.MASK_POST_MINE;
            this.minePredicate = predicate;
            return this;
        }

        SimpleAugment build() {
            return new SimpleAugment(settings, augmentID, augmentMask, attackDamage, attackSpeed, miningSpeedMult, minePredicate);
        }

    }

    public interface PostMinePredicate {
        boolean onPostMine(ItemStack stack, AbstractEngineeredTool tool, World world, BlockState state, BlockPos pos, LivingEntity miner);
    }
}
