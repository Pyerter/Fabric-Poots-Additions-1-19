package net.pyerter.pootsadditions.block;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.impl.tag.convention.TagRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.BlockTagProvider;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataGenerator root) {
        super(root);
    }

    @Override
    protected void generateTags() {
        this.getOrCreateTagBuilder(ModBlockTags.NEEDS_SAPPHIRE_TOOL).add(new Block[]{ModBlocks.SPIRAL_CUBE_BLOCK});
        this.getOrCreateTagBuilder(ModBlockTags.SWORD_MINEABLE).add(new Block[]{Blocks.COBWEB});
    }
}
