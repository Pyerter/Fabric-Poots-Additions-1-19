package net.pyerter.pootsadditions.item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.BlockTags;
import net.pyerter.pootsadditions.item.ModToolMaterials;
import net.pyerter.pootsadditions.item.custom.engineering.AbstractEngineeredTool;
import net.pyerter.pootsadditions.util.IModPlayerEntityWeaponAbilityTriggerer;

public class MaliceScythe extends AbstractEngineeredTool {
    public MaliceScythe(float attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, ModToolMaterials.STARMETAL, BlockTags.HOE_MINEABLE, settings);
    }

    @Override
    public boolean tryUseWeaponAbility(Entity target, PlayerEntity attacker) {
        return target instanceof LivingEntity ? ((IModPlayerEntityWeaponAbilityTriggerer)attacker).trySwordSweepAttack(target, 2) : false;
    }
}
