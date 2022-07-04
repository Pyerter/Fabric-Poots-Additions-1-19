package net.pyerter.pootsadditions.screen.factories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class InventoryScreenFactory implements NamedScreenHandlerFactory {
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new PlayerScreenHandler(inv, true, player);
    }

    @Override
    public Text getDisplayName() {
        return Text.of("Inventory");
    }
}
