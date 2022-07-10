package net.pyerter.pootsadditions.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.pyerter.pootsadditions.item.custom.accessory.AccessoryItem;
import net.pyerter.pootsadditions.item.entity.client.ItemModelRenderer;
import net.pyerter.pootsadditions.item.inventory.AccessoriesInventory;
import net.pyerter.pootsadditions.item.inventory.IAccessoriesInventory;
import net.pyerter.pootsadditions.util.IArmorHider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ModArmorFeatureRendererHiderMixin <T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    public ModArmorFeatureRendererHiderMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method="renderArmor", at=@At("HEAD"), cancellable = true)
    void onRenderArmorCall(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo info) {
        if (entity instanceof IArmorHider) {
            IArmorHider armorHider = (IArmorHider) entity;
            if (!IArmorHider.getArmorVisibleByEquipSlot(armorHider, armorSlot)) {
                info.cancel();
            }
        }
    }

    @Inject(method="render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at=@At(value="RETURN"))
    public void onRenderArmor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo info) {
        // Other calls that appear above this call:
        // this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.HEAD, i, this.getArmor(EquipmentSlot.HEAD));
        if (livingEntity instanceof PlayerEntity) {
            PlayerEntity entity = (PlayerEntity) livingEntity;
            AccessoriesInventory accessoriesInventory = ((IAccessoriesInventory) entity).getAccessoriesInventory();
            ItemStack backStack = accessoriesInventory.getBackAccessorySlot();
            float tickDelta = g;
            if (!backStack.isEmpty() && backStack.getItem() instanceof AccessoryItem) {
                ItemModelRenderer.renderItemViaVanilla(matrixStack, entity, backStack, i, tickDelta, vertexConsumerProvider);
            }
        }
    }
}
