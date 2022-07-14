package net.pyerter.pootsadditions.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tag.TagKey;
import net.pyerter.pootsadditions.PootsAdditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataGenerator root) {
        super(root);
    }

    public static Map<TagKey<Block>, List<Block>> pendingTags = new HashMap<>();
    public static boolean tryRegisterBlockToTag(TagKey<Block> blockTag, Block block) {
        if (!pendingTags.containsKey(blockTag)) {
            pendingTags.put(blockTag, new ArrayList<>());
            pendingTags.get(blockTag).add(block);
            return true;
        } else if (!pendingTags.get(blockTag).contains(block)) {
            pendingTags.get(blockTag).add(block);
            return true;
        }
        return false;
    }
    public static boolean tryRegisterBlockTag(TagKey<Block> blockTag) {
        if (!pendingTags.containsKey(blockTag)) {
            pendingTags.put(blockTag, new ArrayList<>());
            return true;
        }
        return false;
    }

    @Override
    protected void generateTags() {
        int generatedTags = 0;
        for (TagKey<Block> tagKey: pendingTags.keySet()) {
            if (pendingTags.get(tagKey).size() > 0)
                generatedTags++;
            this.getOrCreateTagBuilder(tagKey).add(pendingTags.get(tagKey).stream().toArray(Block[]::new));
        }
        PootsAdditions.logInfo("Generated " + generatedTags + " custom block tags.");
    }
}
