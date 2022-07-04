package net.pyerter.pootsadditions.item.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.collection.DefaultedList;

public class AccessoriesInventory implements ImplementedInventory, Nameable {
    public static final String INVENTORY_NBT_ID = "pootsadditions.accessories_inventory";
    public static final Integer INVENTORY_SIZE = 8;

    public final DefaultedList<ItemStack> items = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
    public final PlayerEntity player;
    public int selectedSlot = -1;

    public AccessoriesInventory(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    public void updateItems() {
        for (int i = 0; i < items.size(); i++) {
            if (!(items.get(i).isEmpty())) {
                items.get(i).inventoryTick(this.player.world, this.player, i, this.selectedSlot == i);
            }
        }
    }

    public void dropAll() {
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                this.player.dropItem(stack, true, false);
                items.set(i, ItemStack.EMPTY);
            }
        }
    }

    public NbtList writeNbt(NbtList nbtList) {
        NbtCompound nbtCompound;
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).isEmpty()) {
                nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)i);
                items.get(i).writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
            }
        }
        return nbtList;
    }

    public void readNbt(NbtList nbtList) {
        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int slot = nbtCompound.getByte("Slot") & 255;
            ItemStack itemStack = ItemStack.fromNbt(nbtCompound);
            if (!itemStack.isEmpty()) {
                if (slot >= 0 && slot < INVENTORY_SIZE) {
                    items.set(slot, itemStack);
                }
            }
        }
    }

    @Override
    public Text getName() {
        return Text.of("Accessories Inventory");
    }
}
