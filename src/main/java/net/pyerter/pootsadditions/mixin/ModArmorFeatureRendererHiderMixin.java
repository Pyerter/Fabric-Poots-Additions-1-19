package net.pyerter.pootsadditions.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.pyerter.pootsadditions.item.custom.armor.AccessoryArmorItem;
import net.pyerter.pootsadditions.item.custom.accessory.AccessoryItem;
import net.pyerter.pootsadditions.item.custom.armor.ModArmorItem;
import net.pyerter.pootsadditions.item.entity.client.ItemModelRenderer;
import net.pyerter.pootsadditions.item.inventory.AccessoriesInventory;
import net.pyerter.pootsadditions.item.inventory.IAccessoriesInventory;
import net.pyerter.pootsadditions.util.IArmorHider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ModArmorFeatureRendererHiderMixin <T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    public ModArmorFeatureRendererHiderMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method="renderArmor", at=@At("HEAD"), cancellable = true)
    void onRenderArmorCall(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo info) {
        /*if (isArmorHidden(entity, armorSlot)) {
            info.cancel();
        }*/
    }

    public boolean isArmorHidden(T entity, EquipmentSlot armorSlot) {
        if (entity instanceof IArmorHider) {
            IArmorHider armorHider = (IArmorHider) entity;
            if (!IArmorHider.getArmorVisibleByEquipSlot(armorHider, armorSlot)) {
                return true;
            }
        }
        return false;
    }

    public ItemStack getPlayerAccessoryArmor(T entity, EquipmentSlot armorSlot) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            ItemStack armorStack = ((IAccessoriesInventory)player).getAccessoriesInventory().getArmorSlot(armorSlot);
            return armorStack;
        }
        return ItemStack.EMPTY;
    }

    public boolean isItemStackSlimArmor(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof AccessoryArmorItem) {
            AccessoryArmorItem accessoryArmorItem = (AccessoryArmorItem) stack.getItem();
            return accessoryArmorItem.isSlim();
        }
        return false;
    }

    public boolean isCurrentArmorSlim(T entity) {
        EquipmentSlot armorSlot = EquipmentSlot.CHEST;
        boolean armorHidden = isArmorHidden(entity, armorSlot);
        ItemStack armorStack = armorHidden ? getPlayerAccessoryArmor(entity, armorSlot) : entity.getEquippedStack(armorSlot);
        return isItemStackSlimArmor(armorStack);
    }

    @Inject(method="render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at=@At(value="RETURN"))
    public void onRenderArmorEnd(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo info) {
        // Other calls that appear above this call:
        // this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.HEAD, i, this.getArmor(EquipmentSlot.HEAD));
        if (livingEntity instanceof PlayerEntity) {
            PlayerEntity entity = (PlayerEntity) livingEntity;
            float tickDelta = g;

            boolean hasChestplate = false;
            ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.CHEST);
            //if (itemStack.getItem() instanceof ArmorItem && !isArmorHidden(livingEntity, EquipmentSlot.CHEST)) {
            //    hasChestplate = true;
            //}
            hasChestplate = !isCurrentArmorSlim(livingEntity);
            AccessoriesInventory accessoriesInventory = ((IAccessoriesInventory) entity).getAccessoriesInventory();

            ItemStack backStack = accessoriesInventory.getBackAccessorySlot();
            if (!backStack.isEmpty() && backStack.getItem() instanceof AccessoryItem) {
                ItemModelRenderer.renderItemViaVanilla(matrixStack, entity, backStack, i, tickDelta, vertexConsumerProvider, hasChestplate);
            }

            ItemStack beltStack = accessoriesInventory.getBeltAccessorySlot();
            if (!beltStack.isEmpty() && beltStack.getItem() instanceof AccessoryItem) {
                ItemModelRenderer.renderItemViaVanilla(matrixStack, entity, beltStack, i, tickDelta, vertexConsumerProvider, hasChestplate);
            }

            ((IAccessoriesInventory)entity).onEndRenderingArmor();
        }
    }

    // used to let the player entity know to return the proper armor slot
    @Inject(method="render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at=@At(value="HEAD"))
    public void onRenderArmorStart(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo info) {
        if (livingEntity instanceof PlayerEntity) {
            PlayerEntity entity = (PlayerEntity)livingEntity;
            ((IAccessoriesInventory) entity).onStartRenderingArmor();
        }
    }
}
