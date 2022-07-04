package net.pyerter.pootsadditions.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.item.inventory.AccessoriesInventory;
import net.pyerter.pootsadditions.item.inventory.IAccessoriesInventory;
import net.pyerter.pootsadditions.item.inventory.IAccessoryTabsHandlerProvider;
import net.pyerter.pootsadditions.screen.AccessoryTabAssistant;
import org.apache.commons.compress.harmony.pack200.NewAttributeBands;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class ModPlayerAccessoriesInventoryMixin extends LivingEntity implements IAccessoriesInventory, IAccessoryTabsHandlerProvider {
    protected AccessoriesInventory accessoriesInventory;
    protected List<ScreenHandler> registeredAccessoryTabHandlers;

    protected ModPlayerAccessoriesInventoryMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method="<init>", at=@At("RETURN"))
    void onConstructorCall(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey, CallbackInfo info) {
        accessoriesInventory = new AccessoriesInventory((PlayerEntity)(Object)this);
        registeredAccessoryTabHandlers = new ArrayList<>();
        AccessoryTabAssistant.generateScreenHandlers(registeredAccessoryTabHandlers, ((PlayerEntity)(Object)this).getInventory(), !world.isClient, ((PlayerEntity)(Object)this));
    }

    @Inject(method="tickMovement", at=@At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;updateItems()V"))
    void duringTickUpdateInventory(CallbackInfo info) {
        accessoriesInventory.updateItems();
    }

    @Inject(method="dropInventory", at=@At(value = "RETURN"))
    void onDropInventory(CallbackInfo info) {
        if (!this.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            this.accessoriesInventory.dropAll();
        }
    }

    @Inject(method="vanishCursedItems", at=@At("RETURN"))
    void onVanishCursedItems(CallbackInfo info) {
        for(int i = 0; i < this.accessoriesInventory.size(); ++i) {
            ItemStack itemStack = this.accessoriesInventory.getStack(i);
            if (!itemStack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemStack)) {
                this.accessoriesInventory.removeStack(i);
            }
        }
    }

    @Inject(method="readCustomDataFromNbt", at=@At(value = "RETURN"))
    public void onReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        NbtList nbtListAccessories = nbt.getList(AccessoriesInventory.INVENTORY_NBT_ID, 10);
        this.accessoriesInventory.readNbt(nbtListAccessories);
    }
    @Inject(method="writeCustomDataToNbt", at=@At(value = "RETURN"))
    public void onWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.put(AccessoriesInventory.INVENTORY_NBT_ID, this.accessoriesInventory.writeNbt(new NbtList()));
    }

    @Override
    public AccessoriesInventory getAccessoriesInventory() {
        return accessoriesInventory;
    }

    @Override
    public ScreenHandler getAccessoryTabsScreenHandler(int tab) {
        if (tab > 0 && tab - 1 < registeredAccessoryTabHandlers.size())
            return registeredAccessoryTabHandlers.get(tab - 1);

        if (tab == 0)
            return ((PlayerEntity)(Object)this).playerScreenHandler;

        PootsAdditions.LOGGER.error("Error - tried fetching non-generated player screen tab from PlayerEntity");
        return null;
    }
}
