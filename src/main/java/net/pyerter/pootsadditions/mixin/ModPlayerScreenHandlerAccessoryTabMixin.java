package net.pyerter.pootsadditions.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.screen.AccessoryTabAssistant;
import net.pyerter.pootsadditions.screen.handlers.ModScreenHandlers;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerScreenHandler.class)
public abstract class ModPlayerScreenHandlerAccessoryTabMixin extends AbstractRecipeScreenHandler<CraftingInventory> {
    public ModPlayerScreenHandlerAccessoryTabMixin(ScreenHandlerType<?> screenHandlerType, int i) {
        super(screenHandlerType, i);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        PootsAdditions.logInfo("Accepting button click for tab " + id);
        if (!player.world.isClient() && AccessoryTabAssistant.userOpenHandledScreen(player, id)) {
            return true;
        }
        return super.onButtonClick(player, id);
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return ModScreenHandlers.PLAYER_SCREEN_HANDLER;
    }
}
