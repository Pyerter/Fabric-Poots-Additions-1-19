package net.pyerter.pootsadditions.network.packet;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.pyerter.pootsadditions.util.ICustomClientPacketHandler;
import net.pyerter.pootsadditions.util.ICustomServerPacketHandler;

public class ToggleHideArmorSlotNotifyPlayersS2CPacket implements Packet<ClientPlayPacketListener> {
    private final int entitySlotId;
    private final boolean slotVisible;

    public ToggleHideArmorSlotNotifyPlayersS2CPacket(EquipmentSlot slot, boolean slotVisible) {
        this.entitySlotId = slot.getEntitySlotId();
        this.slotVisible = slotVisible;
    }

    public ToggleHideArmorSlotNotifyPlayersS2CPacket(PacketByteBuf buf) {
        this.entitySlotId = buf.readVarInt();
        this.slotVisible = buf.readBoolean();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.entitySlotId);
        buf.writeBoolean(this.slotVisible);
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {
        ((ICustomClientPacketHandler)listener).onToggleHideArmorSlot(this);
    }

    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, this.entitySlotId);
    }

    public boolean isSlotVisible() {
        return this.slotVisible;
    }
}
