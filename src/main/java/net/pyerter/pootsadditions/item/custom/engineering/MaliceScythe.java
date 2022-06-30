package net.pyerter.pootsadditions.item.custom.engineering;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.item.ModToolMaterials;
import net.pyerter.pootsadditions.item.custom.engineering.AbstractEngineeredTool;
import net.pyerter.pootsadditions.util.IModPlayerEntityWeaponAbilityTriggerer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MaliceScythe extends AbstractEngineeredTool implements IChargeable {
    public MaliceScythe(float attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, ModToolMaterials.STARMETAL, BlockTags.HOE_MINEABLE, settings);
    }

    @Override
    public String getChargeNbtID() {
        return "pootsadditions.charge";
    }

    @Override
    public boolean tryUseWeaponAbility(Entity target, PlayerEntity attacker) {
        return target instanceof LivingEntity ? ((IModPlayerEntityWeaponAbilityTriggerer)attacker).trySwordSweepAttack(target, 2) : false;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return getCharge(stack) > 0;
    }

    @Override
    public int getMaxCharge() {
        return 100;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        addChargeTooltip(stack, tooltip);
    }
}
