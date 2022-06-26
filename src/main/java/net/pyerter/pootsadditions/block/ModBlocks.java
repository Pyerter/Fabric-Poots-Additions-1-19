package net.pyerter.pootsadditions.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.block.custom.CaptureChamberBlock;
import net.pyerter.pootsadditions.block.custom.EngineeringStationBlock;
import net.pyerter.pootsadditions.block.custom.TridiBlock;
import net.pyerter.pootsadditions.block.entity.CaptureChamberEntity;
import net.pyerter.pootsadditions.item.ModItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final Block SPIRAL_CUBE_BLOCK = registerBlock("spiral_cube_block",
            new Block(FabricBlockSettings.of(Material.METAL).luminance(6).strength(4f).requiresTool()),
            ModItemGroup.SAPPHIRE);

    public static final Block SAPPHIRE_DUST_ORE = registerBlock("sapphire_dust_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(3f).requiresTool(), UniformIntProvider.create(4, 8)),
            ModItemGroup.SAPPHIRE);

    public static final Block DEEPSLATE_SAPPHIRE_DUST_ORE = registerBlock("deepslate_sapphire_dust_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(3f).requiresTool(), UniformIntProvider.create(4, 8)),
            ModItemGroup.SAPPHIRE);

    public static final Block TRIDI = registerBlock("tridi",
            new TridiBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()), ModItemGroup.SAPPHIRE);

    public static final Block CAPTURE_CHAMBER = registerBlock("capture_chamber",
            new CaptureChamberBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()), ModItemGroup.SAPPHIRE);

    public static final Block ENGINEERING_STATION = registerBlock("engineering_station",
            new EngineeringStationBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()), ModItemGroup.SAPPHIRE);

    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(PootsAdditions.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {
        return Registry.register(Registry.ITEM, new Identifier(PootsAdditions.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerModBlocks() {
        PootsAdditions.logDebug("Registering ModBlocks");

    }
}
