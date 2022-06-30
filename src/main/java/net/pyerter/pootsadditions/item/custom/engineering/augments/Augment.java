package net.pyerter.pootsadditions.item.custom.engineering.augments;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Augment extends Item {
    public static final String AUGMENT_NBT_ID = "pootsadditions.augments";
    public static final String AUGMENT_NBT_INDICATOR = "pootsadditions.augment_id";
    public static final Map<String, Augment> stringToAugment = new HashMap<>();

    public static boolean registerAugment(Augment aug) {
        if (!stringToAugment.containsKey(aug.getAugmentID())) {
            stringToAugment.put(aug.getAugmentID(), aug);
            return true;
        }
        return false;
    }

    public Augment(Settings settings) {
        super(settings);
        registerAugment(this);
    }

    public abstract String getAugmentNbtID();
    public String getAugmentID() {
        return getAugmentNbtID();
    }
    public abstract NbtCompound getNbtCompound();

    public static Augment fromNbt(NbtCompound nbt) {
        String augmentID = nbt.getString(AUGMENT_NBT_INDICATOR);
        Augment aug = stringToAugment.containsKey(augmentID) ? stringToAugment.get(augmentID) : null;
        return aug;
    }

    public static boolean writeAugmentsToNbt(NbtCompound nbt, List<Augment> augments) {
        NbtList nbtList = new NbtList();
        for (Augment aug: augments) {
            nbtList.add(aug.getNbtCompound());
        }
        nbt.put(AUGMENT_NBT_ID, nbtList);
        return true;
    }

    public static boolean writeAugmentsToNbt(NbtCompound nbt, NbtList nbtList) {
        nbt.put(AUGMENT_NBT_ID, nbtList);
        return true;
    }

    public static NbtList getAugmentsNbtList(ItemStack stack) {
        return stack.hasNbt() ? stack.getNbt().getList(AUGMENT_NBT_ID, 10) : new NbtList();
    }

    public static List<Augment> getAugments(ItemStack stack) {
        NbtList nbtList = getAugmentsNbtList(stack);
        if (nbtList == null)
            return null;

        List<Augment> augments = new ArrayList<>(nbtList.size());
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound nbt = nbtList.getCompound(i);
            augments.add(Augment.fromNbt(nbt));
        }
        return augments;
    }


}
