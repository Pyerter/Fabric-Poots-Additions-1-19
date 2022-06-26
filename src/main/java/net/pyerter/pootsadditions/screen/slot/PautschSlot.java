package net.pyerter.pootsadditions.screen.slot;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PautschSlot extends Slot {

    public PautschSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }
}
