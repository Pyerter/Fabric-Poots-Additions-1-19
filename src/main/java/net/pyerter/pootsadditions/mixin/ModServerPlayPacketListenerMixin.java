package net.pyerter.pootsadditions.mixin;

import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.item.inventory.IAccessoriesInventory;
import net.pyerter.pootsadditions.item.inventory.IAccessoryTabsHandlerProvider;
import net.pyerter.pootsadditions.network.packet.CloseAccTabScreenC2SPacket;
import net.pyerter.pootsadditions.network.packet.OpenAccTabScreenC2SPacket;
import net.pyerter.pootsadditions.network.packet.ToggleHideArmorSlotC2SPacket;
import net.pyerter.pootsadditions.network.packet.ToggleHideArmorSlotNotifyPlayersS2CPacket;
import net.pyerter.pootsadditions.screen.AccessoryTabAssistant;
import net.pyerter.pootsadditions.util.IAccTabScreenHandlerOpener;
import net.pyerter.pootsadditions.util.IArmorHider;
import net.pyerter.pootsadditions.util.ICustomServerPacketHandler;
import net.pyerter.pootsadditions.util.IOnC2SScreenHandle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ModServerPlayPacketListenerMixin implements IAccTabScreenHandlerOpener, ICustomServerPacketHandler {

    @Shadow
    ServerPlayerEntity player;

    @Override
    public void onOpenAccTabScreen(OpenAccTabScreenC2SPacket packet) {
        ScreenHandlerType<? extends ScreenHandler> handlerType = packet.getScreenHandlerType();
        NetworkThreadUtils.forceMainThread(packet, (ServerPlayPacketListener)this, player.getWorld());
        ScreenHandler handler = ((IAccessoryTabsHandlerProvider)player).getAccessoryTabsScreenHandler(packet.getSyncId());
        if (handler == null) {
            PootsAdditions.LOGGER.error("Server Side - ScreenHandler for handler type " + handlerType.toString() + " is null... server player entity not switching to that :).");
            return;
        }

        if (player.currentScreenHandler != null)
            player.closeScreenHandler();

        ((IOnC2SScreenHandle)player).runOnServerScreenHandleOpened(handler);
        player.currentScreenHandler = handler;
        ((IOnC2SScreenHandle) player).setScreenHandlerSyncId(handler.syncId);
        PootsAdditions.logInfo("Successfully swapped screen handler! New SyncID: " + handler.syncId);
    }

    @Override
    public void onCloseAccTabScreen(CloseAccTabScreenC2SPacket packet) {
        // :)
    }

    @Inject(method="onClickSlot", at=@At(value="HEAD"))
    public void onCallPacketClickSlot(ClickSlotC2SPacket packet, CallbackInfo info) {
        if (packet.getSyncId() != player.currentScreenHandler.syncId) {
            PootsAdditions.LOGGER.error("FROM PlayerServerEntity.onClickSlot(): Received packet for slot click does not match current screen handler sync ID");
            PootsAdditions.logInfo("Current sync ID is " + player.currentScreenHandler.syncId + " versus " + packet.getSyncId());
        }
    }

    @Override
    public void onToggleHideArmorSlot(ToggleHideArmorSlotC2SPacket packet) {
        IArmorHider.setArmorVisibleByEquipSlot((IArmorHider)player, packet.isSlotVisible(), packet.getEquipmentSlot());
        ToggleHideArmorSlotNotifyPlayersS2CPacket responsePacket = new ToggleHideArmorSlotNotifyPlayersS2CPacket(packet.getEquipmentSlot(), packet.isSlotVisible());
        ((ServerWorld)this.player.world).getChunkManager().sendToOtherNearbyPlayers(player, responsePacket);
    }
}
