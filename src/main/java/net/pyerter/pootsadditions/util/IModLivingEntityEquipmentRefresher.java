package net.pyerter.pootsadditions.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Shadow;

public interface IModLivingEntityEquipmentRefresher {
    public boolean refreshEquipmentAttributeModifiers();
}
