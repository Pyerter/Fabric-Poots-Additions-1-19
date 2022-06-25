package net.pyerter.pootsadditions.entity.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.entity.custom.BobcatEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BobcatRenderer extends GeoEntityRenderer<BobcatEntity> {
    public BobcatRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BobcatModel());
    }

    @Override
    public Identifier getTextureResource(BobcatEntity instance) {
        return new Identifier(PootsAdditions.MOD_ID, "textures/entity/bobcat/bobcat.png");
    }

    @Override
    public RenderLayer getRenderType(BobcatEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        // if baby, is smol
        if (animatable.isBaby()) {
            stack.scale(0.5f, 0.5f, 0.5f);
        }
        // if not baby, default size

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
