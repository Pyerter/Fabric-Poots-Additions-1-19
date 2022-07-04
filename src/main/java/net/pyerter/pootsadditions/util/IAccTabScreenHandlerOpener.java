package net.pyerter.pootsadditions.util;

import net.minecraft.network.listener.ServerPlayPacketListener;
import net.pyerter.pootsadditions.network.packet.CloseAccTabScreenC2SPacket;
import net.pyerter.pootsadditions.network.packet.OpenAccTabScreenC2SPacket;

public interface IAccTabScreenHandlerOpener {

    void onOpenAccTabScreen(OpenAccTabScreenC2SPacket packet);
    void onCloseAccTabScreen(CloseAccTabScreenC2SPacket packet);

}
