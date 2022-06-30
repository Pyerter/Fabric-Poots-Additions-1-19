package net.pyerter.pootsadditions.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.item.SpecialWeaponWithAbility;
import net.pyerter.pootsadditions.util.IModPlayerEntityWeaponAbilityTriggerer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.Iterator;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class ModPlayerEntityMixin extends LivingEntity implements IModPlayerEntityWeaponAbilityTriggerer {
    protected ModPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public void spawnSweepAttackParticles() {}

    public boolean tryUseWeaponAbility(Entity target) {
        ItemStack weapon = this.getMainHandStack();
        if (weapon.getItem() instanceof SpecialWeaponWithAbility) {
            SpecialWeaponWithAbility abilityWeapon = (SpecialWeaponWithAbility) weapon.getItem();
            abilityWeapon.tryUseWeaponAbility(target, (PlayerEntity)(Object)this);
        }
        return true;
    }

    public boolean trySwordSweepAttack(Entity target, int radius) {
        float l = 1.0F + EnchantmentHelper.getSweepingMultiplier(this);
        List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, target.getBoundingBox().expand(radius, 0.25, radius));
        Iterator var19 = list.iterator();

        label166:
        while(true) {
            LivingEntity livingEntity;
            do {
                do {
                    do {
                        do {
                            if (!var19.hasNext()) {
                                this.world.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
                                this.spawnSweepAttackParticles();
                                break label166;
                            }

                            livingEntity = (LivingEntity)var19.next();
                        } while(livingEntity == this);
                    } while(livingEntity == target);
                } while(this.isTeammate(livingEntity));
            } while(livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity)livingEntity).isMarker());

            if (this.squaredDistanceTo(livingEntity) < 9.0 * radius * radius) {
                livingEntity.takeKnockback(0.4000000059604645, (double) MathHelper.sin(this.getYaw() * 0.017453292F), (double)(-MathHelper.cos(this.getYaw() * 0.017453292F)));
                livingEntity.damage(DamageSource.player((PlayerEntity)(Object)this), l);
            }
        }
        return true;
    }

    @Override
    public void onAttacking(Entity target) {
        tryUseWeaponAbility(target);
        super.onAttacking(target);
    }
}
