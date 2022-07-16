package net.pyerter.pootsadditions.item.inventory;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.collection.DefaultedList;
import net.pyerter.pootsadditions.item.custom.accessory.AccessoryItem;

import java.util.ArrayList;
import java.util.List;

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
                    ((AccessoryItem)targetStack.getItem()).accessoryTick(this.player.world, this.player, this, targetStack, i, this.selectedSlot == i);
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

    public ItemStack getBeltAccessorySlot() { return getStack(7); }

    public boolean acceptsItemInSlot(ItemStack stack, int slotIndex) {
        if (!stack.isEmpty() && (slotIndex <= 3 && slotIndex >= 0)) {
            if (stack.getItem() instanceof ArmorItem) {
                ArmorItem armorItem = (ArmorItem) stack.getItem();
                EquipmentSlot slotType = armorItem.getSlotType();
                switch (slotIndex) {
                    case 0: return slotType == EquipmentSlot.HEAD;
                    case 1: return slotType == EquipmentSlot.CHEST;
                    case 2: return slotType == EquipmentSlot.LEGS;
                    case 3: return slotType == EquipmentSlot.FEET;
                }
            }
            return false;
        }
        if (!stack.isEmpty() && stack.getItem() instanceof AccessoryItem) {
            AccessoryItem item = (AccessoryItem) stack.getItem();
            switch (item.getEquipStyle()) {
                case BACK: case BACK_SHEATHED: return slotIndex == 5;
                case BELT_LEFT: case BELT_BACK: case BELT_RIGHT: return slotIndex == 7;
            }
        }
        return false;
    }

    public List<ItemStack> getAllEquipmentStacks(PlayerEntity entity) {
        List<ItemStack> stacks = new ArrayList<>();

        ItemStack mainHand = entity.getMainHandStack();
        ItemStack offHand = entity.getOffHandStack();
        if (!mainHand.isEmpty())
            stacks.add(mainHand);
        if (!offHand.isEmpty())
            stacks.add(offHand);

        for (ItemStack item: items) {
            if (!item.isEmpty())
                stacks.add(item);
        }

        for (int i = 0; i < entity.getInventory().armor.size(); i++) {
            ItemStack armorStack = entity.getInventory().getArmorStack(i);
            if (!armorStack.isEmpty())
                stacks.add(armorStack);
        }

        return stacks;
    }

    public ItemStack getArmorSlot(EquipmentSlot slot) {
        switch (slot) {
            case HEAD: return items.get(0);
            case CHEST: return items.get(1);
            case LEGS: return items.get(2);
            case FEET: return items.get(3);
        }
        return ItemStack.EMPTY;
    }
}
