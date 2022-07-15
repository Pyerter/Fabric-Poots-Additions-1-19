package net.pyerter.pootsadditions.particle;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.pyerter.pootsadditions.PootsAdditions;
import net.pyerter.pootsadditions.particle.custom.ManaParticle;

public class ModParticles {

    public static final Random RANDOM = Random.create();

    public static final DefaultParticleType ELECTRO_STATIC_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType MANA_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(PootsAdditions.MOD_ID, "electro_static_particle"), ELECTRO_STATIC_PARTICLE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(PootsAdditions.MOD_ID, "mana_particle"), MANA_PARTICLE);
    }

    public static void registerParticleFactories() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.ELECTRO_STATIC_PARTICLE, GlowParticle.ElectricSparkFactory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.MANA_PARTICLE, ManaParticle.Factory::new);
    }

}
