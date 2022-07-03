package net.pyerter.pootsadditions.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.pyerter.pootsadditions.PootsAdditions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pyerter.pootsadditions.entity.ModEntities;
import net.pyerter.pootsadditions.item.custom.*;
import net.pyerter.pootsadditions.item.custom.engineering.*;

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
            new EngineeredSword(3, -2.4f, ToolMaterials.STONE,
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
            new EngineeredSword(3, -2.4f, ToolMaterials.IRON,
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
            new EngineeredSword(3, -2.4f, ToolMaterials.GOLD,
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
            new EngineeredSword(3, -2.4f, ToolMaterials.DIAMOND,
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
            new EngineeredSword(3, -2.4f, ToolMaterials.NETHERITE,
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

    public static final Item ENGINEERS_TRUSTY_HAMMER = registerItem("engineers_trusty_hammer",
            new EngineersTrustyHammer(3, -2f, new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item MALICE_SCYTHE = registerItem("malice_scythe",
            new MaliceScythe(5f, -1f, new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item ENGINEERS_BLUPRINT = registerItem("engineers_blueprint",
            new EngineersBlueprintItem(new FabricItemSettings().group(ModItemGroup.SAPPHIRE).maxCount(1)));
    public static final Item ENGINEERS_REPAIR_KIT = registerItem("engineers_repair_kit",
            new EngineersRepairKit(new FabricItemSettings().group(ModItemGroup.SAPPHIRE).maxCount(16)));

    public static final Item SCREW_ITEM = registerItem("screw_item",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item COPPER_WIRE_ITEM = registerItem("copper_wire_item",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item POUNDED_KELP = registerItem("pounded_kelp_item",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item STICKY_SLUDGE_ITEM = registerItem("sticky_sludge_item",
            new DescriptiveItem(new FabricItemSettings().group(ModItemGroup.SAPPHIRE).maxCount(256),
                    DescriptiveItem.DescriptionBuilder.start().add("It's uncomfortably", Formatting.GRAY)
                            .add("STICKY...", Formatting.DARK_GREEN, Formatting.ITALIC).build()));
    public static final Item PYRIFERA_INSULATION_GOO = registerItem("pyrifera_insulation_goo",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE).recipeRemainder(STICKY_SLUDGE_ITEM)));

    public static final Item MAKESHIFT_CORE = registerItem("makeshift_core",
            new MakeshiftCore(new FabricItemSettings().group(ModItemGroup.SAPPHIRE).fireproof().maxCount(1)));

    public static final Item PAUTSCH_ITEM = registerItem("pautsch_item",
            new PautschItem(new FabricItemSettings().group(ModItemGroup.SAPPHIRE).fireproof().maxCount(1)));


    public static final Item BOBCAT_SPAWN_EGG = registerItem("bobcat_spawn_egg",
            new SpawnEggItem(ModEntities.BOBCAT, 0x000000, 0xa8925e,
                    new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));

    public static final Item ROLLING_PIN_ITEM = registerItem("rolling_pin_item",
            new RecipeWithstandingItem(new FabricItemSettings().group(ModItemGroup.SAPPHIRE).maxCount(1)));
    public static final Item COOKIE_CUTTER_ITEM = registerItem("cookie_cutter_item",
            new RecipeWithstandingItem(new FabricItemSettings().group(ModItemGroup.SAPPHIRE).maxCount(1)));
    public static final Item BREAD_KNIFE_ITEM = registerItem("bread_knife_item",
            new BreadKnifeItem(new FabricItemSettings().group(ModItemGroup.SAPPHIRE).maxCount(1)));

    public static final Item SLICED_BREAD_ITEM = registerItem("sliced_bread_item",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE).food(
                    (new FoodComponent.Builder()).hunger(2).snack().build())));
    public static final Item DOUGH_ITEM = registerItem("dough_item",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item FLATTENED_DOUGH_ITEM = registerItem("flattened_dough_item",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item COOKIE_DOUGH_ITEM = registerItem("cookie_dough_item",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE)));
    public static final Item CHOCOLATE_CHIP_COOKIE_DOUGH_ITEM = registerItem("chocolate_chip_cookie_dough_item",
            new Item(new FabricItemSettings().group(ModItemGroup.SAPPHIRE).food(
                    (new FoodComponent.Builder()).hunger(2).snack().build())));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(PootsAdditions.MOD_ID, name), item);
    }

    public static void registerModItems() {
        PootsAdditions.LOGGER.debug("Registering Mod Items for " + PootsAdditions.MOD_ID);
    }

    public static void registerPredicateOverrides() {
        ModelPredicateProviderRegistry.register(ModItems.MAKESHIFT_CORE, new Identifier("charge"), new AbstractPowerCore.ChargePredicateProvider(
                (stack, chargeable) -> chargeable.getOverridePredicateChargeValue(stack)));
        ModelPredicateProviderRegistry.register(ModItems.MALICE_SCYTHE, new Identifier("charge"), new AbstractPowerCore.ChargePredicateProvider(
                (stack, chargeable) -> chargeable.getOverridePredicateChargeValue(stack)));
    }
}
