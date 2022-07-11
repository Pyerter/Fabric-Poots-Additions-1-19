package net.pyerter.pootsadditions.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.pyerter.pootsadditions.item.inventory.IAccessoryTabsHandlerProvider;
import net.pyerter.pootsadditions.network.packet.ToggleHideArmorFullS2CPacket;
import net.pyerter.pootsadditions.network.packet.ToggleHideArmorSlotS2CPacket;
import net.pyerter.pootsadditions.util.IArmorHider;
import net.pyerter.pootsadditions.util.ICustomClientPacketHandler;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientPlayNetworkHandler.class)
public class ModClientPlayNetworkHandlerMixin implements ICustomClientPacketHandler {

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

    @Override
    public void onToggleHideArmorSlot(ToggleHideArmorSlotS2CPacket packet) {
        IArmorHider.setArmorVisibleByEquipSlot((IArmorHider) client.player, packet.isSlotVisible(), packet.getEquipmentSlot());
    }

    @Override
    public void onToggleHideArmorFull(ToggleHideArmorFullS2CPacket packet) {
        IArmorHider armorHider = (IArmorHider) client.player;
        Boolean[] visibilities = packet.slotVisibilities();
        IArmorHider.setArmorVisibleByEquipSlot(armorHider, visibilities[0], EquipmentSlot.HEAD);
        IArmorHider.setArmorVisibleByEquipSlot(armorHider, visibilities[1], EquipmentSlot.CHEST);
        IArmorHider.setArmorVisibleByEquipSlot(armorHider, visibilities[2], EquipmentSlot.LEGS);
        IArmorHider.setArmorVisibleByEquipSlot(armorHider, visibilities[3], EquipmentSlot.FEET);
    }
}
