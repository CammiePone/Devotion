package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Registry;

public class DevotionStaffCores {
	public static final RegistryHandler<StaffCore> STAFF_CORES = RegistryHandler.create(DevotionRegistries.STAFF_CORE, Devotion.MOD_ID);
	public static final Registry<StaffCore> REGISTRY = STAFF_CORES.createNewRegistry(true, Devotion.id("oak_core"));

	public static final RegistrySupplier<StaffCore> OAK_CORE = STAFF_CORES.register("oak_core", StaffCore::new);
	public static final RegistrySupplier<StaffCore> SPRUCE_CORE = STAFF_CORES.register("spruce_core", StaffCore::new);
	public static final RegistrySupplier<StaffCore> BIRCH_CORE = STAFF_CORES.register("birch_core", StaffCore::new);
	public static final RegistrySupplier<StaffCore> JUNGLE_CORE = STAFF_CORES.register("jungle_core", StaffCore::new);
	public static final RegistrySupplier<StaffCore> ACACIA_CORE = STAFF_CORES.register("acacia_core", StaffCore::new);
	public static final RegistrySupplier<StaffCore> DARK_OAK_CORE = STAFF_CORES.register("dark_oak_core", StaffCore::new);
	public static final RegistrySupplier<StaffCore> BAMBOO_CORE = STAFF_CORES.register("bamboo_core", StaffCore::new);
	public static final RegistrySupplier<StaffCore> MANGROVE_CORE = STAFF_CORES.register("mangrove_core", StaffCore::new);
	public static final RegistrySupplier<StaffCore> CHERRY_CORE = STAFF_CORES.register("cherry_core", StaffCore::new);
	public static final RegistrySupplier<StaffCore> CRIMSON_CORE = STAFF_CORES.register("crimson_core", StaffCore::new);
	public static final RegistrySupplier<StaffCore> WARPED_CORE = STAFF_CORES.register("warped_core", StaffCore::new);
}
