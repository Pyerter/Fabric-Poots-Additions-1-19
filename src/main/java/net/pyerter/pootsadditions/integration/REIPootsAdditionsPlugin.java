package net.pyerter.pootsadditions.integration;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.plugins.REIPlugin;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.integration.rei.display.EngineeringStationRefineRecipeDisplay;
import org.jetbrains.annotations.NotNull;

public class REIPootsAdditionsPlugin implements REIPlugin {
    public static final CategoryIdentifier<EngineeringStationRefineRecipeDisplay> REFINING = CategoryIdentifier.of(PootsAdditions.MOD_ID, "refining");


    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }

    @Override
    public Class getPluginProviderClass() {
        return null;
    }
}
