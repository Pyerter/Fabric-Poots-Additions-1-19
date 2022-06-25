package net.pyerter.pootsadditions.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.pyerter.pootsadditions.PootsAdditions;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup SAPPHIRE = FabricItemGroupBuilder.build(
            new Identifier(PootsAdditions.MOD_ID, "sapphire"), () -> new ItemStack(ModItems.SAPPHIRE_DUST));
}
