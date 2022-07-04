package net.pyerter.pootsadditions.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.screen.AccessoryTabAssistant;
import net.pyerter.pootsadditions.util.Util;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryScreen.class)
public abstract class ModInventoryScreenTabsMixin extends AbstractInventoryScreen<PlayerScreenHandler> {

    private boolean mouseHeldDown;

    public ModInventoryScreenTabsMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method="drawBackground", at=@At("RETURN"))
    public void drawAccessoryTabs(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo info) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, AccessoryTabAssistant.ACC_TABS_TEXTURE);

        // draw tab background
        AccessoryTabAssistant.drawTabBackground(this, matrices, x, y);

        for (int i = 0; i < 2; i++) {
            if (AccessoryTabAssistant.mouseInAccessoryTab(x, y, mouseX, mouseY, i)) {
                if (mouseHeldDown)
                    AccessoryTabAssistant.drawHeldTab(this, matrices, x, y, i);
                else
                    AccessoryTabAssistant.drawHoverTab(this, matrices, x, y, i);
                AccessoryTabAssistant.drawAccTabIcon(this, matrices, x, y, i);
                break;
            }
        }
    }

    @Inject(method="mouseClicked", at=@At("HEAD"), cancellable = true)
    public void mouseClickedTabsAction(double mouseX, double mouseY, int button, CallbackInfoReturnable info) {
        mouseHeldDown = true;

        int buttonClicked = AccessoryTabAssistant.tryClickAction(null, x, y, mouseX, mouseY, button, true);
        if (buttonClicked > 0) {
            this.client.interactionManager.clickButton(this.handler.syncId, buttonClicked);
            info.setReturnValue(true);
        }
    }

    @Inject(method="mouseReleased", at=@At("HEAD"))
    public void mouseReleasedTabsAction(CallbackInfoReturnable info) {
        mouseHeldDown = false;
    }

}
