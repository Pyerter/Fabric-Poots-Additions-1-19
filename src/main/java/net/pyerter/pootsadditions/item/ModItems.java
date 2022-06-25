package net.pyerter.pootsadditions.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Material;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;
import net.pyerter.pootsadditions.PootsAdditions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pyerter.pootsadditions.entity.ModEntities;
import net.pyerter.pootsadditions.item.custom.*;

public class ModItems {
    public static final Item SAPPHIRE_DUST = registerItem("sapphire_dust",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item STARMETAL_ALLOY_INGOT = registerItem("starmetal_alloy_ingot",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item STARMETAL_COMPOUND_MIX = registerItem("starmetal_compound_mix",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item SAPPHIRE_STAR = registerItem("sapphire_star",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item SAPPHIRE_STAR_BASE = registerItem("sapphire_star_base",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item CELESTIAL_FOCUS_STAFF = registerItem("celestial_focus_staff",
            new CelestialFocusStaff(new FabricItemSettings().group(ModItemGroup.SAPPHIRE).maxCount(1).rarity(Rarity.EPIC)));

    public static final Item SAPPHIRE_SWORD = registerItem("starmetal_sword",
            new StarmetalSword(3, 1f,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE).fireproof()));
    public static final Item STARMETAL_SHOVEL = registerItem("starmetal_shovel",
            new StarmetalShovel(1.5f, -3f,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE).fireproof()));
    public static final Item STARMETAL_PICKAXE = registerItem("starmetal_pickaxe",
            new StarmetalPickaxe(1, -2.8f,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE).fireproof()));
    public static final Item STARMETALE_AXE = registerItem("starmetal_axe",
            new StarmetalAxe(6, -3f,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE).fireproof()));
    public static final Item STARMETAL_HOE = registerItem("starmetal_hoe",
            new StarmetalHoe(-4, 0f,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE).fireproof()));

