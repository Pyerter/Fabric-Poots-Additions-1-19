package net.pyerter.pootsadditions.screen.handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Pair;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.block.entity.EngineeringStationEntity;
import net.pyerter.pootsadditions.screen.slot.ModFuelSlot;
import net.pyerter.pootsadditions.screen.slot.ModResultSlot;
import net.pyerter.pootsadditions.screen.slot.PickySlot;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class EngineeringStationScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final EngineeringStationEntity station;

    public EngineeringStationScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(EngineeringStationEntity.ENGINEERING_STATION_INVENTORY_SIZE), new ArrayPropertyDelegate(2));
    }

    public EngineeringStationScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        this(syncId, playerInventory, inventory, propertyDelegate, null);
    }

    public EngineeringStationScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate, EngineeringStationEntity station) {
        super(ModScreenHandlers.ENGINEERING_STATION_SCREEN_HANDLER, syncId);
        checkSize(inventory, EngineeringStationEntity.ENGINEERING_STATION_INVENTORY_SIZE);
        this.inventory = inventory;
        this.station = station;
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = propertyDelegate;

        BiPredicate<ItemStack, Integer> pickyPredicate = EngineeringStationEntity::acceptsQuickTransfer;

        // add slots
        this.addSlot(new PickySlot(inventory, 0, 54, 32, pickyPredicate, 0)); // left slot
        this.addSlot(new PickySlot(inventory, 1, 102, 32, pickyPredicate, 1)); // right slot
        this.addSlot(new PickySlot(inventory, 2, 78, 53, pickyPredicate, 2)); // bottom slot
        this.addSlot(new PickySlot(inventory, 3, 78, 11, pickyPredicate, 3)); // top slot

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(propertyDelegate);
    }

    // This method handles quick transfers (shift clicks).
    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            // if index corresponds to player inventory
            if (index != 3 && index != 2 && index != 1 && index != 0) {
                if (this.insertItem(itemStack2, 0, 4, false)) {
                    // :)
                } else if (index >= 4 && index < 31) {
                    if (!this.insertItem(itemStack2, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 31 && index < 40 && !this.insertItem(itemStack2, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            // finally, try to quick transfer from the primary slots of machine to inventory
            } else if (!this.insertItem(itemStack2, 4, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    public Pair<Boolean, Boolean> tryHammer() {
        PootsAdditions.logInfo("Trying hammer?");
        if (station != null) {
            return station.hammerIt();
        } else {
            PootsAdditions.logInfo("It's null!");
            return new Pair<>(false, false);
        }
        // return station != null ? station.hammerIt() : new Pair<>(false, false);
    }

    public int getHammerStage() {
        return propertyDelegate.get(0);
    }

    public boolean getSuccess() {
        return propertyDelegate.get(1) > 0;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        station.resetHammeredProgress();
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id == 0 && tryHammer().getLeft())
            return true;
        return super.onButtonClick(player, id);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, 9 + col + row * 9, 8 + col * 18, 86 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
