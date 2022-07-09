package net.pyerter.pootsadditions.mixin;

import net.pyerter.pootsadditions.PootsAdditions;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class ModHelloMixin {

	@Inject(method = "init", at = @At("HEAD"))
	private void init(CallbackInfo info) {
		PootsAdditions.logInfo("Let's get ready to CRRAAAAFFFFTTT!");
	}
}
