package net.pyerter.pootsadditions.mixin;

import net.minecraft.item.Item;
import net.pyerter.pootsadditions.util.IItemSpecialRecipeRemainder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ModItemSpecialRecipeMixin {

    @Inject(method="getRecipeRemainder", at=@At("HEAD"), cancellable = true)
    public void onGetRecipeRemainder(CallbackInfoReturnable<Item> info) {
        if (this instanceof IItemSpecialRecipeRemainder) {
            info.setReturnValue(((IItemSpecialRecipeRemainder) this).getSpecialRecipeRemainder());
        }
    }
}
