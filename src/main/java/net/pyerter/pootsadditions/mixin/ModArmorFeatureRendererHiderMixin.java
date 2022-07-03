package net.pyerter.pootsadditions.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.pyerter.pootsadditions.util.IArmorHider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public class ModArmorFeatureRendererHiderMixin <T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

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

    @Shadow
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

    }
}
