package net.pyerter.pootsadditions.screen.handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;

public interface AccessoryTabsScreenHandlerFactory {

    public ScreenHandler generateScreenHandler(PlayerInventory inventory, boolean onServer, final PlayerEntity owner);

}
