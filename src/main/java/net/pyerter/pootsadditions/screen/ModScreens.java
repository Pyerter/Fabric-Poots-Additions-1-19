package net.pyerter.pootsadditions.screen;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.pyerter.pootsadditions.item.inventory.IAccessoriesInventory;
import net.pyerter.pootsadditions.screen.factories.AccessoryInventoryNamedScreenHandlerFactory;
import net.pyerter.pootsadditions.screen.handlers.AccessoryInventoryScreenHandler;
import net.pyerter.pootsadditions.screen.handlers.ModScreenHandlers;

public class ModScreens {

    public static void registerScreens() {
        ScreenRegistry.register(ModScreenHandlers.TRIDI_SCREEN_HANDLER, TridiScreen::new);
        ScreenRegistry.register(ModScreenHandlers.CAPTURE_CHAMBER_SCREEN_HANDLER, CaptureChamberScreen::new);
        ScreenRegistry.register(ModScreenHandlers.PAUTSCH_ITEM_SCREEN_HANDLER, PautschItemScreen::new);
        ScreenRegistry.register(ModScreenHandlers.ENGINEERING_STATION_SCREEN_HANDLER, EngineeringStationScreen::new);
        ScreenRegistry.register(ModScreenHandlers.KITCHEN_STOVE_STATION_SCREEN_HANDLER, KitchenStoveStationScreen::new);
        ScreenRegistry.register(ModScreenHandlers.FOOD_PREPPING_STATION_SCREEN_HANDLER, FoodPreppingStationScreen::new);
        ScreenRegistry.register(ModScreenHandlers.ACCESSORIES_INVENTORY_SCREEN_HANDLER, AccessoryInventoryScreen::new);
    }

    public static void registerAccessoryTabScreens() {
        AccessoryTabAssistant.tryRegisterScreens(ModScreenHandlers.ACCESSORIES_INVENTORY_SCREEN_HANDLER, new AccessoryInventoryNamedScreenHandlerFactory());
    }

}
