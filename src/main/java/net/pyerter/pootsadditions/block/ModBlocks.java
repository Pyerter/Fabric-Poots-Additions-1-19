package net.pyerter.pootsadditions.block;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.block.custom.*;
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

    public static final Block GARLIC_CROP_BLOCK = registerOnlyBlock("garlic_crop_block",
            new GarlicCropBlock(FabricBlockSettings.copy(Blocks.WHEAT)));

    public static final Block TRIDI = registerBlock("tridi",
            new TridiBlock(FabricBlockSettings.of(Material.METAL).nonOpaque().strength(3f).requiresTool()), ModItemGroup.SAPPHIRE);

    public static final Block CAPTURE_CHAMBER = registerBlock("capture_chamber",
            new CaptureChamberBlock(FabricBlockSettings.of(Material.METAL).nonOpaque().strength(2f).requiresTool()), ModItemGroup.SAPPHIRE);
    public static final Block CAPTURE_CHAMBER_PROVIDER = registerBlock("capture_chamber_provider",
            new CaptureChamberProviderBlock(FabricBlockSettings.of(Material.REDSTONE_LAMP).nonOpaque().strength(2f).requiresTool()), ModItemGroup.SAPPHIRE);

    public static final Block ENGINEERING_STATION = registerBlock("engineering_station",
            new EngineeringStationBlock(FabricBlockSettings.of(Material.METAL).nonOpaque().strength(2f).requiresTool()), ModItemGroup.SAPPHIRE);

    public static final Block KITCHEN_STOVE_STATION = registerBlock("kitchen_stove_station",
            new KitchenStoveStation(FabricBlockSettings.of(Material.METAL).nonOpaque().luminance(state -> {
                if (state.get(KitchenStoveStation.OVEN_LIT) == true)
                    return 14;
                if (state.get(KitchenStoveStation.STOVE_LIT) == true)
                    return 7;
                return 0;
            }).strength(2f).requiresTool()),
            ModItemGroup.SAPPHIRE);

    public static final Block FOOD_PREPPING_STATION = registerBlock("food_prepping_station",
            new FoodPreppingStation(FabricBlockSettings.of(Material.WOOD).nonOpaque().strength(2f).requiresTool()), ModItemGroup.SAPPHIRE);

    public static final Block REDSTONE_MEMORIZER_BLOCK = registerBlock("redstone_memorizer_block",
            new RedstoneMemorizerBlock(FabricBlockSettings.of(Material.REDSTONE_LAMP).luminance(RedstoneMemorizerBlock::calculateLuminance).solidBlock((a, b, c) -> false).strength(1f)), ModItemGroup.SAPPHIRE);

    private static Block registerOnlyBlock(String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(PootsAdditions.MOD_ID, name), block);
    }

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

    public static void registerModBlockRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TRIDI, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CAPTURE_CHAMBER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CAPTURE_CHAMBER_PROVIDER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ENGINEERING_STATION, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.KITCHEN_STOVE_STATION, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FOOD_PREPPING_STATION, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GARLIC_CROP_BLOCK, RenderLayer.getCutout());
    }
}
