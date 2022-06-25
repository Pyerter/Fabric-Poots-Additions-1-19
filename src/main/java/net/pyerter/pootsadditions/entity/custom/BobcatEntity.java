package net.pyerter.pootsadditions.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.entity.ModEntities;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

/*
 * This class represents a Bobcat. A lot of the code is taken from the minecraft:CatEntity class.
 * TODO: createChild()
 */
public class BobcatEntity extends TameableEntity implements IAnimatable {

    private AnimationFactory factory = new AnimationFactory(this);
    @Nullable
    private net.minecraft.entity.ai.goal.TemptGoal temptGoal;

    public BobcatEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4f);
    }

    @Override
    protected void initGoals() {
        this.temptGoal = new BobcatEntity.TemptGoal(this, 0.6, TAMING_INGREDIENT, true);
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new SitGoal(this));
        this.goalSelector.add(3, this.temptGoal);
        //this.goalSelector.add(5, new GoToBedAndSleepGoal(this, 1.1, 8));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0F, 5.0F, false));
        // this.goalSelector.add(7, new CatSitOnBlockGoal(this, 0.8));
        this.goalSelector.add(8, new PounceAtTargetGoal(this, 0.3F));
        this.goalSelector.add(9, new AttackGoal(this));
        this.goalSelector.add(10, new AnimalMateGoal(this, 0.8));
        this.goalSelector.add(11, new WanderAroundFarGoal(this, 0.8, 1.0000001E-5F));
        this.goalSelector.add(12, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));
        this.targetSelector.add(1, new UntamedActiveTargetGoal(this, RabbitEntity.class, false, (Predicate)null));
        this.targetSelector.add(1, new UntamedActiveTargetGoal(this, TurtleEntity.class, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
        this.targetSelector.add(3, (new RevengeGoal(this, new Class[0])));
    }

    // Custom Animations Method
    private <E extends IAnimatable>PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bobcat.walk"));
            return PlayState.CONTINUE;
        }

        if (isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bobcat.sit"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bobcat.idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isTamed()) {
            if (this.isInLove()) {
                return SoundEvents.ENTITY_CAT_PURR;
            } else {
                return this.random.nextInt(4) == 0 ? SoundEvents.ENTITY_CAT_PURREOW : SoundEvents.ENTITY_CAT_AMBIENT;
            }
        } else {
            return SoundEvents.ENTITY_CAT_STRAY_AMBIENT;
        }
    }

    public int getMinAmbientSoundDelay() {
        return 120;
    }

    public void hiss() {
        this.playSound(SoundEvents.ENTITY_CAT_HISS, this.getSoundVolume(), this.getSoundPitch());
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_CAT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CAT_DEATH;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    protected void eat(PlayerEntity player, Hand hand, ItemStack stack) {
        if (this.isBreedingItem(stack)) {
            this.playSound(SoundEvents.ENTITY_CAT_EAT, 1.0F, 1.0F);
        }

        super.eat(player, hand, stack);
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }

    public boolean tryAttack(Entity target) {
        return target.damage(DamageSource.mob(this), this.getAttackDamage());
    }



    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }

    public boolean canImmediatelyDespawn(double distanceSquared) {
        return !this.isTamed() && this.age > 2400;
    }

    @Override
    protected void onTamedChanged() {
        // nada
    }

    static class TemptGoal extends net.minecraft.entity.ai.goal.TemptGoal {
        @Nullable
        private PlayerEntity player;
        private final BobcatEntity cat;

        public TemptGoal(BobcatEntity cat, double speed, Ingredient food, boolean canBeScared) {
            super(cat, speed, food, canBeScared);
            this.cat = cat;
        }

        public void tick() {
            super.tick();
            if (this.player == null && this.mob.getRandom().nextInt(this.getTickCount(600)) == 0) {
                this.player = this.closestPlayer;
            } else if (this.mob.getRandom().nextInt(this.getTickCount(500)) == 0) {
                this.player = null;
            }

        }

        protected boolean canBeScared() {
            return this.player != null && this.player.equals(this.closestPlayer) ? false : super.canBeScared();
        }

        public boolean canStart() {
            return super.canStart() && !this.cat.isTamed();
        }
    }

    /* MAKE ENTITY TAMABLE */
    private static final TrackedData<Boolean> SITTING =
            DataTracker.registerData(BobcatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<BlockPos> RESPAWN_POS =
            DataTracker.registerData(BobcatEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final Ingredient TAMING_INGREDIENT = Ingredient.ofItems(new ItemConvertible[]{Items.COD, Items.SALMON});
    public boolean isBreedingItem(ItemStack stack) {
        return TAMING_INGREDIENT.test(stack);
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        if (this.world.isClient) {
            if (this.isTamed() && this.isOwner(player)) {
                return ActionResult.SUCCESS;
            } else {
                return !this.isBreedingItem(itemStack) || !(this.getHealth() < this.getMaxHealth()) && this.isTamed() ? ActionResult.PASS : ActionResult.SUCCESS;
            }
        } else {
            ActionResult actionResult;
            if (this.isTamed()) {
                if (this.isOwner(player)) {
                    if (!(item instanceof DyeItem)) {
                        if (item.isFood() && this.isBreedingItem(itemStack) && this.getHealth() < this.getMaxHealth()) {
                            this.eat(player, hand, itemStack);
                            this.heal((float)item.getFoodComponent().getHunger());
                            return ActionResult.CONSUME;
                        }

                        actionResult = super.interactMob(player, hand);
                        if (!actionResult.isAccepted() || this.isBaby()) {
                            this.setSitting(!this.isSitting());
                        }

                        return actionResult;
                    }

                    // Removed dye
                }
            } else if (this.isBreedingItem(itemStack)) {
                this.eat(player, hand, itemStack);
                if (this.random.nextInt(3) == 0) {
                    this.setOwner(player);
                    this.setSitting(true);
                    this.world.sendEntityStatus(this, (byte)7);
                } else {
                    this.world.sendEntityStatus(this, (byte)6);
                }

                this.setPersistent();
                return ActionResult.CONSUME;
            }

            actionResult = super.interactMob(player, hand);
            if (actionResult.isAccepted()) {
                this.setPersistent();
            }

            return actionResult;
        }
    }

    public void setSitting(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
        super.setSitting(sitting);
    }

    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    public BlockPos setRespawnPos(BlockPos blockPos) {
        this.dataTracker.set(RESPAWN_POS, blockPos);
        return blockPos;
    }

    public BlockPos getRespawnPos() {
        BlockPos respawn = this.dataTracker.get(RESPAWN_POS);
        if (respawn.equals(BlockPos.ORIGIN)) {
            respawn = this.getBlockPos();
            setRespawnPos(respawn);
        }
        return respawn;
    }

    public BlockPos getRespawnPos(ServerWorld world) {
        List<ServerPlayerEntity> serverEntityOwner = world.getPlayers().stream().filter(e -> e.getGameProfile().getId().equals(getOwnerUuid())).toList();
        return setRespawnPos(serverEntityOwner.size() == 1 ? serverEntityOwner.get(0).getSpawnPointPosition() : getRespawnPos());
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(60);
            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(12);
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.6f);
        } else {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(20);
            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(8);
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4f);
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SITTING, false);
        this.dataTracker.startTracking(RESPAWN_POS, BlockPos.ORIGIN);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("isSitting", this.dataTracker.get(SITTING));

        BlockPos respawn = getRespawnPos();
        nbt.putLong("respawn_x", respawn.getX());
        nbt.putLong("respawn_y", respawn.getY());
        nbt.putLong("respawn_z", respawn.getZ());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        this.dataTracker.set(SITTING, nbt.getBoolean("isSitting"));

        BlockPos respawn = new BlockPos(nbt.getLong("respawn_x"), nbt.getLong("respawn_y"), nbt.getLong("respawn_z"));
        setRespawnPos(respawn);
    }

    @Override
    public AbstractTeam getScoreboardTeam() {
        return super.getScoreboardTeam();
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return this.isOwner(player);
    }

    /* Baby Bobcats! */
    public BobcatEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        BobcatEntity catEntity = ModEntities.BOBCAT.create(serverWorld);
        if (passiveEntity instanceof CatEntity) {
            if (this.isTamed()) {
                catEntity.setOwnerUuid(this.getOwnerUuid());
                catEntity.setTamed(true);
            }
        }

        return catEntity;
    }

    public boolean canBreedWith(AnimalEntity other) {
        if (!this.isTamed()) {
            return false;
        } else if (!(other instanceof BobcatEntity)) {
            return false;
        } else {
            BobcatEntity catEntity = (BobcatEntity)other;
            return catEntity.isTamed() && super.canBreedWith(other);
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
    }

    /* If the pet is tamed, respawn it back at the player's spawnpoint! */
    @Override
    protected void updatePostDeath() {
        if (isTamed()) {
            ++this.deathTime;
            if (this.deathTime == 20) {
                if (!this.world.isClient()) {
                    BlockPos respawnPos = getRespawnPos((ServerWorld)this.world);
                    if (respawnPos != null) {
                        refreshPositionAndAngles(respawnPos, 0, 0);
                        this.world.sendEntityStatus(this, (byte) 42);
                    }
                }
                this.setHealth((float) getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getBaseValue());
                this.dead = false;
                this.deathTime = 0;
                this.setPose(EntityPose.STANDING);
            }
        } else {
            super.updatePostDeath();
        }
    }

    @Override
    public void handleStatus(byte status) {
        if (status == (byte)42) {
            this.setPose(EntityPose.STANDING);
        }
         else {
             super.handleStatus(status);
        }
    }
}
