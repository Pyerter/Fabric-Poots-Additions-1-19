package net.pyerter.pootsadditions.item.custom.armor;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.item.ModArmorMaterials;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModArmorItem extends ArmorItem {
    private static final Map<ArmorMaterial, StatusEffectInstance> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, StatusEffectInstance>())
                    .put(ModArmorMaterials.TAILORED, new StatusEffectInstance(StatusEffects.LUCK, 400, 1)).build();

    public ModArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                if (hasArmorSetEquipped(player, getMaterial())) {
                    applyArmorEffects(player);
                }
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private void applyArmorEffects(PlayerEntity player) {
        for (Map.Entry<ArmorMaterial, StatusEffectInstance> entry: MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial material = entry.getKey();
            StatusEffectInstance effect = entry.getValue();

            applyArmorStatusEffect(player, material, effect);
        }
    }

    public static boolean applyArmorStatusEffect(PlayerEntity player, ArmorMaterial material, StatusEffectInstance effect) {
        boolean hasEffect = player.hasStatusEffect(effect.getEffectType());

        if (!hasEffect) {
            player.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), effect.getDuration(), effect.getAmplifier()));
            return true;
        }
        return false;
    }

    private boolean hasArmorSetEquipped(PlayerEntity player, ArmorMaterial targetMat) {
        List<ItemStack> armors = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            ItemStack stack = player.getInventory().getArmorStack(i);
            if (!stack.isEmpty())
                armors.add(stack);
        }

        if (armors.size() < 4)
            return false;

        boolean success = true;
        for (ItemStack armor: armors) {
            if (((ArmorItem)armor.getItem()).getMaterial() != targetMat) {
                success = false;
                break;
            }
        }
        return success;
    }
}
