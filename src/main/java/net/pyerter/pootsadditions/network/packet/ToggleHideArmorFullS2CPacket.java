package net.pyerter.pootsadditions.network.packet;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.pyerter.pootsadditions.util.ICustomClientPacketHandler;

public class ToggleHideArmorFullS2CPacket implements Packet<ClientPlayNetworkHandler> {
    private final boolean headVisible;
    private final boolean chestVisible;
    private final boolean legsVisible;
    private final boolean feetVisible;

    public ToggleHideArmorFullS2CPacket(boolean headVisible, boolean chestVisible, boolean legsVisible, boolean feetVisible) {
        this.headVisible = headVisible;
        this.chestVisible = chestVisible;
        this.legsVisible = legsVisible;
        this.feetVisible = feetVisible;
    }

    public ToggleHideArmorFullS2CPacket(PacketByteBuf buf) {
        this.headVisible = buf.readBoolean();
        this.chestVisible = buf.readBoolean();
        this.legsVisible = buf.readBoolean();
        this.feetVisible = buf.readBoolean();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(this.headVisible);
        buf.writeBoolean(this.chestVisible);
        buf.writeBoolean(this.legsVisible);
        buf.writeBoolean(this.feetVisible);
    }

    @Override
    public void apply(ClientPlayNetworkHandler listener) {
        ((ICustomClientPacketHandler)listener).onToggleHideArmorFull(this);
    }

    public Boolean[] slotVisibilities() {
        return new Boolean[]{ this.headVisible, this.chestVisible, this.legsVisible, this.feetVisible };
    }
}