    public static final Item ENGINEERED_STONE_SWORD = registerItem("engineered_stone_sword",
            new EngineeredSword(3, 1f, ToolMaterials.STONE,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_STONE_SHOVEL = registerItem("engineered_stone_shovel",
            new EngineeredShovel(1.5f, -3f, ToolMaterials.STONE,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_STONE_PICKAXE = registerItem("engineered_stone_pickaxe",
            new EngineeredPickaxe(1, -2.8f, ToolMaterials.STONE,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_STONE_AXE = registerItem("engineered_stone_axe",
            new EngineeredAxe(6, -3f, ToolMaterials.STONE,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_STONE_HOE = registerItem("engineered_stone_hoe",
            new EngineeredHoe(-4, 0f, ToolMaterials.STONE,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item ENGINEERED_IRON_SWORD = registerItem("engineered_iron_sword",
            new EngineeredSword(3, 1f, ToolMaterials.IRON,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_IRON_SHOVEL = registerItem("engineered_iron_shovel",
            new EngineeredShovel(1.5f, -3f, ToolMaterials.IRON,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_IRON_PICKAXE = registerItem("engineered_iron_pickaxe",
            new EngineeredPickaxe(1, -2.8f, ToolMaterials.IRON,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_IRON_AXE = registerItem("engineered_iron_axe",
            new EngineeredAxe(6, -3f, ToolMaterials.IRON,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_IRON_HOE = registerItem("engineered_iron_hoe",
            new EngineeredHoe(-4, 0f, ToolMaterials.IRON,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item ENGINEERED_GOLDEN_SWORD = registerItem("engineered_golden_sword",
            new EngineeredSword(3, 1f, ToolMaterials.GOLD,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_GOLDEN_SHOVEL = registerItem("engineered_golden_shovel",
            new EngineeredShovel(1.5f, -3f, ToolMaterials.GOLD,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_GOLDEN_PICKAXE = registerItem("engineered_golden_pickaxe",
            new EngineeredPickaxe(1, -2.8f, ToolMaterials.GOLD,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_GOLDEN_AXE = registerItem("engineered_golden_axe",
            new EngineeredAxe(6, -3f, ToolMaterials.GOLD,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_GOLDEN_HOE = registerItem("engineered_golden_hoe",
            new EngineeredHoe(-4, 0f, ToolMaterials.GOLD,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item ENGINEERED_DIAMOND_SWORD = registerItem("engineered_diamond_sword",
            new EngineeredSword(3, 1f, ToolMaterials.DIAMOND,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_DIAMOND_SHOVEL = registerItem("engineered_diamond_shovel",
            new EngineeredShovel(1.5f, -3f, ToolMaterials.DIAMOND,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_DIAMOND_PICKAXE = registerItem("engineered_diamond_pickaxe",
            new EngineeredPickaxe(1, -2.8f, ToolMaterials.DIAMOND,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_DIAMOND_AXE = registerItem("engineered_diamond_axe",
            new EngineeredAxe(6, -3f, ToolMaterials.DIAMOND,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_DIAMOND_HOE = registerItem("engineered_diamond_hoe",
            new EngineeredHoe(-4, 0f, ToolMaterials.DIAMOND,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item ENGINEERED_NETHERITE_SWORD = registerItem("engineered_netherite_sword",
            new EngineeredSword(3, 1f, ToolMaterials.NETHERITE,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_NETHERITE_SHOVEL = registerItem("engineered_netherite_shovel",
            new EngineeredShovel(1.5f, -3f, ToolMaterials.NETHERITE,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_NETHERITE_PICKAXE = registerItem("engineered_netherite_pickaxe",
            new EngineeredPickaxe(1, -2.8f, ToolMaterials.NETHERITE,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_NETHERITE_AXE = registerItem("engineered_netherite_axe",
            new EngineeredAxe(6, -3f, ToolMaterials.NETHERITE,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_NETHERITE_HOE = registerItem("engineered_netherite_hoe",
            new EngineeredHoe(-4, 0f, ToolMaterials.NETHERITE,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item ENGINEERED_STARMETAL_SWORD = registerItem("engineered_starmetal_sword",
            new EngineeredSword(3, 1f, ModToolMaterials.STARMETAL,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_STARMETAL_SHOVEL = registerItem("engineered_starmetal_shovel",
            new EngineeredShovel(1.5f, -3f, ModToolMaterials.STARMETAL,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_STARMETAL_PICKAXE = registerItem("engineered_starmetal_pickaxe",
            new EngineeredPickaxe(1, -2.8f, ModToolMaterials.STARMETAL,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_STARMETAL_AXE = registerItem("engineered_starmetal_axe",
            new EngineeredAxe(6, -3f, ModToolMaterials.STARMETAL,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item ENGINEERED_STARMETAL_HOE = registerItem("engineered_starmetal_hoe",
            new EngineeredHoe(-4, 0f, ModToolMaterials.STARMETAL,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item MAKESHIFT_CORE = registerItem("makeshift_core",
            new MakeshiftCore(new FabricItemSettings().group(ModItemGroup.SAPPHIRE).fireproof().maxCount(1)));


    public static final Item BOBCAT_SPAWN_EGG = registerItem("bobcat_spawn_egg",
            new SpawnEggItem(ModEntities.BOBCAT, 0x000000, 0xa8925e,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(PootsAdditions.MOD_ID, name), item);
    }

    public static void registerModItems() {
        PootsAdditions.LOGGER.debug("Registering Mod Items for " + PootsAdditions.MOD_ID);
    }

    public static void registerPredicateOverrides() {
        ModelPredicateProviderRegistry.register(ModItems.MAKESHIFT_CORE, new Identifier("charge"), new MakeshiftCore.CoreChargePredicateProvider(
                (stack) -> MakeshiftCore.getOverridePredicateChargeValue(stack)));
    }
}
