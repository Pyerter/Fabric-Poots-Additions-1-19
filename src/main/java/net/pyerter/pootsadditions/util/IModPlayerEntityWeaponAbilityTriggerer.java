package net.pyerter.pootsadditions.util;

import net.minecraft.entity.Entity;

public interface IModPlayerEntityWeaponAbilityTriggerer {
    public boolean tryUseWeaponAbility(Entity target);
    public default boolean trySwordSweepAttack(Entity target) {
        return trySwordSweepAttack(target, 1);
    }
    public boolean trySwordSweepAttack(Entity target, int radiusMultiplier);
}
