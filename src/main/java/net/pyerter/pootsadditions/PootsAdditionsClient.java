package net.pyerter.pootsadditions;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.pyerter.pootsadditions.block.ModBlocks;
import net.pyerter.pootsadditions.entity.ModEntities;
import net.pyerter.pootsadditions.entity.client.BobcatRenderer;
import net.pyerter.pootsadditions.screen.*;
import net.pyerter.pootsadditions.screen.handlers.ModScreenHandlers;

public class PootsAdditionsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TRIDI, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CAPTURE_CHAMBER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ENGINEERING_STATION, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.KITCHEN_STOVE_STATION, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putItem(Items.DIAMOND_SWORD, RenderLayer.getCutout());

        KeyBindingRegistryImpl.registerKeyBinding(new KeyBinding("key.pootsadditions.combat_key", InputUtil.GLFW_KEY_G, "PootsAdditions"));

        EntityRendererRegistry.register(ModEntities.BOBCAT, BobcatRenderer::new);

        ScreenRegistry.register(ModScreenHandlers.TRIDI_SCREEN_HANDLER, TridiScreen::new);
        ScreenRegistry.register(ModScreenHandlers.CAPTURE_CHAMBER_SCREEN_HANDLER, CaptureChamberScreen::new);
        ScreenRegistry.register(ModScreenHandlers.PAUTSCH_ITEM_SCREEN_HANDLER, PautschItemScreen::new);
        ScreenRegistry.register(ModScreenHandlers.ENGINEERING_STATION_SCREEN_HANDLER, EngineeringStationScreen::new);
        ScreenRegistry.register(ModScreenHandlers.KITCHEN_STOVE_STATION_SCREEN_HANDLER, KitchenStoveStationScreen::new);
        ScreenRegistry.register(ModScreenHandlers.FOOD_PREPPING_STATION_SCREEN_HANDLER, FoodPreppingStationScreen::new);
    }
}
