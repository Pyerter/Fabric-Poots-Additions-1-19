package net.pyerter.pootsadditions.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.entity.client.BobcatRenderer;
import net.pyerter.pootsadditions.entity.custom.BobcatEntity;

public class ModEntities {
    public static final EntityType<BobcatEntity> BOBCAT = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(PootsAdditions.MOD_ID, "bobcat"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BobcatEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4f, 0.3f)).build());

    public static void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntities.BOBCAT, BobcatEntity.setAttributes());
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.BOBCAT, BobcatRenderer::new);
    }
}
