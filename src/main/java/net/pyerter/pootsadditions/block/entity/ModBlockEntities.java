package net.pyerter.pootsadditions.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.block.ModBlocks;

public class ModBlockEntities {
    public static BlockEntityType<TridiBlockEntity> TRIDI;

    public static BlockEntityType<CaptureChamberEntity> CAPTURE_CHAMBER;

    public static BlockEntityType<EngineeringStationEntity> ENGINEERING_STATION;

    public static BlockEntityType<KitchenStoveStationEntity> KITCHEN_STOVE_STATION;

    public static BlockEntityType<FoodPreppingStationEntity> FOOD_PREPPING_STATION;

    public static void registerAllBlockEntities() {
        TRIDI = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(PootsAdditions.MOD_ID, "tridi"),
                FabricBlockEntityTypeBuilder.create(TridiBlockEntity::new, ModBlocks.TRIDI).build(null));

        CAPTURE_CHAMBER = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(PootsAdditions.MOD_ID, "capture_chamber"),
                FabricBlockEntityTypeBuilder.create(CaptureChamberEntity::new, ModBlocks.CAPTURE_CHAMBER).build(null));

        ENGINEERING_STATION = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(PootsAdditions.MOD_ID, "engineering_station"),
                FabricBlockEntityTypeBuilder.create(EngineeringStationEntity::new, ModBlocks.ENGINEERING_STATION).build(null));

        KITCHEN_STOVE_STATION = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(PootsAdditions.MOD_ID, "kitchen_stove_station"),
                FabricBlockEntityTypeBuilder.create(KitchenStoveStationEntity::new, ModBlocks.KITCHEN_STOVE_STATION).build(null));

        FOOD_PREPPING_STATION = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(PootsAdditions.MOD_ID, "food_prepping_station"),
                FabricBlockEntityTypeBuilder.create(FoodPreppingStationEntity::new, ModBlocks.FOOD_PREPPING_STATION).build(null));
    }
}
