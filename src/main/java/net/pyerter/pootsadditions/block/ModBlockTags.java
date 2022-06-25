package net.pyerter.pootsadditions.block;

import net.minecraft.block.Block;
import net.minecraft.data.server.BlockTagProvider;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockTags {
    public static final TagKey<Block> NEEDS_SAPPHIRE_TOOL = of("needs_sapphire_tool");
    public static final TagKey<Block> SWORD_MINEABLE = of("mineable/sword");

    private static TagKey<Block> of(String id) {
        return TagKey.of(Registry.BLOCK_KEY, new Identifier(id));
    }
}
