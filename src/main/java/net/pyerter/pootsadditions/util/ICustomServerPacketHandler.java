package net.pyerter.pootsadditions.util;

import net.pyerter.pootsadditions.network.packet.ToggleHideArmorSlotC2SPacket;

public interface ICustomServerPacketHandler {

    public void onToggleHideArmorSlot(ToggleHideArmorSlotC2SPacket packet);

}
