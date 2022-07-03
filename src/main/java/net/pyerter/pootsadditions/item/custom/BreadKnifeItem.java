package net.pyerter.pootsadditions.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.pyerter.pootsadditions.item.RecipeWithstandingItem;
import net.pyerter.pootsadditions.util.IItemSpecialRecipeRemainder;

public class BreadKnifeItem extends SwordItem implements IItemSpecialRecipeRemainder {

    public BreadKnifeItem(Settings settings) {
        super(ToolMaterials.IRON, 0, 10, settings);
    }

    @Override
    public Item getSpecialRecipeRemainder() {
        return this;
    }
}
