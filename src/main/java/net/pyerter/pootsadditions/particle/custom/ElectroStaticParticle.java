package net.pyerter.pootsadditions.particle.custom;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class ElectroStaticParticle extends SpriteBillboardParticle {
    protected ElectroStaticParticle(ClientWorld clientWorld, double x, double y, double z,
                                    SpriteProvider spriteProvider, double xDelta, double yDelta, double zDelta) {
        super(clientWorld, x, y, z, xDelta, yDelta, zDelta);

        this.velocityMultiplier = 0.5f;
        this.x = xDelta;
        this.y = yDelta;
        this.z = zDelta;

        this.scale *= 1.25f;
        this.maxAge = 10;
        this.setSpriteForAge(spriteProvider);

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteProvider) {
            this.sprites = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld world,
                                       double x, double y, double z,
                                       double xDelta, double yDelta, double zDelta) {
            return new ElectroStaticParticle(world, x, y, z, this.sprites, xDelta, yDelta, zDelta);
        }
    }
}
