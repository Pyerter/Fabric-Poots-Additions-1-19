package net.pyerter.pootsadditions.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;

public class EngineeredWeaponDamageReverterEnchantment extends Enchantment {
    protected EngineeredWeaponDamageReverterEnchantment(EnchantmentTarget type, EquipmentSlot ... slotTypes) {
        super(Rarity.COMMON, type, slotTypes);
    }

    @Override
    public float getAttackDamage(int level, EntityGroup group) {

        return super.getAttackDamage(level, group);
    }
}
