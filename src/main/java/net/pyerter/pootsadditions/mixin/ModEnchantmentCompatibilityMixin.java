package net.pyerter.pootsadditions.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.pyerter.pootsadditions.enchantments.ModEnchantmentTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Enchantment.class)
public class ModEnchantmentCompatibilityMixin {

    @Shadow
    EnchantmentTarget type;

    @Inject(method="isAcceptableItem", at=@At(value="HEAD"), cancellable = true)
    public void onIsAcceptableItemCallback(ItemStack stack, CallbackInfoReturnable info) {
        Optional<ModEnchantmentTarget> modEnchantType = ModEnchantmentTarget.vanillaToModTarget(type);
        if (modEnchantType.isPresent() && modEnchantType.get().isAcceptableItem(stack.getItem())) {
            info.setReturnValue(true);
        }
    }

}
