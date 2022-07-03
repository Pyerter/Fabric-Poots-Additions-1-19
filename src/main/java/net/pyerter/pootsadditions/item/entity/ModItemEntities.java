package net.pyerter.pootsadditions.item.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.item.entity.client.ItemModelRenderer;
import net.pyerter.pootsadditions.item.entity.custom.WearableItemEntity;

public class ModItemEntities {

    public static final EntityType<WearableItemEntity> DIAMOND_SWORD_ENTITY = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(PootsAdditions.MOD_ID, "diamond_sword_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, WearableItemEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build());

    public static void registerEntityAttributes() {
        // FabricDefaultAttributeRegistry.register(DIAMOND_SWORD_ENTITY, DiamondSwordEntity.create);
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(DIAMOND_SWORD_ENTITY, (context) -> { return new ItemModelRenderer(context, Items.DIAMOND_SWORD); });
    }

}
