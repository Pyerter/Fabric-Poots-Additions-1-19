package net.pyerter.pootsadditions.item.entity.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.Identifier;

public class DiamondSwordEntityRenderer extends ItemEntityRenderer {
    public DiamondSwordEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ItemEntity itemEntity) {
        return new Identifier("minecraft", "textures/item/diamond_sword.png");
    }

    @Override
    public void render(ItemEntity itemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(itemEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
