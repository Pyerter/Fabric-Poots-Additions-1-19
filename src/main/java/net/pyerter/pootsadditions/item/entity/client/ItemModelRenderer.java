package net.pyerter.pootsadditions.item.entity.client;

import com.eliotlash.mclib.math.functions.limit.Min;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.pyerter.pootsadditions.item.custom.accessory.AccessoryItem;

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

    public static void renderItem(PlayerEntity entity, ItemStack stack, Item item, PlayerEquipStyle equipStyle, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(item);
        bakedModel = bakedModel.getOverrides().apply(bakedModel, stack, MinecraftClient.getInstance().world, entity, 0);
        if (stack == null)
            stack = item.getDefaultStack();

        boolean glint = stack.hasGlint();
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

        ItemStack referenceStack = stack;
        BakedModel referenceModel = bakedModel;
        transformByStyle(entity, matrixStack, equipStyle, tickDelta,
                () -> BakedItemModelRenderer.renderBakedItemModel(referenceModel, referenceStack, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumer)
        );

        matrixStack.pop();
    }

    public static boolean renderItemViaVanilla(MatrixStack matrices, PlayerEntity entity, ItemStack stack, int light, float tickDelta, VertexConsumerProvider vertexConsumerProvider) {
        if (!(stack.getItem() instanceof AccessoryItem))
            return false;

        BakedModel model = MinecraftClient.getInstance().getItemRenderer().getModels().getModel(stack);
        BakedModel referenceModel = model.getOverrides().apply(model, stack, MinecraftClient.getInstance().world, entity, 0);
        Runnable renderCall = () -> MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, false, matrices, vertexConsumerProvider, light, 0, referenceModel);
        ItemModelRenderer.transformByStyle(entity, matrices, ((AccessoryItem) stack.getItem()).getEquipStyle(), tickDelta, renderCall);
        return true;
    }

    public static void transformByStyle(ItemEntity entity, MatrixStack matrices, PlayerEquipStyle style, float tickDelta) {
        switch (style) {
            case BACK: transformBack(entity, matrices, tickDelta, false); break;
            default: case BACK_SHEATHED: transformBack(entity, matrices, tickDelta, true); break;
            case NADA: break;
        }
    }

    public static void transformByStyle(PlayerEntity entity, MatrixStack matrices, PlayerEquipStyle style, float tickDelta, Runnable renderCall) {
        switch (style) {
            case BACK: transformBack(entity, matrices, tickDelta, false, renderCall); break;
            default: case BACK_SHEATHED: transformBack(entity, matrices, tickDelta, true, renderCall); break;
            case NADA: break;
        }
    }

    public static void transformBack(ItemEntity entity, MatrixStack matrices, float tickDelta, boolean sheathed) {
        Quaternion rotationYaw = null;
        if (entity.hasVehicle()) {
            if (entity.getVehicle() instanceof PlayerEntity) {
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

    public static void transformBack(PlayerEntity entity, MatrixStack matrices, float tickDelta, boolean sheathed, Runnable renderCall) {
        matrices.push();

        Quaternion rotationYaw = null;
        float yaw = entity.bodyYaw;
        rotationYaw = new Quaternion(Vec3f.POSITIVE_X, 180, true);

        if (!sheathed) {
            matrices.scale(0.75f, 0.75f, 0.75f);
            matrices.translate(0, 0, -0.35);
        } else {
            matrices.scale(0.75f, 0.75f, 0.75f);
            matrices.translate(0, 0.5, 0.35);
            matrices.multiply(rotationYaw);
        }

        renderCall.run();

        matrices.pop();
    }
}
