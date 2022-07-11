package net.pyerter.pootsadditions.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.network.packet.ToggleHideArmorFullS2CPacket;
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

    @Inject(method="writeCustomDataToNbt", at=@At("RETURN"))
    void inWriteNbtIncludeHiddenArmorTracking(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean(IArmorHider.NBT_HEAD_VISIBLITY, this.isArmorHeadVisible());
        nbt.putBoolean(IArmorHider.NBT_CHEST_VISIBLITY, this.isArmorChestVisible());
        nbt.putBoolean(IArmorHider.NBT_LEGS_VISIBLITY, this.isArmorLegsVisible());
        nbt.putBoolean(IArmorHider.NBT_FEET_VISIBLITY, this.isArmorFeetVisible());
    }

    @Inject(method="readCustomDataFromNbt", at=@At("RETURN"))
    void inReadNbtIncludeHiddenArmorTracking(NbtCompound nbt, CallbackInfo info) {
        this.setArmorHeadVisible(nbt.getBoolean(IArmorHider.NBT_HEAD_VISIBLITY));
        this.setArmorChestVisible(nbt.getBoolean(IArmorHider.NBT_CHEST_VISIBLITY));
        this.setArmorLegsVisible(nbt.getBoolean(IArmorHider.NBT_LEGS_VISIBLITY));
        this.setArmorFeetVisible(nbt.getBoolean(IArmorHider.NBT_FEET_VISIBLITY));
    }

    public void syncPlayerArmorVisiblity() {
        if (!((Object) this instanceof ServerPlayerEntity))
            return;

        ServerPlayerEntity entity = (ServerPlayerEntity)(Object)this;

        IArmorHider armorHider = (IArmorHider)this;
        entity.networkHandler.sendPacket(
                new ToggleHideArmorFullS2CPacket(armorHider.isArmorHeadVisible(),
                        armorHider.isArmorChestVisible(),
                        armorHider.isArmorLegsVisible(),
                        armorHider.isArmorFeetVisible()));
    }

}
