package net.pyerter.pootsadditions.mixin;

import net.minecraft.client.Mouse;
import net.pyerter.pootsadditions.util.ICursorController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mouse.class)
public abstract class ModMouseCursorControllerMixin implements ICursorController {

    @Shadow
    double x;
    @Shadow
    double y;

    @Override
    public void setCursorPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

}
