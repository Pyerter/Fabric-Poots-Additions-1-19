package net.pyerter.pootsadditions.integration.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.display.Display;
import net.pyerter.pootsadditions.integration.rei.category.EngineeringStationRefineRecipeCategory;

public class REIPootsAdditionsClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {

        registry.add(
                new EngineeringStationRefineRecipeCategory()
        );

    }
}
