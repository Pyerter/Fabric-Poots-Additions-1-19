package net.pyerter.pootsadditions.item.entity.client;

import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.pyerter.pootsadditions.item.entity.custom.DiamondSwordEntity;

import java.util.Iterator;
import java.util.List;

public class DiamondSwordEntityModel extends EntityModel<DiamondSwordEntity> {

    ItemColors colors;

    DiamondSwordEntityModel() {
        colors = ItemColors.create(new BlockColors());
    }

    @Override
    public void setAngles(DiamondSwordEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        BakedModel model = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(Items.DIAMOND_SWORD); //MinecraftClient.getInstance().getBakedModelManager()
        renderBakedItemModel(model, Items.DIAMOND_SWORD.getDefaultStack(), light, overlay, matrices, vertices);
    }

    private void renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices) {
        Random random = Random.create();
        long l = 42L;
        Direction[] var10 = Direction.values();
        int var11 = var10.length;

        for(int var12 = 0; var12 < var11; ++var12) {
            Direction direction = var10[var12];
            random.setSeed(42L);
            this.renderBakedItemQuads(matrices, vertices, model.getQuads((BlockState)null, direction, random), stack, light, overlay);
        }

        random.setSeed(42L);
        this.renderBakedItemQuads(matrices, vertices, model.getQuads((BlockState)null, (Direction)null, random), stack, light, overlay);
    }

    private void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
        boolean bl = !stack.isEmpty();
        MatrixStack.Entry entry = matrices.peek();
        Iterator var9 = quads.iterator();

        while(var9.hasNext()) {
            BakedQuad bakedQuad = (BakedQuad)var9.next();
            int i = -1;
            if (bl && bakedQuad.hasColor()) {
                i = this.colors.getColor(stack, bakedQuad.getColorIndex());
            }

            float f = (float)(i >> 16 & 255) / 255.0F;
            float g = (float)(i >> 8 & 255) / 255.0F;
            float h = (float)(i & 255) / 255.0F;
            vertices.quad(entry, bakedQuad, f, g, h, light, overlay);
        }

    }
}
