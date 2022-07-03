package net.pyerter.pootsadditions.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.util.IArmorHider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class ModPlayerEntityArmorHiddenTrackerMixin extends LivingEntity implements IArmorHider {

    protected ModPlayerEntityArmorHiddenTrackerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method="initDataTracker", at=@At("RETURN"))
    void onInitDataTracker(CallbackInfo info) {
        this.dataTracker.startTracking(ARMOR_VISIBLE_CHEST, true);
        this.dataTracker.startTracking(ARMOR_VISIBLE_LEGS, true);
        this.dataTracker.startTracking(ARMOR_VISIBLE_FEET, true);
        this.dataTracker.startTracking(ARMOR_VISIBLE_HEAD, true);
    }
}
