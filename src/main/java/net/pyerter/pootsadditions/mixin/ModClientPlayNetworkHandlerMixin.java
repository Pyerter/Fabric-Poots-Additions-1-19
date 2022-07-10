package net.pyerter.pootsadditions.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.pyerter.pootsadditions.item.inventory.IAccessoryTabsHandlerProvider;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientPlayNetworkHandler.class)
public class ModClientPlayNetworkHandlerMixin {

    @Shadow
    MinecraftClient client;

    @Inject(method="onInventory", at=@At("RETURN"))
    public void onInventoryDoAccessoryCheckSync(InventoryS2CPacket packet, CallbackInfo info) {
        PlayerEntity playerEntity = this.client.player;
        if (packet.getSyncId() != playerEntity.currentScreenHandler.syncId) {
            List<ScreenHandler> registeredScreenHandlers = ((IAccessoryTabsHandlerProvider)playerEntity).getAllRegisteredAccessoryScreenHandlers();
            for (ScreenHandler handler: registeredScreenHandlers) {
                if (handler.syncId == packet.getSyncId()) {
                    handler.updateSlotStacks(packet.getRevision(), packet.getContents(), packet.getCursorStack());
                    break;
                }
            }
        }
    }

}
