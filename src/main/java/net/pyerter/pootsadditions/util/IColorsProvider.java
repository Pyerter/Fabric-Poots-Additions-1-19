package net.pyerter.pootsadditions.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.item.ItemColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

public interface IColorsProvider {

    public ItemColors getColors();

}
