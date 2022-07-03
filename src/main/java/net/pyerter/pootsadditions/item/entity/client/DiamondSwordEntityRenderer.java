package net.pyerter.pootsadditions.item.entity.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.pyerter.pootsadditions.item.entity.ModItemEntities;

public class DiamondSwordEntityRenderer extends ItemEntityRenderer {
    DiamondSwordEntityModel model;

    public DiamondSwordEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        model = new DiamondSwordEntityModel();
    }

    @Override
    public Identifier getTexture(ItemEntity itemEntity) {
        return new Identifier("minecraft", "textures/item/diamond_sword.png");
    }

    @Override
    public void render(ItemEntity itemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(Items.DIAMOND_SWORD);
        ItemStack stack = Items.DIAMOND_SWORD.getDefaultStack();
        int light = i;
        boolean someBool = true;
        RenderLayer renderLayer = RenderLayers.getItemLayer(stack, someBool);
        MatrixStack.Entry entry = matrixStack.peek();
        entry.getPositionMatrix().multiply(0.75f);
        VertexConsumer vertexConsumer = ItemRenderer.getCompassGlintConsumer(vertexConsumerProvider, renderLayer, entry);
        // BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices
        model.renderBakedItemModel(bakedModel, stack, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumer);
    }
}
