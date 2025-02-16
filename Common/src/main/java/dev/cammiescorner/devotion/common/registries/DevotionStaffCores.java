package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Registry;

public class DevotionStaffCores {
	public static final RegistryHandler<StaffCore> STAFF_CORES = RegistryHandler.create(DevotionRegistries.STAFF_CORE, Devotion.MOD_ID);
	public static final Registry<StaffCore> REGISTRY = STAFF_CORES.createNewRegistry(true, Devotion.id("oak"));

	public static final RegistrySupplier<StaffCore> OAK_CORE = STAFF_CORES.register("oak", StaffCore::new);
	public static final RegistrySupplier<StaffCore> SPRUCE_CORE = STAFF_CORES.register("spruce", StaffCore::new);
	public static final RegistrySupplier<StaffCore> BIRCH_CORE = STAFF_CORES.register("birch", StaffCore::new);
	public static final RegistrySupplier<StaffCore> JUNGLE_CORE = STAFF_CORES.register("jungle", StaffCore::new);
	public static final RegistrySupplier<StaffCore> ACACIA_CORE = STAFF_CORES.register("acacia", StaffCore::new);
	public static final RegistrySupplier<StaffCore> DARK_OAK_CORE = STAFF_CORES.register("dark_oak", StaffCore::new);
	public static final RegistrySupplier<StaffCore> MANGROVE_CORE = STAFF_CORES.register("mangrove", StaffCore::new);
	public static final RegistrySupplier<StaffCore> CHERRY_CORE = STAFF_CORES.register("cherry", StaffCore::new);
	public static final RegistrySupplier<StaffCore> CRIMSON_CORE = STAFF_CORES.register("crimson", StaffCore::new);
	public static final RegistrySupplier<StaffCore> WARPED_CORE = STAFF_CORES.register("warped", StaffCore::new);
}
