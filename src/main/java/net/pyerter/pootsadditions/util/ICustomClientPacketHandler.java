package net.pyerter.pootsadditions.util;

import net.pyerter.pootsadditions.network.packet.ToggleHideArmorFullS2CPacket;
import net.pyerter.pootsadditions.network.packet.ToggleHideArmorSlotS2CPacket;

public interface ICustomClientPacketHandler {

    public void onToggleHideArmorSlot(ToggleHideArmorSlotS2CPacket packet);

    public void onToggleHideArmorFull(ToggleHideArmorFullS2CPacket packet);

}
