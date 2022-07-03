package net.pyerter.pootsadditions.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pyerter.pootsadditions.screen.handlers.NbtItemNamedScreenHandlerFactory;
import net.pyerter.pootsadditions.util.IModServerPlayerEntityPautschScreenOpener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.OptionalInt;

@Mixin(ServerPlayerEntity.class)
public abstract class ModServerPlayerEntityPautschScreenMixin extends PlayerEntity implements IModServerPlayerEntityPautschScreenOpener {


    public ModServerPlayerEntityPautschScreenMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Shadow
    private int screenHandlerSyncId;
    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    @Shadow
    public void closeHandledScreen() { }

    @Shadow
    private void incrementScreenHandlerSyncId(){ }

    @Shadow
    private void onScreenHandlerOpened(ScreenHandler screenHandler){ }

    @Override
    public OptionalInt openHandledScreen(@Nullable NbtItemNamedScreenHandlerFactory factory, ItemStack stack) {
        if (factory == null) {
            return OptionalInt.empty();
        } else {
            if (this.currentScreenHandler != this.playerScreenHandler) {
                this.closeHandledScreen();
            }

            this.incrementScreenHandlerSyncId();
            ScreenHandler screenHandler = factory.createMenu(screenHandlerSyncId, this.getInventory(), this, stack);
            if (screenHandler == null) {
                if (this.isSpectator()) {
                    this.sendMessage(Text.translatable("container.spectatorCantOpen").formatted(Formatting.RED), true);
                }

                return OptionalInt.empty();
            } else {
                this.networkHandler.sendPacket(new OpenScreenS2CPacket(screenHandler.syncId, screenHandler.getType(), factory.getDisplayName()));
                this.onScreenHandlerOpened(screenHandler);
                this.currentScreenHandler = screenHandler;
                return OptionalInt.of(screenHandlerSyncId);
            }
        }
    }

}
