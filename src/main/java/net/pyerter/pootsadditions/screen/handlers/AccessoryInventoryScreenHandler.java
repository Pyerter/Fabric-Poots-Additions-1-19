package net.pyerter.pootsadditions.screen.handlers;

import com.eliotlash.mclib.math.functions.classic.Mod;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.pyerter.pootsadditions.item.inventory.AccessoriesInventory;
import net.pyerter.pootsadditions.screen.AccessoryTabAssistant;
import net.pyerter.pootsadditions.screen.slot.PickySlot;
import java.util.function.BiPredicate;

import static net.minecraft.screen.PlayerScreenHandler.*;

public class AccessoryInventoryScreenHandler extends ScreenHandler {
    private final AccessoriesInventory inventory;

    public AccessoryInventoryScreenHandler(int syncId, PlayerInventory playerInventory, AccessoriesInventory inventory) {
        super(ModScreenHandlers.ACCESSORIES_INVENTORY_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        checkSize(inventory, AccessoriesInventory.INVENTORY_SIZE);
        inventory.onOpen(playerInventory.player);

        initializeInventory();

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addPlayerEquipment(playerInventory);

        // addProperties(propertyDelegate);
    }

    private void initializeInventory() {
        int startX = 116;
        int starY = 8;
        int offsetY = 18;
        int startX2 = 151;
        int offsetX = startX2 - startX;

        for (int i = 0; i < 2; i++) {
            int x = startX + offsetX * i;
            for (int j = 0; j < 4; j++) {
                int y = starY + offsetY * j;
                this.addSlot(new PickySlot(inventory, i * 4 + j, x, y, (stack, index) -> false));
            }
        }
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);

            // if index corresponds to player inventory
            int invSize = AccessoriesInventory.INVENTORY_SIZE;
            int playerHotbarIndex = invSize + 27;
            int playerEquipmentIndex = playerHotbarIndex + 9;
            int maxInvSize = playerEquipmentIndex + 5;

            if (index >= playerHotbarIndex && index < playerHotbarIndex) {
                if (!(this.insertItem(itemStack2, invSize, playerHotbarIndex, false) || this.insertItem(itemStack2, playerEquipmentIndex, playerEquipmentIndex + 4, false))) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= invSize && index < playerEquipmentIndex && equipmentSlot.getType() == EquipmentSlot.Type.ARMOR &&
                    this.insertItem(itemStack2, playerEquipmentIndex, maxInvSize, false)) {
                // :)
            } else if (equipmentSlot == EquipmentSlot.OFFHAND && !(this.slots.get(playerEquipmentIndex + 4)).hasStack()) {
                if (!this.insertItem(itemStack2, playerEquipmentIndex + 4, maxInvSize, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= invSize && index < playerHotbarIndex) {
                if (!this.insertItem(itemStack2, playerHotbarIndex, playerHotbarIndex + 9, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= playerHotbarIndex && index < playerHotbarIndex + 9) {
                if (!this.insertItem(itemStack2, invSize, playerHotbarIndex, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, playerEquipmentIndex, false)) {
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
                this.addSlot(new Slot(playerInventory, 9 + col + row * 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    private void addPlayerEquipment(PlayerInventory playerInventory) {
        int x = 8, y = 8;
        for (int i = 0; i < 4; i++) {
            final EquipmentSlot equipmentSlot = EQUIPMENT_SLOT_ORDER[i];
            BiPredicate<ItemStack, Integer> equipmentInsertPredicate = (stack, index) -> (equipmentSlot == MobEntity.getPreferredEquipmentSlot(stack));
            PickySlot slot = new PickySlot(playerInventory, PlayerInventory.MAIN_SIZE + 3 - i, x, y + 18 * i, equipmentInsertPredicate);
            slot.backgroundSpriteSupplier = () -> Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, EMPTY_ARMOR_SLOT_TEXTURES[equipmentSlot.getEntitySlotId()]);
            this.addSlot(slot);
        }
        PickySlot offhandSlot = new PickySlot(playerInventory, PlayerInventory.OFF_HAND_SLOT, 77, 62);
        offhandSlot.backgroundSpriteSupplier = () -> Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT);
        this.addSlot(offhandSlot);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (AccessoryTabAssistant.userOpenHandledScreen(player, id)) {
            return true;
        }
        return super.onButtonClick(player, id);
        // have code to open up other tabs
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return ModScreenHandlers.ACCESSORIES_INVENTORY_SCREEN_HANDLER;
    }

    public static final Identifier[] EMPTY_ARMOR_SLOT_TEXTURES = new Identifier[]{EMPTY_BOOTS_SLOT_TEXTURE, EMPTY_LEGGINGS_SLOT_TEXTURE, EMPTY_CHESTPLATE_SLOT_TEXTURE, EMPTY_HELMET_SLOT_TEXTURE};
    public static final EquipmentSlot[] EQUIPMENT_SLOT_ORDER = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
}
