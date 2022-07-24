package net.pyerter.pootsadditions.integration.rei;

import me.shedaniel.rei.api.client.entry.renderer.EntryRendererRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.display.Display;
import net.minecraft.recipe.RecipeType;
import net.pyerter.pootsadditions.integration.rei.category.EngineeringStationRefineRecipeCategory;
import net.pyerter.pootsadditions.integration.rei.category.TridiRecipeCategory;
import net.pyerter.pootsadditions.integration.rei.display.TridiRecipeDisplay;
import net.pyerter.pootsadditions.recipe.TridiRecipe;

public class REIPootsAdditionsClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {

        registry.add(new TridiRecipeCategory());

    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {

        registry.registerRecipeFiller(TridiRecipe.class, TridiRecipe.Type.INSTANCE, TridiRecipeDisplay::new);

    }
}
