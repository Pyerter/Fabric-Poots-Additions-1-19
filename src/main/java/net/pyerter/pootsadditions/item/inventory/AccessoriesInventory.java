package net.pyerter.pootsadditions.item.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.collection.DefaultedList;
import net.pyerter.pootsadditions.item.custom.accessory.AccessoryItem;

public class AccessoriesInventory implements ImplementedInventory, Nameable {
    public static final String INVENTORY_NBT_ID = "pootsadditions.accessories_inventory";
    public static final Integer INVENTORY_SIZE = 8;

    public final DefaultedList<ItemStack> items = DefaultedList.ofSize(8, ItemStack.EMPTY);
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
            ItemStack targetStack = items.get(i);
            if (!(targetStack.isEmpty())) {
                if (targetStack.getItem() instanceof AccessoryItem)
                    ((AccessoryItem)targetStack.getItem()).accessoryTick(this.player.world, this.player, targetStack, i, this.selectedSlot == i);
                else
                    targetStack.inventoryTick(this.player.world, this.player, i, this.selectedSlot == i);
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
        items.clear();

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

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return player.equals(this.player);
    }

    public ItemStack getBackAccessorySlot() {
        return getStack(5);
    }

    public boolean acceptsItemInSlot(ItemStack stack, int slotIndex) {
        if (!stack.isEmpty() && (slotIndex <= 3 && slotIndex >= 0))
            return true;
        if (!stack.isEmpty() && stack.getItem() instanceof AccessoryItem) {
            AccessoryItem item = (AccessoryItem) stack.getItem();
            switch (item.getEquipStyle()) {
                case BACK: case BACK_SHEATHED: return slotIndex == 5;
            }
        }
        return false;
    }
}
