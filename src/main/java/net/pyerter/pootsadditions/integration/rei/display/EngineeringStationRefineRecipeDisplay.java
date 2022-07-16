package net.pyerter.pootsadditions.integration.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.util.Identifier;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.integration.REIPootsAdditionsPlugin;
import net.pyerter.pootsadditions.recipe.EngineeringStationRefineRecipe;

import java.util.List;

public class EngineeringStationRefineRecipeDisplay extends BasicDisplay {
    public EngineeringStationRefineRecipeDisplay(EngineeringStationRefineRecipe recipe) {
        super(null, null);
    }

    public EngineeringStationRefineRecipeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return REIPootsAdditionsPlugin.REFINING;
    }
}
