package net.pyerter.pootsadditions.item.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class DiamondSwordEntity extends ItemEntity {
    public DiamondSwordEntity(EntityType<? extends ItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        if (this.getStack().isEmpty()) {
            this.discard();
        } else {
            super.tick();
        }
    }
}
