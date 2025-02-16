package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistryKeys;
import dev.cammiescorner.devotion.api.wands.WandCore;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;

public class DevotionWandCores {
	public static final RegistryHandler<WandCore> WAND_CORES = RegistryHandler.create(DevotionRegistryKeys.WAND_CORE, Devotion.MOD_ID);

	public static final RegistrySupplier<WandCore> WOODEN_CORE = WAND_CORES.register("wooden", WandCore::new);
}
