package net.pyerter.pootsadditions.recipe;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pyerter.pootsadditions.PootsAdditions;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(PootsAdditions.MOD_ID, TridiRecipe.Serializer.ID),
                TridiRecipe.Serializer.INSTANCE);

        Registry.register(Registry.RECIPE_TYPE, new Identifier(PootsAdditions.MOD_ID, TridiRecipe.Type.ID),
                TridiRecipe.Type.INSTANCE);
    }

}
