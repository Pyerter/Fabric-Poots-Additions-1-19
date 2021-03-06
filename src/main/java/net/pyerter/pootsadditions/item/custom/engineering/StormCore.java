package net.pyerter.pootsadditions.item.custom.engineering;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class StormCore extends AbstractPowerCore {
    public static final int MAX_CHARGE = 12100;

    public StormCore(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxCharge() {
        return MAX_CHARGE;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add(new ItemStack(this));
            ItemStack chargedStack = new ItemStack(this);
            tryCharge(chargedStack, getMaxCharge());
            stacks.add(chargedStack);
        }
    }
}
