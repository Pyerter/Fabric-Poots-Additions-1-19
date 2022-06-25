package net.pyerter.pootsadditions.world.feature;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.heightprovider.TrapezoidHeightProvider;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

public class ModPlacedFeatures {
    // 7 veins per chunk, from y = [-40, 0], most likely to be found at y = [-22, -18]
    public static final RegistryEntry<PlacedFeature> SAPPHIRE_DUST_ORE_PLACED = PlacedFeatures.register("sapphire_dust_ore_placed",
            ModConfiguredFeatures.SAPPHIRE_DUST_ORE, ModOreFeatures.modifiersWithCount(7,
                    HeightRangePlacementModifier.of(TrapezoidHeightProvider.create(YOffset.aboveBottom(20), YOffset.aboveBottom(60), 4)))); //HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-80), YOffset.aboveBottom(10))

}
