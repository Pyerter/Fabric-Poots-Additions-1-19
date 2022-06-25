package net.pyerter.pootsadditions;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.data.DataGenerator;
import net.pyerter.pootsadditions.block.ModBlocks;
import net.pyerter.pootsadditions.block.entity.ModBlockEntities;
import net.pyerter.pootsadditions.item.ModItems;
import net.pyerter.pootsadditions.recipe.ModRecipes;
import net.pyerter.pootsadditions.screen.handlers.ModScreenHandlers;
import net.pyerter.pootsadditions.util.ModRegistries;
import net.pyerter.pootsadditions.world.gen.ModWorldGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

import java.nio.file.Path;

// Very important comment
public class PootsAdditions implements ModInitializer {
	public static final String MOD_ID = "pootsadditions";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModItems.registerPredicateOverrides();

		ModRegistries.registerModElements();

		ModWorldGen.initializeWorldGen();

		ModBlockEntities.registerAllBlockEntities();

		ModRecipes.registerRecipes();

		ModScreenHandlers.registerAllScreenHandlers();

		GeckoLib.initialize();
	}

	public static void logInfo(String message) {
		LOGGER.info(MOD_ID + " --> " + message);
	}

	public static void logDebug(String message) {
		LOGGER.debug(MOD_ID + " --> " + message);
	}
}
