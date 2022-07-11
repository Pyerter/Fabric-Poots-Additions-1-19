package net.pyerter.pootsadditions.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.item.inventory.IAccessoryTabsHandlerProvider;
import net.pyerter.pootsadditions.screen.handlers.AccessoryInventoryScreenHandler;
import net.pyerter.pootsadditions.screen.handlers.EngineeringStationScreenHandler;
import net.pyerter.pootsadditions.screen.handlers.ModScreenHandlers;
import net.pyerter.pootsadditions.screen.handlers.PautschItemScreenHandler;
import net.pyerter.pootsadditions.util.Util;

public class AccessoryInventoryScreen extends HandledScreen<AccessoryInventoryScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier(PootsAdditions.MOD_ID, "textures/gui/accessory_inventory_screen_gui.png");

    private boolean mouseDown = false;
    private int x, y;
    private float mouseX, mouseY;

    public AccessoryInventoryScreen(ClientPlayerEntity player) {
        super(((IAccessoryTabsHandlerProvider)player).getAccessoryTabsScreenHandler(AccessoryTabAssistant.getScreenTab(ModScreenHandlers.ACCESSORIES_INVENTORY_SCREEN_HANDLER)),
                player.getInventory(), Text.of("Accessories Inventory"));
        passEvents = true;

        PootsAdditions.logInfo("Opened new accessory inventory screen with inventory size " + handler.slots.size());
    }

    public AccessoryInventoryScreen(AccessoryInventoryScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        passEvents = true;
    }

    @Override
    protected void init() {
        super.init();

        this.width = width;
        this.height = height;

        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        titleY = -25;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        x = (width - backgroundWidth) / 2;
        y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        drawVisibleGlassesIfHovered(this, matrices, x, y);

        RenderSystem.setShaderTexture(0, AccessoryTabAssistant.ACC_TABS_TEXTURE);

        // draw tab background
        AccessoryTabAssistant.drawTabBackground(this, matrices, x, y);

        for (int i = 0; i < 2; i++) {
            if (AccessoryTabAssistant.mouseInAccessoryTab(x, y, mouseX, mouseY, i)) {
                if (mouseDown)
                    AccessoryTabAssistant.drawHeldTab(this, matrices, x, y, i);
                else
                    AccessoryTabAssistant.drawHoverTab(this, matrices, x, y, i);
                AccessoryTabAssistant.drawAccTabIcon(this, matrices, x, y, i);
                break;
            }
        }

        drawEntity(x + 51, y + 75, 30, (float)(x + 51) - this.mouseX, (float)(y + 75 - 50) - this.mouseY, this.client.player);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.title, (float)this.titleX, (float)this.titleY, 4210752);
    }

    public static void drawEntity(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity) {
        float f = (float)Math.atan((double)(mouseX / 40.0F));
        float g = (float)Math.atan((double)(mouseY / 40.0F));
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate((double)x, (double)y, 1050.0);
        matrixStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        matrixStack2.translate(0.0, 0.0, 1000.0);
        matrixStack2.scale((float)size, (float)size, (float)size);
        Quaternion quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
        Quaternion quaternion2 = Vec3f.POSITIVE_X.getDegreesQuaternion(g * 20.0F);
        quaternion.hamiltonProduct(quaternion2);
        matrixStack2.multiply(quaternion);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = 180.0F + f * 20.0F;
        entity.setYaw(180.0F + f * 40.0F);
        entity.setPitch(-g * 20.0F);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternion2.conjugate();
        entityRenderDispatcher.setRotation(quaternion2);
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, matrixStack2, immediate, 15728880);
        });
        immediate.draw();
        entityRenderDispatcher.setRenderShadows(true);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
        DiffuseLighting.enableGuiDepthLighting();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseDown = true;

        int minButtonId = AccessoryTabAssistant.getFreeButtonId();

        int buttonClicked = AccessoryTabAssistant.tryClickAction(handler.getType(), x, y, mouseX, mouseY, button);
        if (buttonClicked >= 0) {
            this.client.interactionManager.clickButton(this.handler.syncId, buttonClicked);
            return true;
        }

        int glassClicked = getGlassHovered(mouseX, mouseY, x, y);
        if (glassClicked >= 0) {
            this.client.interactionManager.clickButton(this.handler.syncId, minButtonId + glassClicked);
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK, MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER), 1f);
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void drawVisibleGlassesIfHovered(DrawableHelper helper, MatrixStack matrices, int x, int y) {
        int hovered = getGlassHovered(mouseX, mouseY, x, y);
        if (hovered >= 0)
            drawGlassInSlot(helper, matrices, x, y, hovered);
    }

    public int getGlassHovered(double mouseX, double mouseY, int x, int y) {
        for (int i = 0; i < 4; i++) {
            if (Util.pointInRectangleByCorners(mouseX, mouseY, x + 101, y + 10 + (18 * i), x + 101 + 11, y + 10 + 11 + (18 * i))) {
                return i;
            }
        }
        return -1;
    }

    public void drawGlassInSlot(DrawableHelper helper, MatrixStack matrices, int x, int y, int glassNumb) {
        helper.drawTexture(matrices, x + 101, y + 10 + (18 * glassNumb), 176, 0, 12, 12);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        mouseDown = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        this.mouseX = (float)mouseX;
        this.mouseY = (float)mouseY;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
