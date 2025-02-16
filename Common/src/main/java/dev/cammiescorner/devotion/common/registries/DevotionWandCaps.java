package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistryKeys;
import dev.cammiescorner.devotion.api.wands.WandCap;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;

public class DevotionWandCaps {
	public static final RegistryHandler<WandCap> WAND_CAPS = RegistryHandler.create(DevotionRegistryKeys.WAND_CAP, Devotion.MOD_ID);

	public static final RegistrySupplier<WandCap> IRON_CAP = WAND_CAPS.register("iron", WandCap::new);
}
