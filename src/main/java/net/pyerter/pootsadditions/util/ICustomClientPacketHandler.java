package net.pyerter.pootsadditions.util;

import net.pyerter.pootsadditions.network.packet.ToggleHideArmorSlotC2SPacket;
import net.pyerter.pootsadditions.network.packet.ToggleHideArmorSlotNotifyPlayersS2CPacket;

public interface ICustomClientPacketHandler {

    public void onToggleHideArmorSlot(ToggleHideArmorSlotNotifyPlayersS2CPacket packet);

}
