package net.pyerter.pootsadditions.mixin;

import com.google.common.collect.Maps;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.item.StackDependentAttributeModifierItem;
import net.pyerter.pootsadditions.util.IModLivingEntityEquipmentRefresher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class ModLivingEntityMixin implements IModLivingEntityEquipmentRefresher {

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    private ItemStack getSyncedHandStack(EquipmentSlot slot) { return null; }
    @Shadow
    private ItemStack getSyncedArmorStack(EquipmentSlot slot) { return null; }

    @Shadow
    public AttributeContainer getAttributes() {
        return null;
    }

    @Shadow public abstract void equipStack(EquipmentSlot slot, ItemStack stack);

    @Override
    public boolean refreshEquipmentAttributeModifiers() {
        EquipmentSlot[] var2 = EquipmentSlot.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            EquipmentSlot equipmentSlot = var2[var4];
            ItemStack itemStack;
            switch (equipmentSlot.getType()) {
                case HAND:
                    itemStack = this.getSyncedHandStack(equipmentSlot);
                    break;
                case ARMOR:
                    itemStack = this.getSyncedArmorStack(equipmentSlot);
                    break;
                default:
                    continue;
            }

            ItemStack itemStack2 = this.getEquippedStack(equipmentSlot);
            if (!ItemStack.areEqual(itemStack2, itemStack)) {
                if (!itemStack.isEmpty() && itemStack.getItem() instanceof StackDependentAttributeModifierItem) {
                    StackDependentAttributeModifierItem attributeModifierItem = (StackDependentAttributeModifierItem) itemStack.getItem();
                    this.getAttributes().removeModifiers(attributeModifierItem.getAttributeModifiers(equipmentSlot, itemStack));
                    attributeModifierItem.setToRefreshAttributes(equipmentSlot, itemStack);
                }
                if (!itemStack2.isEmpty() && itemStack2.getItem() instanceof StackDependentAttributeModifierItem) {
                    StackDependentAttributeModifierItem attributeModifierItem = (StackDependentAttributeModifierItem) itemStack2.getItem();
                    this.getAttributes().removeModifiers(itemStack2.getAttributeModifiers(equipmentSlot));
                    this.getAttributes().addTemporaryModifiers(attributeModifierItem.getAttributeModifiers(equipmentSlot, itemStack2));
                }
            } else if (!itemStack2.isEmpty() && ItemStack.areEqual(itemStack2, itemStack) && itemStack2.getItem() instanceof StackDependentAttributeModifierItem) {
                StackDependentAttributeModifierItem attributeModifierItem = (StackDependentAttributeModifierItem) itemStack2.getItem();
                if (attributeModifierItem.getNeedsAttributeRefresh(equipmentSlot, itemStack2)) {
                    this.getAttributes().removeModifiers(itemStack2.getAttributeModifiers(equipmentSlot));
                    this.getAttributes().addTemporaryModifiers(attributeModifierItem.getAttributeModifiers(equipmentSlot, itemStack2));
                }
            }
        }
        return false;
    }

    @Inject(method="getEquipmentChanges", at=@At("RETURN"))
    public void onReturnGetEquipmentChanges(CallbackInfoReturnable info) {
        ((IModLivingEntityEquipmentRefresher)this).refreshEquipmentAttributeModifiers();
    }

}
