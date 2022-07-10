package net.pyerter.pootsadditions.particle;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pyerter.pootsadditions.PootsAdditions;

public class ModParticles {

    public static final DefaultParticleType ELECTRO_STATIC_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(PootsAdditions.MOD_ID, "electro_static_particle"), ELECTRO_STATIC_PARTICLE);
    }

    public static void registerParticleFactories() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.ELECTRO_STATIC_PARTICLE, GlowParticle.ElectricSparkFactory::new);
    }

}
