package net.pyerter.pootsadditions.util;

import net.minecraft.screen.ScreenHandler;

public interface IOnC2SScreenHandle {

    public void runOnServerScreenHandleOpened(ScreenHandler screenHandler);

    public void setScreenHandlerSyncId(int id);

}
