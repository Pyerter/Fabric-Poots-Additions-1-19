package net.pyerter.pootsadditions.item.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.util.IIsRemovedOverrider;

public class WearableItemEntity extends ItemEntity implements IIsRemovedOverrider {
    public WearableItemEntity(EntityType<? extends ItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        if (world.getPlayers().size()  > 0 && !this.hasVehicle()) {
            this.startRiding(world.getPlayers().get(0));
        }
        if (this.hasVehicle()) {
            if (this.getVehicle() instanceof PlayerEntity) {
                PlayerEntity wearer = (PlayerEntity) this.getVehicle();
                this.setRotation(wearer.getBodyYaw(), this.getVehicle().getPitch());
            }
        }
    }

    @Override
    public boolean isRemovedSpecial() {
        return false;
    }
}
