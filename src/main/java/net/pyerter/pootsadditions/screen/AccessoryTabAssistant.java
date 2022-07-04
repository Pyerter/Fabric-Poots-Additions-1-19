package net.pyerter.pootsadditions.screen;

import com.eliotlash.mclib.math.functions.limit.Min;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.screen.factories.InventoryScreenFactory;
import net.pyerter.pootsadditions.screen.handlers.ModScreenHandlers;
import net.pyerter.pootsadditions.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class AccessoryTabAssistant {

    public static final Identifier ACC_TABS_TEXTURE = new Identifier(PootsAdditions.MOD_ID, "textures/gui/accessory_inventory_tabs_gui.png");
    public static final Map<ScreenHandlerType<? extends ScreenHandler>, NamedScreenHandlerFactory> registeredScreens = new HashMap<>();
    private static final List<ScreenHandlerType<? extends ScreenHandler>> registeredScreenTypes = initializeList();
    private static List<ScreenHandlerType<? extends ScreenHandler>> initializeList() {
        List<ScreenHandlerType<? extends ScreenHandler>> list = new ArrayList<>();
        return list;
    }

    public static boolean tryRegisterScreens(ScreenHandlerType<? extends ScreenHandler> screenHandlerType, NamedScreenHandlerFactory screenSupplier) {
        if (!registeredScreenTypes.contains(screenHandlerType)) {
            registeredScreenTypes.add(screenHandlerType);
            registeredScreens.put(screenHandlerType, screenSupplier);
            return true;
        }
        return false;
    }

    public static int getScreenTab(ScreenHandlerType<? extends ScreenHandler> screenHandlerType) {
        int holdingIndex = registeredScreenTypes.indexOf(screenHandlerType);
        if (holdingIndex != -1)
            return holdingIndex + 1;

        return -1;
    }

    public static int getScreenTab(ScreenHandler screenHandler) {
        if (screenHandler instanceof PlayerScreenHandler)
            return 0;
        return -1;
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
            openInventoryScreenManually();
            targetTab = 0;
        }

        if (targetTab == -1) {
            for (int i = 0; i < registeredScreenTypes.size(); i++) {
                int tab = i + 1;
                if (currentTab != tab && AccessoryTabAssistant.mouseInAccessoryTab(x, y, mouseX, mouseY, tab)) {
                    PootsAdditions.logInfo("Clicked tab " + tab);
                    targetTab = tab;
                    break;
                }
            }
        }

        if (targetTab > 0 && MinecraftClient.getInstance().player.currentScreenHandler != null)
            MinecraftClient.getInstance().player.closeHandledScreen();

        return targetTab;
    }

    public static boolean userOpenHandledScreen(PlayerEntity entity, int tab) {
        //if (entity.currentScreenHandler != null)
        //    entity.currentScreenHandler.close(entity);

        if (tab == 0) {
            return true;
        }
        if (tab > 0 && tab - 1 < registeredScreenTypes.size()) {
            PootsAdditions.logInfo("Attemptig to construct screen on tab " + tab);
            entity.openHandledScreen(registeredScreens.get(registeredScreenTypes.get(tab - 1)));
            return true;
        }
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

}
