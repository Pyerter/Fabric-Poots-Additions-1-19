package net.pyerter.pootsadditions.item.custom.engineering;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.block.entity.CaptureChamberEntity;
import net.pyerter.pootsadditions.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MakeshiftCore extends AbstractPowerCore {
    public static final int MAX_CHARGE = 1210;

    public MakeshiftCore(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxCharge() {
        return 1210;
    }
}
