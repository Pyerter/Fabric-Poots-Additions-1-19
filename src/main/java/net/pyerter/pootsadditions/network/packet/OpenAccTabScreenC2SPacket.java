package net.pyerter.pootsadditions.network.packet;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import net.pyerter.pootsadditions.util.IAccTabScreenHandlerOpener;
import org.jetbrains.annotations.Nullable;

public class OpenAccTabScreenC2SPacket  implements Packet<ServerPlayPacketListener> {
    private final int syncId;
    private final ScreenHandlerType<?> screenHandlerId;
    private final Text name;


    public OpenAccTabScreenC2SPacket(int syncId, ScreenHandlerType<?> type, Text name) {
        this.syncId = syncId;
        this.screenHandlerId = type;
        this.name = name;
    }

    public OpenAccTabScreenC2SPacket(PacketByteBuf buf) {
        this.syncId = buf.readVarInt();
        this.screenHandlerId = (ScreenHandlerType)buf.readRegistryValue(Registry.SCREEN_HANDLER);
        this.name = buf.readText();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.syncId);
        buf.writeRegistryValue(Registry.SCREEN_HANDLER, this.screenHandlerId);
        buf.writeText(this.name);
    }

    @Override
    public void apply(ServerPlayPacketListener listener) {
        ((IAccTabScreenHandlerOpener)listener).onOpenAccTabScreen(this);
    }

    public int getSyncId() {
        return this.syncId;
    }

    @Nullable
    public ScreenHandlerType<?> getScreenHandlerType() {
        return this.screenHandlerId;
    }

    public Text getName() {
        return this.name;
    }
}
