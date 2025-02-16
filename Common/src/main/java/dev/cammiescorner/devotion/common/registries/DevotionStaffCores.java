package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistryKeys;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;

public class DevotionStaffCores {
	public static final RegistryHandler<StaffCore> STAFF_CORES = RegistryHandler.create(DevotionRegistryKeys.STAFF_CORE, Devotion.MOD_ID);

	public static final RegistrySupplier<StaffCore> WOODEN_CORE = STAFF_CORES.register("wooden", StaffCore::new);
}
