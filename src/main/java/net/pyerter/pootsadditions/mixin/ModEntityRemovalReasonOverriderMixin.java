package net.pyerter.pootsadditions.mixin;

import net.minecraft.entity.Entity;
import net.pyerter.pootsadditions.util.IIsRemovedOverrider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ModEntityRemovalReasonOverriderMixin {

    @Inject(method="isRemoved", at=@At("HEAD"), cancellable = true)
    public void onIsRemoved(CallbackInfoReturnable info) {
        if (this instanceof IIsRemovedOverrider) {
            IIsRemovedOverrider overrider = (IIsRemovedOverrider) this;
            info.setReturnValue(overrider.isRemovedSpecial());
        }
    }

}
