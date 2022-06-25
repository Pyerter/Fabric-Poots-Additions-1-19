package net.pyerter.pootsadditions.entity.client;

import net.minecraft.util.Identifier;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.entity.custom.BobcatEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BobcatModel extends AnimatedGeoModel<BobcatEntity> {
    @Override
    public Identifier getModelResource(BobcatEntity object) {
        return new Identifier(PootsAdditions.MOD_ID, "geo/bobcat.geo.json");
    }

    @Override
    public Identifier getTextureResource(BobcatEntity object) {
        return new Identifier(PootsAdditions.MOD_ID, "textures/entity/bobcat/bobcat.png");
    }

    @Override
    public Identifier getAnimationResource(BobcatEntity animatable) {
        return new Identifier(PootsAdditions.MOD_ID, "animations/bobcat.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(BobcatEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180f));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180f));
        }
    }
}
