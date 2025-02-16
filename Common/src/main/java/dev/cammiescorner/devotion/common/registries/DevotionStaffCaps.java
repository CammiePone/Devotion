package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistryKeys;
import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;

public class DevotionStaffCaps {
	public static final RegistryHandler<StaffCap> STAFF_CAPS = RegistryHandler.create(DevotionRegistryKeys.STAFF_CAP, Devotion.MOD_ID);

	public static final RegistrySupplier<StaffCap> IRON_CAP = STAFF_CAPS.register("iron", StaffCap::new);
	public static final RegistrySupplier<StaffCap> GOLD_CAP = STAFF_CAPS.register("gold", StaffCap::new);
	public static final RegistrySupplier<StaffCap> INERT_COPPER_CAP = STAFF_CAPS.register("inert_copper", StaffCap::new);
	public static final RegistrySupplier<StaffCap> CHARGED_COPPER_CAP = STAFF_CAPS.register("charged_copper", StaffCap::new);
	public static final RegistrySupplier<StaffCap> INERT_NETHERITE_CAP = STAFF_CAPS.register("inert_netherite", StaffCap::new);
	public static final RegistrySupplier<StaffCap> CHARGED_NETHERITE_CAP = STAFF_CAPS.register("charged_netherite", StaffCap::new);
}
