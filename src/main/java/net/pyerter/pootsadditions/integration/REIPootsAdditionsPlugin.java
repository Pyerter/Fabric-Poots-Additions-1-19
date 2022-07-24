package net.pyerter.pootsadditions.integration;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.plugins.REIPlugin;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.integration.rei.REIPootsAdditionsPluginProvider;
import net.pyerter.pootsadditions.integration.rei.display.EngineeringStationRefineRecipeDisplay;
import net.pyerter.pootsadditions.integration.rei.display.TridiRecipeDisplay;
import org.jetbrains.annotations.NotNull;

public class REIPootsAdditionsPlugin implements REIPlugin {
    public static final CategoryIdentifier<EngineeringStationRefineRecipeDisplay> REFINING = CategoryIdentifier.of(PootsAdditions.MOD_ID, "refining");
    public static final CategoryIdentifier<TridiRecipeDisplay> TRIDI = CategoryIdentifier.of(PootsAdditions.MOD_ID, "tridi_display");


    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }

    @Override
    public Class getPluginProviderClass() {
        return REIPootsAdditionsPluginProvider.class;
    }
}
