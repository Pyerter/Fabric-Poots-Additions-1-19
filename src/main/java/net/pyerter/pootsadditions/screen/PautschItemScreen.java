package net.pyerter.pootsadditions.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.screen.handlers.PautschItemScreenHandler;

import java.util.List;

public class PautschItemScreen extends HandledScreen<PautschItemScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier(PootsAdditions.MOD_ID, "textures/gui/pautsch_item_gui.png");

    public PautschItemScreen(PautschItemScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.width = width;
        this.height = height;

        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        titleY = -10;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // draw top title tab
        drawTexture(matrices, x + 24, y - 20, 0, 179, 128,  20);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
