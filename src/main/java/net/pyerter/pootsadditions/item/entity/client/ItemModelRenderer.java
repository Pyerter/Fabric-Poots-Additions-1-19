package net.pyerter.pootsadditions.item.entity.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

public class ItemModelRenderer extends ItemEntityRenderer {
    public enum PlayerEquipStyle {
        NADA,
        BACK,
        BACK_SHEATHED,
        BELT_LEFT,
        BELT_RIGHT,
        BELT_BACK,
        ARM_LEFT,
        ARM_RIGHT
    }

    public final Item item;

    public ItemModelRenderer(EntityRendererFactory.Context context, Item item) {
        super(context);
        this.item = item;
    }

    @Override
    public Identifier getTexture(ItemEntity itemEntity) {
        return new Identifier("minecraft", "textures/item/diamond_sword.png");
    }

    @Override
    public void render(ItemEntity itemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        renderItem(itemEntity, item, f, g, matrixStack, vertexConsumerProvider, i, false);
    }

    public static void renderItem(ItemEntity entity, Item item, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, boolean glint) {
        BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(item);
        ItemStack stack = item.getDefaultStack();

        boolean modelItem = true;
        RenderLayer renderLayer = RenderLayers.getItemLayer(stack, modelItem);
        VertexConsumer vertexConsumer;
        if (!glint) {
            vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
        } else {
            MatrixStack.Entry entry = matrixStack.peek();
            entry.getPositionMatrix().multiply(0.75f);
            vertexConsumer = ItemRenderer.getCompassGlintConsumer(vertexConsumerProvider, renderLayer, entry);
        }

        matrixStack.push();

        matrixStack.translate(0, -0.5, 0); // move item down to rotate about center of item

        transformByStyle(entity, matrixStack, PlayerEquipStyle.BACK_SHEATHED, tickDelta);

        BakedItemModelRenderer.renderBakedItemModel(bakedModel, stack, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumer);

        matrixStack.pop();
    }

    public static void transformByStyle(ItemEntity entity, MatrixStack matrices, PlayerEquipStyle style, float tickDelta) {
        switch (style) {
            default: case BACK: transformBack(entity, matrices, tickDelta, false); break;
            case BACK_SHEATHED: transformBack(entity, matrices, tickDelta, true); break;
            case NADA: break;
        }
    }

    public static void transformBack(ItemEntity entity, MatrixStack matrices, float tickDelta, boolean sheathed) {
        Quaternion rotationYaw = null;
        if (entity.hasVehicle()) {
            //EntityRenderer renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(entity.getVehicle());
            if (entity.getVehicle() instanceof PlayerEntity) {
                // whelp at least now I know how to access the playerRenderer and thus player entity model
                // ;-;
                // && renderer instanceof PlayerEntityRenderer) {
                //PlayerEntityRenderer playerRenderer = (PlayerEntityRenderer) renderer;
                //float h = MathHelper.lerpAngleDegrees(tickDelta, playerEntity.prevBodyYaw, playerEntity.bodyYaw);
                //float j = MathHelper.lerpAngleDegrees(tickDelta, playerEntity.prevHeadYaw, playerEntity.headYaw);
                //float k = j - h;

                PlayerEntity playerEntity = (PlayerEntity) entity.getVehicle();
                float yaw = playerEntity.bodyYaw;
                rotationYaw = new Quaternion(Vec3f.POSITIVE_Y, -yaw, true);
            } else {
                rotationYaw = new Quaternion(Vec3f.POSITIVE_Y, -entity.getVehicle().getBodyYaw(), true);
            }
        } else {
            rotationYaw = new Quaternion(Vec3f.POSITIVE_Y, -entity.getYaw(tickDelta), true);
        }

        if (sheathed) {
            Quaternion rotationPitch = new Quaternion(Vec3f.POSITIVE_X, 180, true);
            rotationYaw.hamiltonProduct(rotationPitch);
        }

        matrices.multiply(rotationYaw);
        matrices.scale(0.75f, 0.75f, 0.75f);
        matrices.translate(-0.5, -1.0, -0.3);
    }
}
