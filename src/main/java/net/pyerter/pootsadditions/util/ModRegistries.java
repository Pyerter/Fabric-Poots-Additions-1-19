package net.pyerter.pootsadditions.util;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.entity.ModEntities;
import net.pyerter.pootsadditions.entity.custom.BobcatEntity;
import net.pyerter.pootsadditions.item.entity.ModItemEntities;

public class ModRegistries {

    public static void registerModElements() {
        registerAttributes();
    }

    private static void registerAttributes() {
        PootsAdditions.logInfo("Registering entity attributes");
        ModEntities.registerEntityAttributes();
        ModItemEntities.registerEntityAttributes();
    }
}
