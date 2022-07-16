package net.pyerter.pootsadditions.integration.rei.category;

import com.google.common.collect.Lists;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.pyerter.pootsadditions.block.ModBlocks;
import net.pyerter.pootsadditions.integration.REIPootsAdditionsPlugin;
import net.pyerter.pootsadditions.integration.rei.display.EngineeringStationRefineRecipeDisplay;

import java.util.ArrayList;
import java.util.List;

public class EngineeringStationRefineRecipeCategory implements DisplayCategory<EngineeringStationRefineRecipeDisplay> {
    public EngineeringStationRefineRecipeCategory() {

    }

    @Override
    public CategoryIdentifier<? extends EngineeringStationRefineRecipeDisplay> getCategoryIdentifier() {
        return REIPootsAdditionsPlugin.REFINING;
    }

    @Override
    public Text getTitle() {
        return Text.of("Engineering Station Refining");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.ENGINEERING_STATION);
    }

    @Override
    public List<Widget> setupDisplay(EngineeringStationRefineRecipeDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 31, bounds.getCenterY() - 13);
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createRecipeBase(bounds));

        return DisplayCategory.super.setupDisplay(display, bounds);
    }
}
