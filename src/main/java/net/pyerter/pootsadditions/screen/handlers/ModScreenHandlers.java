package net.pyerter.pootsadditions.screen.handlers;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.block.entity.EngineeringStationEntity;

public class ModScreenHandlers {
    public static ScreenHandlerType<TridiScreenHandler> TRIDI_SCREEN_HANDLER;

    public static ScreenHandlerType<CaptureChamberScreenHandler> CAPTURE_CHAMBER_SCREEN_HANDLER;

    public static ScreenHandlerType<PautschItemScreenHandler> PAUTSCH_ITEM_SCREEN_HANDLER;

    public static ScreenHandlerType<EngineeringStationScreenHandler> ENGINEERING_STATION_SCREEN_HANDLER;

    public static ScreenHandlerType<KitchenStoveStationScreenHandler> KITCHEN_STOVE_STATION_SCREEN_HANDLER;

    public static void registerAllScreenHandlers() {
        TRIDI_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(PootsAdditions.MOD_ID, "tridi"), TridiScreenHandler::new);
        CAPTURE_CHAMBER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(PootsAdditions.MOD_ID, "capture_chamber"), CaptureChamberScreenHandler::new);
        PAUTSCH_ITEM_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(PootsAdditions.MOD_ID, "pautsch_item"), PautschItemScreenHandler::new);
        ENGINEERING_STATION_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(PootsAdditions.MOD_ID, "engineering_station"), EngineeringStationScreenHandler::new);
        KITCHEN_STOVE_STATION_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(PootsAdditions.MOD_ID, "kitchen_stove_station"), KitchenStoveStationScreenHandler::new);
    }
}
