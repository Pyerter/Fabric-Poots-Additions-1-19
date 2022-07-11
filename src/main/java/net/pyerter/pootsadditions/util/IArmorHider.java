package net.pyerter.pootsadditions.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;

public interface IArmorHider {
    public static final String NBT_HEAD_VISIBLITY = "pootsadditions.head_visible";
    public static final String NBT_CHEST_VISIBLITY = "pootsadditions.chest_visible";
    public static final String NBT_LEGS_VISIBLITY = "pootsadditions.legs_visible";
    public static final String NBT_FEET_VISIBLITY = "pootsadditions.feet_visible";

    public static final TrackedData<Boolean> ARMOR_VISIBLE_CHEST = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> ARMOR_VISIBLE_LEGS = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> ARMOR_VISIBLE_FEET = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> ARMOR_VISIBLE_HEAD = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public DataTracker getDataTracker();

    public default boolean isArmorChestVisible() {
        return getDataTracker().get(ARMOR_VISIBLE_CHEST);
    }
    public default boolean isArmorLegsVisible() {
        return getDataTracker().get(ARMOR_VISIBLE_LEGS);
    }
    public default boolean isArmorFeetVisible() {
        return getDataTracker().get(ARMOR_VISIBLE_FEET);
    }
    public default boolean isArmorHeadVisible() {
        return getDataTracker().get(ARMOR_VISIBLE_HEAD);
    }
    public default void setArmorChestVisible(boolean val) {
        getDataTracker().set(ARMOR_VISIBLE_CHEST, val);
    }
    public default void setArmorLegsVisible(boolean val) {
        getDataTracker().set(ARMOR_VISIBLE_LEGS, val);
    }
    public default void setArmorFeetVisible(boolean val) {
        getDataTracker().set(ARMOR_VISIBLE_FEET, val);
    }
    public default void setArmorHeadVisible(boolean val) {
        getDataTracker().set(ARMOR_VISIBLE_HEAD, val);
    }

    public static void setArmorVisibleByEquipSlot(IArmorHider armorHider, boolean val, EquipmentSlot slot) {
        switch (slot) {
            case CHEST: armorHider.setArmorChestVisible(val); break;
            case LEGS: armorHider.setArmorLegsVisible(val); break;
            case FEET: armorHider.setArmorFeetVisible(val); break;
            case HEAD: armorHider.setArmorHeadVisible(val); break;
        }
    }

    public static boolean getArmorVisibleByEquipSlot(IArmorHider armorHider, EquipmentSlot slot) {
        switch (slot) {
            case CHEST: return armorHider.isArmorChestVisible();
            case LEGS: return armorHider.isArmorLegsVisible();
            case FEET: return armorHider.isArmorFeetVisible();
            case HEAD: return armorHider.isArmorHeadVisible();
        }
        return true;
    }
}
