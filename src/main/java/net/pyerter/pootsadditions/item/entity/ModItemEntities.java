package net.pyerter.pootsadditions.item.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.entity.ModEntities;
import net.pyerter.pootsadditions.entity.client.BobcatRenderer;
import net.pyerter.pootsadditions.item.entity.client.DiamondSwordEntityRenderer;
import net.pyerter.pootsadditions.item.entity.custom.DiamondSwordEntity;

public class ModItemEntities {

    public static final EntityType<DiamondSwordEntity> DIAMOND_SWORD_ENTITY = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(PootsAdditions.MOD_ID, "diamond_sword_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, DiamondSwordEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build());

    public static void registerEntityAttributes() {
        EntityRendererRegistry.register(DIAMOND_SWORD_ENTITY, (context) -> { return new DiamondSwordEntityRenderer(context); });
        // FabricDefaultAttributeRegistry.register(DIAMOND_SWORD_ENTITY, DiamondSwordEntity.create);
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(DIAMOND_SWORD_ENTITY, DiamondSwordEntityRenderer::new);
    }

}
