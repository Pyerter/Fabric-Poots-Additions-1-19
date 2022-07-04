package net.pyerter.pootsadditions.mixin;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.pyerter.pootsadditions.util.IOnC2SScreenHandle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayerEntity.class)
public abstract class ModPlayerServerEntityScreenHandlerMixin implements IOnC2SScreenHandle {

    @Shadow
    int screenHandlerSyncId;

    @Shadow
    void onScreenHandlerOpened(ScreenHandler screenHandler) { }

    @Override
    public void runOnServerScreenHandleOpened(ScreenHandler screenHandler) {
        onScreenHandlerOpened(screenHandler);
    }

    public void setScreenHandlerSyncId(int id) {
        this.screenHandlerSyncId = id;
    }
}
