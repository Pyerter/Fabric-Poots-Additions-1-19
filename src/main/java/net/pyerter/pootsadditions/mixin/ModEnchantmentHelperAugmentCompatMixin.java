package net.pyerter.pootsadditions.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import net.pyerter.pootsadditions.item.custom.engineering.AugmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class ModEnchantmentHelperAugmentCompatMixin {

    // static methods don't work with mixin ;-;
    /*
    @Inject(method="getAttackDamage", at=@At(value="RETURN"), cancellable = true)
    public void onCallGetAttackDamageAugmentAddition(ItemStack stack, EntityGroup group, CallbackInfoReturnable info) {
        float extraDamage = 0;
        extraDamage += AugmentHelper.getAugmentDamage(stack);
        if (extraDamage > 0)
            info.setReturnValue(extraDamage);
    }*/

}
