package net.pyerter.pootsadditions.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.pyerter.pootsadditions.util.Util;

public interface Chargeable {
    public int getMaxCharge();
    public default String getChargeNbtID() {
        return "pootsadditions.charge";
    }

    public default int tryCharge(ItemStack stack, int amount) {
        int currentCharge = getCharge(stack);
        return addCharge(stack, amount) - currentCharge;
    }

    public default int addCharge(ItemStack stack, int charge) {
        return setCharge(stack, charge + getCharge(stack));
    }

    public default int setCharge(ItemStack stack, int charge) {
        charge = Util.clamp(charge, 0, getMaxCharge());
        if (stack.hasNbt()) {
            stack.getNbt().putInt(getChargeNbtID(), charge);
        } else {
            NbtCompound nbtData = new NbtCompound();
            nbtData.putInt(getChargeNbtID(), charge);
            stack.setNbt(nbtData);
        }

        return charge;
    }

    public default int getCharge(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(getChargeNbtID())) {
            return stack.getNbt().getInt(getChargeNbtID());
        } else {
            return setCharge(stack, 0);
        }
    }
}
