package net.pyerter.pootsadditions.screen.handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.block.entity.TridiBlockEntity;
import net.pyerter.pootsadditions.item.ModItems;
import net.pyerter.pootsadditions.item.custom.PautschItem;
import net.pyerter.pootsadditions.screen.slot.ModFuelSlot;
import net.pyerter.pootsadditions.screen.slot.ModResultSlot;
import org.jetbrains.annotations.Nullable;

public class PautschItemScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final ItemStack pautschStack;
    private final PautschItem pautschItem;
    private final World world;

    public PautschItemScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(PautschItem.PAUTSCH_INVENTORY_SIZE), (playerInventory.player.getMainHandStack()), (PautschItem)ModItems.PAUTSCH_ITEM);
    }

    public PautschItemScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack pautschStack, PautschItem pautschItem) {
        this(syncId, playerInventory, new SimpleInventory(PautschItem.PAUTSCH_INVENTORY_SIZE), pautschStack, pautschItem);
    }

    public PautschItemScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ItemStack pautschStack, PautschItem pautschItem) {
        super(ModScreenHandlers.TRIDI_SCREEN_HANDLER, syncId);
        checkSize(inventory, PautschItem.PAUTSCH_INVENTORY_SIZE);
        this.inventory = inventory;
        this.pautschStack = pautschStack;
        this.pautschItem = pautschItem;
        this.world = playerInventory.player.world;
        inventory.onOpen(playerInventory.player);

        PootsAdditions.logInfo("SyncID: " + syncId);

        // add slots
        // 9 x 3 grid
        initializeInventory();

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        // addProperties(propertyDelegate);
    }

    private void initializeInventory() {
        int startX = 8;
        int starY = 7;
        int offsetX = 18;
        int offsetY = 18;
        int cols = 9;
        int rows = 3;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                this.addSlot(new Slot(inventory, row * 9 + col, startX + col * offsetX, starY + row * offsetY));
            }
        }
    }

    @Override
    public void close(PlayerEntity player) {
        updatePautschInventory();
        super.close(player);
    }

    public void updatePautschInventory() {
        if (pautschItem == null || pautschStack == null || world.isClient())
            return;

        DefaultedList<ItemStack> invList = DefaultedList.ofSize(PautschItem.PAUTSCH_INVENTORY_SIZE, ItemStack.EMPTY);
        for (int i = 0; i < PautschItem.PAUTSCH_INVENTORY_SIZE; i++) {
            invList.set(i, inventory.getStack(i));
        }

        pautschItem.createInventory(pautschStack, invList);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            // if index corresponds to player inventory
            if (index >= 27) {
                if (index >= 27 && index < 54) {
                    if (!this.insertItem(itemStack2, 54, 63, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 54 && index < 63 && !this.insertItem(itemStack2, 27, 54, false)) {
                    return ItemStack.EMPTY;
                }
                // finally, try to quick transfer from the primary slots of machine to inventory
            } else if (!this.insertItem(itemStack2, 27, 63, false)) {
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
