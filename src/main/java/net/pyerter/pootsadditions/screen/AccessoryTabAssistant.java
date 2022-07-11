package net.pyerter.pootsadditions.screen;

import com.eliotlash.mclib.math.functions.limit.Min;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.item.inventory.IAccessoryTabsHandlerProvider;
import net.pyerter.pootsadditions.network.packet.OpenAccTabScreenC2SPacket;
import net.pyerter.pootsadditions.screen.factories.InventoryScreenFactory;
import net.pyerter.pootsadditions.screen.handlers.AccessoryTabsScreenFactory;
import net.pyerter.pootsadditions.screen.handlers.AccessoryTabsScreenHandlerFactory;
import net.pyerter.pootsadditions.screen.handlers.ModScreenHandlers;
import net.pyerter.pootsadditions.util.ICursorController;
import net.pyerter.pootsadditions.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class AccessoryTabAssistant {

    public static final Identifier ACC_TABS_TEXTURE = new Identifier(PootsAdditions.MOD_ID, "textures/gui/accessory_inventory_tabs_gui.png");
    public static final Map<ScreenHandlerType<? extends ScreenHandler>, Pair<AccessoryTabsScreenFactory, AccessoryTabsScreenHandlerFactory>> registeredScreens = new HashMap<>();
    public static final Map<ScreenHandlerType<? extends ScreenHandler>, Text> registeredScreenNames = new HashMap<>();
    private static final List<ScreenHandlerType<? extends ScreenHandler>> registeredScreenTypes = initializeList();
    private static List<ScreenHandlerType<? extends ScreenHandler>> initializeList() {
        List<ScreenHandlerType<? extends ScreenHandler>> list = new ArrayList<>();
        return list;
    }

    public static int getFreeButtonId() {
        return registeredScreenTypes.size() + 1;
    }

    public static boolean tryRegisterScreens(ScreenHandlerType<? extends ScreenHandler> screenHandlerType, Pair<AccessoryTabsScreenFactory, AccessoryTabsScreenHandlerFactory> screenSupplier, Text screenName) {
        if (!registeredScreenTypes.contains(screenHandlerType) && screenSupplier != null) {
            registeredScreenTypes.add(screenHandlerType);
            registeredScreens.put(screenHandlerType, screenSupplier);
            registeredScreenNames.put(screenHandlerType, screenName);
            return true;
        } else if (screenSupplier == null)
            PootsAdditions.logInfo("ERROR - screenSupplier given to register screens is null");
        return false;
    }

    public static int getScreenTab(ScreenHandlerType<? extends ScreenHandler> screenHandlerType) {
        int holdingIndex = registeredScreenTypes.indexOf(screenHandlerType);
        if (holdingIndex != -1)
            return holdingIndex + 1;

        return -1;
    }

    public static int getSyncId(ScreenHandlerType<? extends ScreenHandler> screenHandlerType) {
        int generatedSyncId = -1;

        int index = registeredScreenTypes.indexOf(screenHandlerType);
        if (index != -1)
            generatedSyncId = index + 1;

        return generatedSyncId;
    }

    public static boolean generateScreenHandlers(List<ScreenHandler> targetList, PlayerInventory inventory, boolean onServer, PlayerEntity owner) {
        for (ScreenHandlerType<? extends ScreenHandler> screenHandlerType: registeredScreenTypes) {
            targetList.add(registeredScreens.get(screenHandlerType).getRight().generateScreenHandler(inventory, onServer, owner));
        }
        return true;
    }

    public static void drawTabBackground(DrawableHelper drawableHelper, MatrixStack matrices, int x, int y) {
       drawableHelper.drawTexture(matrices, x + 24, y - 30, 0, 50, 128,  30);
    }

    public static boolean mouseInAccessoryTab(int x, int y, double mouseX, double mouseY, int tab) {
        int minXTab = x + 24 + 6 + 24 * tab, minYTab = y + -30 + 11;
        int maxXTab = minXTab + 19, maxYTab = minYTab + 18;
        return Util.pointInRectangleByCorners(mouseX, mouseY, minXTab, minYTab, maxXTab, maxYTab);
    }

    public static void drawHoverTab(DrawableHelper drawableHelper, MatrixStack matrices, int x, int y, int tab) {
        int minX = x + 24 + 5 + 24 * tab;
        int minY = y - 30 + 10;
        drawableHelper.drawTexture(matrices, minX, minY, 0, 30, 22, 20);
    }

    public static void drawHeldTab(DrawableHelper drawableHelper, MatrixStack matrices, int x, int y, int tab) {
        int minX = x + 24 + 5 + 24 * tab;
        int minY = y - 30 + 10;
        drawableHelper.drawTexture(matrices, minX, minY, 46, 30, 22, 20);
    }

    public static void drawAccTabIcon(DrawableHelper drawableHelper, MatrixStack matrices, int x, int y, int tab) {
        int minX = x + 24 + 8 + 24 * tab;
        int minY = y - 30 + 13;
        drawableHelper.drawTexture(matrices, minX, minY, 128 + 16 * tab, 0, 16, 16);
    }

    public static int tryClickAction(ScreenHandlerType<?> handlerType, int x, int y, double mouseX, double mouseY, int button) {
        return tryClickAction(handlerType, x, y, mouseX, mouseY, button, false);
    }

    public static int tryClickAction(ScreenHandlerType<?> handlerType, int x, int y, double mouseX, double mouseY, int button, boolean playerScreen) {
        int currentTab;
        if (playerScreen)
            currentTab = 0;
        else
            currentTab = getScreenTab(handlerType);

        int targetTab = -1;

        if (currentTab != 0 && AccessoryTabAssistant.mouseInAccessoryTab(x, y, mouseX, mouseY, 0)) {
            Pair<Double, Double> mousePos = freezeMouse();
            if (MinecraftClient.getInstance().player.currentScreenHandler != null)
                MinecraftClient.getInstance().player.closeHandledScreen();
            openInventoryScreenManually();
            unfreezeMouse(mousePos);
            targetTab = 0;
        }

        if (targetTab == -1) {
            for (int i = 0; i < registeredScreenTypes.size(); i++) {
                int tab = i + 1;
                if (currentTab != tab && AccessoryTabAssistant.mouseInAccessoryTab(x, y, mouseX, mouseY, tab)) {
                    Pair<Double, Double> mousePos = freezeMouse();
                    if (MinecraftClient.getInstance().player.currentScreenHandler != null)
                        MinecraftClient.getInstance().player.closeScreen();
                    PootsAdditions.logInfo("Clicked tab " + tab);
                    ScreenHandlerType<? extends ScreenHandler> screenType = registeredScreenTypes.get(i);
                    tryOpenTabScreen(screenType, true);
                    unfreezeMouse(mousePos);
                    targetTab = tab;
                    break;
                }
            }
        }

        //if (targetTab > 0 && MinecraftClient.getInstance().player.currentScreenHandler != null)
        //    MinecraftClient.getInstance().player.closeHandledScreen();

        if (targetTab != -1)
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK, MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER), 1f);

        return targetTab;
    }

    public static boolean tryOpenTabScreen(ScreenHandlerType<? extends ScreenHandler> handlerType) {
        return tryOpenTabScreen(handlerType, false) == ActionResult.SUCCESS;
    }

    public static ActionResult tryOpenTabScreen(ScreenHandlerType<? extends ScreenHandler> handlerType, boolean clientSide) {
        if (registeredScreenTypes.contains(handlerType)) {
            // check if screen is already open
            int screenIndex = getScreenTab(handlerType);
            if (MinecraftClient.getInstance().player.currentScreenHandler == ((IAccessoryTabsHandlerProvider)MinecraftClient.getInstance().player).getAccessoryTabsScreenHandler(screenIndex)) {
                return ActionResult.PASS;
            }

            // create a screen with the proper handler
            HandledScreen newScreen = registeredScreens.get(handlerType).getLeft().generateScreen(MinecraftClient.getInstance().player);

            // ensure the screen will not cause errors
            if (newScreen == null || newScreen.getScreenHandler() == null) {
                PootsAdditions.LOGGER.error("Screen or ScreenHandler for handler type " + handlerType.toString() + " is null... I'm not switching to that screen :).");
                return ActionResult.FAIL;
            }

            // set the new screen
            MinecraftClient.getInstance().player.currentScreenHandler = newScreen.getScreenHandler();
            MinecraftClient.getInstance().setScreen(newScreen);

            // notify the server
            if (clientSide)
                MinecraftClient.getInstance().player.networkHandler.sendPacket(new OpenAccTabScreenC2SPacket(getSyncId(handlerType), handlerType, registeredScreenNames.get(handlerType)));
            // Hooray!
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    public static void tryCloseScreen() {
        MinecraftClient.getInstance().player.closeHandledScreen();
    }

    public static boolean userOpenHandledScreen(PlayerEntity entity, int tab) {
        /*if (entity.currentScreenHandler != null)
            entity.currentScreenHandler.close(entity);

        if (tab == 0) {
            return true;
        }
        if (tab > 0 && tab - 1 < registeredScreenTypes.size()) {
            PootsAdditions.logInfo("Attemptig to construct screen on tab " + tab);
            entity.openHandledScreen(registeredScreens.get(registeredScreenTypes.get(tab - 1)));
            return true;
        }*/
        return false;
    }

    public static void openInventoryScreenWithKeybinds() {
        KeyBinding.onKeyPressed(InputUtil.fromTranslationKey(MinecraftClient.getInstance().options.inventoryKey.getBoundKeyTranslationKey()));
        KeyBinding.onKeyPressed(InputUtil.fromTranslationKey(MinecraftClient.getInstance().options.inventoryKey.getBoundKeyTranslationKey()));
        KeyBinding.updatePressedStates();
    }

    public static void openInventoryScreenManually() {
        if (MinecraftClient.getInstance().interactionManager.hasRidingInventory()) {
            MinecraftClient.getInstance().player.openRidingInventory();
        } else {
            MinecraftClient.getInstance().getTutorialManager().onInventoryOpened();
            MinecraftClient.getInstance().setScreen(new InventoryScreen(MinecraftClient.getInstance().player));
        }
    }

    public static Pair<Double, Double> freezeMouse() {
        Mouse mouse = MinecraftClient.getInstance().mouse;
        //mouse.lockCursor();
        return new Pair<>(mouse.getX(), mouse.getY());
    }

    public static void unfreezeMouse(Pair<Double, Double> targetPos) {
        Mouse mouse = MinecraftClient.getInstance().mouse;
        //mouse.unlockCursor();
        if (targetPos != null && mouse instanceof ICursorController) {
            //((ICursorController)mouse).setCursorPosition(targetPos.getLeft(), targetPos.getRight());
        }
    }
}
