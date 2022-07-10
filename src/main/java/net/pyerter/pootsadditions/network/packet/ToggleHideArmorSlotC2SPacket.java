package net.pyerter.pootsadditions.network.packet;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import net.pyerter.pootsadditions.util.ICustomServerPacketHandler;

public class ToggleHideArmorSlotC2SPacket implements Packet<ServerPlayPacketListener> {
    private final int entitySlotId;
    private final boolean slotVisible;

    public ToggleHideArmorSlotC2SPacket(EquipmentSlot slot, boolean slotVisible) {
        this.entitySlotId = slot.getEntitySlotId();
        this.slotVisible = slotVisible;
    }

    public ToggleHideArmorSlotC2SPacket(PacketByteBuf buf) {
        this.entitySlotId = buf.readVarInt();
        this.slotVisible = buf.readBoolean();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.entitySlotId);
        buf.writeBoolean(this.slotVisible);
    }

    @Override
    public void apply(ServerPlayPacketListener listener) {
        ((ICustomServerPacketHandler)listener).onToggleHideArmorSlot(this);
    }

    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, this.entitySlotId);
    }

    public boolean isSlotVisible() {
        return this.slotVisible;
    }
}
