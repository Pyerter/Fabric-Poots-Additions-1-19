package net.pyerter.pootsadditions.screen.handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public interface NbtItemNamedScreenHandlerFactory {
    @Nullable
    ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player, ItemStack stack);

    Text getDisplayName();

}
