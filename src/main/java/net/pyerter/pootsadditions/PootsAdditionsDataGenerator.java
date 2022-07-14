package net.pyerter.pootsadditions;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.pyerter.pootsadditions.tag.ModBlockTagProvider;

public class PootsAdditionsDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        PootsAdditions.logInfo("Initializing data generator");
        fabricDataGenerator.addProvider(ModBlockTagProvider::new);
    }
}
