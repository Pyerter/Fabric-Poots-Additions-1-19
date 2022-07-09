package net.pyerter.pootsadditions.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.util.math.random.Random;
import net.pyerter.pootsadditions.enchantments.ModEnchantmentHelper;
import net.pyerter.pootsadditions.enchantments.ModEnchantmentTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentScreenHandler.class)
public class ModEnchantmentScreenHandlerModEnchantCompatibilityMixin {

    @Shadow
    Random random;
    @Shadow
    Property seed;

    // This code is basically a copy of what already exists in the EnchantmentScreenHandler
    @Inject(method="generateEnchantments", at=@At("HEAD"), cancellable = true)
    private void onGenerateEnchantmentsTryModCompat(ItemStack stack, int slot, int level, CallbackInfoReturnable info) {
        if (!ModEnchantmentTarget.stackIsCompatibleWithModEnchantment(stack))
            return;

        this.random.setSeed((long)(this.seed.get() + slot));
        List<EnchantmentLevelEntry> list = ModEnchantmentHelper.generateEnchantments(this.random, stack, level, false);
        if (stack.isOf(Items.BOOK) && list.size() > 1) {
            list.remove(this.random.nextInt(list.size()));
        }

        info.setReturnValue(list);
    }

}
