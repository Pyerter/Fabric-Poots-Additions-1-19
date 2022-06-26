package net.pyerter.pootsadditions.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

public class InventoryUtil {
    public static final String DEFAULT_INVENTORY_NBT_ID = "InvItems";

    public static boolean hasPlayerStackInInventory(PlayerEntity player, Item item) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack currentStack = player.getInventory().getStack(i);
            if (!currentStack.isEmpty() && currentStack.isItemEqual(new ItemStack(item))) {
                return true;
            }
        }
        return false;
    }

    public static int getFirstInventoryIndex(PlayerEntity player, Item item) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack currentStack = player.getInventory().getStack(i);
            if (!currentStack.isEmpty() && currentStack.isItemEqual(new ItemStack(item))) {
                return i;
            }
        }
        return -1;
    }

    public static NbtCompound writeItemsToNbt(NbtCompound nbt, int listSize, String listID) {
        return writeItemsToNbt(nbt, DefaultedList.ofSize(listSize, ItemStack.EMPTY), listID);
    }


    public static NbtCompound writeItemsToNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks, String listID) {
        if (Util.isNullOrBlank(listID)) {
            listID = DEFAULT_INVENTORY_NBT_ID;
        }
        nbt.put(listID, getNbtListFromItems(stacks, false));
        return nbt;
    }

    public static NbtList getNbtListFromItems(DefaultedList<ItemStack> stacks) {
        return getNbtListFromItems(stacks, false);
    }

    public static NbtList getNbtListFromItems(DefaultedList<ItemStack> stacks, boolean writeEmptyItems) {
        NbtList nbtList = new NbtList();
        for(int i = 0; i < stacks.size(); ++i) {
            ItemStack itemStack = (ItemStack)stacks.get(i);
            if (!itemStack.isEmpty()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte) i);
                itemStack.writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
            }
        }

        return nbtList;
    }

    public static DefaultedList<ItemStack> getItemsFromNbtList(NbtList nbtList, int listSize) {
        return getItemsFromNbtList(nbtList, DefaultedList.ofSize(listSize, ItemStack.EMPTY));
    }

    public static DefaultedList<ItemStack> getItemsFromNbtList(NbtList nbtList, DefaultedList<ItemStack> stacks) {
        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 255;
            if (j >= 0 && j < stacks.size()) {
                ItemStack stack = ItemStack.fromNbt(nbtCompound);
                stacks.set(j, stack);
            }
        }

        return stacks;
    }

    public static DefaultedList<ItemStack> getItemsFromNbt(NbtCompound nbt, String listID, int listSize) {
        return getItemsFromNbt(nbt, listID, DefaultedList.ofSize(listSize, ItemStack.EMPTY));
    }

    public static DefaultedList<ItemStack> getItemsFromNbt(NbtCompound nbt, String listID, DefaultedList<ItemStack> stacks) {
        if (Util.isNullOrBlank(listID)) {
            listID = DEFAULT_INVENTORY_NBT_ID;
        }
        NbtList nbtList = nbt.getList(listID, 10);
        return getItemsFromNbtList(nbtList, stacks);
    }

    /**
     * Tries to merge as many things from the second list to the first. Returns false if the entire list is not merged.
     * **/
    public static boolean tryMergeItemLists(DefaultedList<ItemStack> stackInto, DefaultedList<ItemStack> stackFrom) {
        int i = 0;
        int j = 0;
        while (i < stackInto.size() && j < stackFrom.size()) {
            while (stackInto.get(i).isEmpty() && j < stackFrom.size()) {
                if (!stackFrom.get(j).isEmpty()) {
                    stackInto.set(i, stackFrom.get(j));
                    stackFrom.set(j, ItemStack.EMPTY);
                }
                j++;
            }
            i++;
        }
        return i == stackInto.size();
    }

}
