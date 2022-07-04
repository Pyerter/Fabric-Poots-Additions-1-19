package net.pyerter.pootsadditions.screen.handlers;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;

public interface AccessoryTabsScreenFactory {

    public HandledScreen generateScreen(ClientPlayerEntity player);

}
