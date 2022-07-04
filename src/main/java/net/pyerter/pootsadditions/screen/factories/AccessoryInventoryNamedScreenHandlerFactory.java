package net.pyerter.pootsadditions.screen.factories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.pyerter.pootsadditions.item.inventory.IAccessoriesInventory;
import net.pyerter.pootsadditions.screen.handlers.AccessoryInventoryScreenHandler;
import org.jetbrains.annotations.Nullable;

public class AccessoryInventoryNamedScreenHandlerFactory implements NamedScreenHandlerFactory {
    @Override
    public Text getDisplayName() {
        return Text.of("Accessory Inventory Screen");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new AccessoryInventoryScreenHandler(syncId, inv, ((IAccessoriesInventory)player).getAccessoriesInventory());
    }
}
