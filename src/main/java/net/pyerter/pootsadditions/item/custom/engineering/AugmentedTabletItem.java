package net.pyerter.pootsadditions.item.custom.engineering;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AugmentedTabletItem extends Item {

    public AugmentedTabletItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        List<Augment> augments = AugmentHelper.getAugments(stack);
        for (Augment aug: augments) {
            tooltip.add(Text.of(aug.getTranslation()));
        }
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            for (Augment aug : Augment.getAllAugments()) {
                stacks.add(tabletEntry(aug));
            }
        }
    }

    public static ItemStack tabletEntry(Augment aug) {
        ItemStack stack = new ItemStack(ModItems.AUGMENTED_TABLET_ITEM);
        aug.applyAugment(stack);
        return stack;
    }
}
