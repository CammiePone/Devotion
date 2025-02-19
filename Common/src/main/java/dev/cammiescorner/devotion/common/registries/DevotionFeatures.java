package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.world.AuraNodeFeature;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class DevotionFeatures {
	public static final RegistryHandler<Feature<?>> FEATURES = RegistryHandler.create(Registries.FEATURE, Devotion.MOD_ID);

	public static final RegistrySupplier<Feature<NoneFeatureConfiguration>> AURA_NODE_FEATURE = FEATURES.register("aura_node", () -> new AuraNodeFeature(NoneFeatureConfiguration.CODEC));
}
