package net.pyerter.pootsadditions.tag;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockTags {
    public static final TagKey<Block> NEEDS_STARMETAL_TOOL = registerBlockTag(of("needs_starmetal_tool"));
    public static final TagKey<Block> NEEDS_NETHERITE_TOOL = registerBlockTag(of("needs_netherite_tool"));
    public static final TagKey<Block> SWORD_MINEABLE = registerBlockTag(of("mineable/sword"), Blocks.COBWEB);

    public static TagKey<Block> of(String id) {
        return TagKey.of(Registry.BLOCK_KEY, new Identifier(id));
    }

    public static TagKey<Block> registerBlockTag(TagKey<Block> tagKey, Block ... blocks) {
        ModBlockTagProvider.tryRegisterBlockTag(tagKey);
        for (Block block: blocks) {
            ModBlockTagProvider.tryRegisterBlockToTag(tagKey, block);
        }
        return tagKey;
    }
}
