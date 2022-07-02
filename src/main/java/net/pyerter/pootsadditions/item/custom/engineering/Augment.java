package net.pyerter.pootsadditions.item.custom.engineering;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// :)
public abstract class Augment extends Item {
    public static final String AUGMENT_NBT_ID = "pootsadditions.augments";
    public static final String AUGMENT_NBT_INDICATOR = "pootsadditions.augment_id";
    public static final String AUGMENT_NBT_MASK_INDICATOR = "pootsadditions.augment_mask";
    public static final Map<String, Augment> stringToAugment = new HashMap<>();

    public static final int MASK_NADA =                 0;
    public static final int MASK_GET_ATTACK_DAMAGE =    1;
    public static final int MASK_GET_MINING_SPEED =     1 << 1;
    public static final int MASK_USE_ON_BLOCK =         1 << 2;
    public static final int MASK_POST_HIT =             1 << 3;
    public static final int MASK_POST_MINE =            1 << 4;
    public static final int MASK_USE_WEAPON_ABILITY =   1 << 5;
    public static final int MASK_USE_INVENTORY_TICK =   1 << 6;
    public static final int MASK_ITEM_NOT_SUITABLE =    1 << 7;
    public static final int MASK_ITEM_NOW_SUITABLE =    1 << 8;
    public static final int MASK_EFFECTIVE_BLOCKS =     1 << 9;

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

    /** Used to map the NbtCompound of this augment to the Augment Item **/
    public abstract String getAugmentID();
    /** Used to filter augments when only augments with certain implementations are wanted **/
    public abstract int getAugmentMask();
    /** Returns an NbtCompound containing data that maps to the Augment **/
    public NbtCompound getNbtCompound() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString(AUGMENT_NBT_INDICATOR, getAugmentID());
        nbt.putInt(AUGMENT_NBT_MASK_INDICATOR, getAugmentMask());
        return nbt;
    }

    /** All of the augment methods that can be overriden to apply effects **/
    public float onGetAttackDamage(ItemStack stack, AbstractEngineeredTool tool) { return 0; }
    public float onGetMiningSpeedMultiplier(ItemStack stack, AbstractEngineeredTool tool) { return 0; }
    public ActionResult onUseOnBlock(ItemStack stack, AbstractEngineeredTool tool, ItemUsageContext context) { return ActionResult.PASS; }
    public boolean onPostHit(ItemStack stack, AbstractEngineeredTool tool, LivingEntity target, LivingEntity attacker) { return false; }
    public boolean onPostMine(ItemStack stack, AbstractEngineeredTool tool, World world, BlockState state, BlockPos pos, LivingEntity miner) { return false; }
    public boolean onUseWeaponAbility(ItemStack stack, AbstractEngineeredTool tool, Entity target, PlayerEntity attacker) { return false; }
    public boolean onInventoryTick(ItemStack stack, AbstractEngineeredTool tool, World world, Entity entity, int slot, boolean selected) { return false; }
    public boolean onItemNoLongerSuitable(ItemStack stack, AbstractEngineeredTool tool) { return false; }
    public boolean onItemNowSuitable(ItemStack stack, AbstractEngineeredTool tool) { return false; }
    public boolean onStateInEffectiveBlockList(ItemStack stack, AbstractEngineeredTool tool) { return false; }

    public static Augment fromNbt(NbtCompound nbt) {
        String augmentID = nbt.getString(AUGMENT_NBT_INDICATOR);
        Augment aug = stringToAugment.get(augmentID);
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
            Augment aug = Augment.fromNbt(nbt);
            if (aug != null && !augments.contains(aug))
                augments.add(aug);
        }
        return augments;
    }

    public static List<Augment> getAugments(ItemStack stack, int mask) {
        NbtList nbtList = getAugmentsNbtList(stack);
        if (nbtList == null)
            return null;

        List<Augment> augments = new ArrayList<>(nbtList.size());
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound nbt = nbtList.getCompound(i);
            if ((nbt.getInt(AUGMENT_NBT_MASK_INDICATOR) & mask) == 0)
                continue;

            Augment aug = Augment.fromNbt(nbt);
            if (aug != null && !augments.contains(aug))
                augments.add(aug);
        }
        return augments;
    }


}
