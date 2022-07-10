package net.pyerter.pootsadditions;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.pyerter.pootsadditions.block.ModBlocks;
import net.pyerter.pootsadditions.entity.ModEntities;
import net.pyerter.pootsadditions.entity.client.BobcatRenderer;
import net.pyerter.pootsadditions.item.entity.ModItemEntities;
import net.pyerter.pootsadditions.particle.ModParticles;
import net.pyerter.pootsadditions.screen.*;
import net.pyerter.pootsadditions.screen.handlers.ModScreenHandlers;
import net.pyerter.pootsadditions.screen.input.ModInputs;

public class PootsAdditionsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        // Register renderers
        ModBlocks.registerModBlockRenderLayers();
        ModEntities.registerEntityRenderers();
        ModItemEntities.registerEntityRenderers();

        // Register other visual generators
        ModParticles.registerParticleFactories();

        // Register screens
        ModScreens.registerScreens();

        // Register keybindings
        ModInputs.registerKeyBindings();
    }
}
