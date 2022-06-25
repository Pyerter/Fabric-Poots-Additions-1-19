package net.pyerter.pootsadditions.world.feature;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.pyerter.pootsadditions.block.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {
    public static final List<OreFeatureConfig.Target> OVERWORLD_SAPPHIRE_DUST_ORE = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    ModBlocks.SAPPHIRE_DUST_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES,
                    ModBlocks.DEEPSLATE_SAPPHIRE_DUST_ORE.getDefaultState()));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> SAPPHIRE_DUST_ORE =
            ConfiguredFeatures.register("sapphire_dust_ore", Feature.ORE,
                    new OreFeatureConfig(OVERWORLD_SAPPHIRE_DUST_ORE, 5));

}
