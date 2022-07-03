package net.pyerter.pootsadditions.mixin;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.render.item.ItemRenderer;
import net.pyerter.pootsadditions.util.IColorsProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemRenderer.class)
public abstract class ModItemRendererColorProviderMixin implements IColorsProvider {

    @Shadow
    ItemColors colors;

    @Override
    public ItemColors getColors() {
        return colors;
    }
}
