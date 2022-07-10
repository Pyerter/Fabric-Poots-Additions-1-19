package net.pyerter.pootsadditions.item.custom.accessory;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.item.entity.client.ItemModelRenderer;

public interface AccessoryItem {

    public ItemModelRenderer.PlayerEquipStyle getEquipStyle();

    public boolean accessoryTick(World world, PlayerEntity entity, ItemStack stack, int slot, boolean selected);

}
