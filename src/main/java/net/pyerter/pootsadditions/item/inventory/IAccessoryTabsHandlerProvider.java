package net.pyerter.pootsadditions.item.inventory;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

import java.util.List;

public interface IAccessoryTabsHandlerProvider {

    public <T extends ScreenHandler> T getAccessoryTabsScreenHandler(int registeredScreenHandlerIndex);
    public List<ScreenHandler> getAllRegisteredAccessoryScreenHandlers();

}
