package net.pyerter.pootsadditions.util;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.pyerter.pootsadditions.screen.handlers.NbtItemNamedScreenHandlerFactory;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;

public interface IModServerPlayerEntityPautschScreenOpener {
    public OptionalInt openHandledScreen(@Nullable NbtItemNamedScreenHandlerFactory factory, ItemStack stack);

}
