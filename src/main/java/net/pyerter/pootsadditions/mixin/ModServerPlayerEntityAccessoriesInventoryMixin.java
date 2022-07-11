package net.pyerter.pootsadditions.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerSyncHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.item.inventory.IAccessoryTabsHandlerProvider;
import net.pyerter.pootsadditions.network.packet.ToggleHideArmorFullS2CPacket;
import net.pyerter.pootsadditions.util.IArmorHider;
import net.pyerter.pootsadditions.util.ScreenHanderSyncHandlerOwner;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ModServerPlayerEntityAccessoriesInventoryMixin extends PlayerEntity implements ScreenHanderSyncHandlerOwner {
    protected boolean initialUpdate = false;
    @Shadow
    ScreenHandlerSyncHandler screenHandlerSyncHandler;

    @Shadow
    void onScreenHandlerOpened(ScreenHandler screenHandler) {
    }

    @Shadow public ServerPlayNetworkHandler networkHandler;

    @Override
    public void doInitialSyncOfScreenHandlers(List<ScreenHandler> handlers) {
        for (ScreenHandler handler: handlers) {
            PootsAdditions.logInfo("Syncing screen handler on spawn, syncId: " + handler.syncId);
            onScreenHandlerOpened(handler);
        }
    }

    public void doInitialSyncOfArmorVisibility() {
        if (this.world.isClient)
            return;

        IArmorHider armorHider = (IArmorHider)this;
        this.networkHandler.sendPacket(
                new ToggleHideArmorFullS2CPacket(armorHider.isArmorHeadVisible(),
                        armorHider.isArmorChestVisible(),
                        armorHider.isArmorLegsVisible(),
                        armorHider.isArmorFeetVisible()));
    }

    @Inject(method="onSpawn", at=@At("HEAD"))
    public void onServerPlayerEntitySpawnDoSync(CallbackInfo info) {
        doInitialSyncOfScreenHandlers(((IAccessoryTabsHandlerProvider)this).getAllRegisteredAccessoryScreenHandlers());
        //doInitialSyncOfArmorVisibility();
    }

    public ModServerPlayerEntityAccessoriesInventoryMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Override
    public ScreenHandlerSyncHandler getSyncHandler() {
        return screenHandlerSyncHandler;
    }
}
