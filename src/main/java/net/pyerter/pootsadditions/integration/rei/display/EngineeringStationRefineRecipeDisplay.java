package net.pyerter.pootsadditions.integration.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.util.Identifier;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.integration.REIPootsAdditionsPlugin;
import net.pyerter.pootsadditions.recipe.EngineeringStationRefineRecipe;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EngineeringStationRefineRecipeDisplay extends BasicDisplay {
    public EngineeringStationRefineRecipeDisplay(EngineeringStationRefineRecipe recipe) {
        super(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getOutput())),
                Optional.ofNullable(recipe.getId()));
    }

    public EngineeringStationRefineRecipeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return REIPootsAdditionsPlugin.REFINING;
    }

    public static DisplaySerializer<TridiRecipeDisplay> getSerializer() {
        return BasicDisplay.Serializer.ofSimple(TridiRecipeDisplay::new);
    }
}
