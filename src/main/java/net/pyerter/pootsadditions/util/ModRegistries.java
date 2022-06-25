package net.pyerter.pootsadditions.util;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.entity.ModEntities;
import net.pyerter.pootsadditions.entity.custom.BobcatEntity;

public class ModRegistries {

    public static void registerModElements() {
        registerAttributes();
    }

    private static void registerAttributes() {
        PootsAdditions.logInfo("Registering entity attributes");
        FabricDefaultAttributeRegistry.register(ModEntities.BOBCAT, BobcatEntity.setAttributes());
    }
}
