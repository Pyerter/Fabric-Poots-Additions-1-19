package net.pyerter.pootsadditions.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.block.entity.EngineeringStationEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EngineeringStationRefineRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> ingredients;
    private final Integer hammerCost;

    public EngineeringStationRefineRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems, Integer hammerCost) {
        this.id = id;
        this.output = output;
        this.ingredients = recipeItems;
        this.hammerCost = hammerCost;
        if (ingredients.size() == 1) {
            Arrays.stream(ingredients.get(0).getMatchingStacks()).forEach(stack -> EngineeringStationEntity.acceptedRefiningMaterials.add(stack.getItem()));
        }
    }

    @Override
    // This recipe type will not be shaped, it relies on only the presence
    // of the required ingredients in any slots.
    public boolean matches(SimpleInventory inventory, World world) {
        // end short if client, due to recipeItems not being populated on client
        if (world.isClient())
            return false;

        if (ingredients.size() != 1) {
            PootsAdditions.logDebug("Error while crafting EngineeringStationRefineRecipe - ingredients list too large for recipe ID " + id);
            return false;
        }

        List<Ingredient> requiredIngredients = new ArrayList<>(ingredients.size());
        for (Ingredient ing: ingredients) { requiredIngredients.add(ing); }

        if (inventory.getStack(4).isEmpty() && hammerCost > 0)
            return false;

        return requiredIngredients.get(0).test(inventory.getStack(3));
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true; // ?????
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public Integer getHammerCost() { return hammerCost; }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<EngineeringStationRefineRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "engineering_station_refine";
    }

    public static class Serializer implements RecipeSerializer<EngineeringStationRefineRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "engineering_station_refine";

        @Override
        public EngineeringStationRefineRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            Integer hammerCost = JsonHelper.getInt(json, "hammer_cost");

            return new EngineeringStationRefineRecipe(id, output, inputs, hammerCost);
        }

        @Override
        public EngineeringStationRefineRecipe read(Identifier id, PacketByteBuf buf) {
            Integer hammerCost = buf.readInt();

            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new EngineeringStationRefineRecipe(id, output, inputs, hammerCost);
        }

        @Override
        public void write(PacketByteBuf buf, EngineeringStationRefineRecipe recipe) {
            buf.writeInt(recipe.getHammerCost());
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing: recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }


}
