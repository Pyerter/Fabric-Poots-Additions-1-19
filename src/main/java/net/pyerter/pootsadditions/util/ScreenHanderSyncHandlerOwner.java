package net.pyerter.pootsadditions.util;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerSyncHandler;

import java.util.List;

public interface ScreenHanderSyncHandlerOwner {

    public ScreenHandlerSyncHandler getSyncHandler();

    public void doInitialSyncOfScreenHandlers(List<ScreenHandler> handlers);

}
