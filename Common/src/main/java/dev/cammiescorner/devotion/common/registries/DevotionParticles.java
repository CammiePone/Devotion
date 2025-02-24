package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class DevotionParticles {
	public static final RegistryHandler<ParticleType<?>> PARTICLES = RegistryHandler.create(Registries.PARTICLE_TYPE, Devotion.MOD_ID);

	public static final RegistrySupplier<SimpleParticleType> AURA_NODE = PARTICLES.register("aura_node", () -> new SimpleParticleType(true));
}
