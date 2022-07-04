package net.pyerter.pootsadditions.item.inventory;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public interface IAccessoryTabsHandlerProvider {

    public <T extends ScreenHandler> T getAccessoryTabsScreenHandler(int registeredScreenHandlerIndex);

}
