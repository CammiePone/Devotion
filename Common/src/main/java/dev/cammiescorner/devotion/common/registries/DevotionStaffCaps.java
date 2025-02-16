package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Registry;

public class DevotionStaffCaps {
	public static final RegistryHandler<StaffCap> STAFF_CAPS = RegistryHandler.create(DevotionRegistries.STAFF_CAP, Devotion.MOD_ID);
	public static final Registry<StaffCap> REGISTRY = STAFF_CAPS.createNewRegistry(true, Devotion.id("iron"));

	public static final RegistrySupplier<StaffCap> IRON_CAP = STAFF_CAPS.register("iron", () -> new StaffCap(false, false));
	public static final RegistrySupplier<StaffCap> GOLD_CAP = STAFF_CAPS.register("gold", () -> new StaffCap(false, false));
	public static final RegistrySupplier<StaffCap> INERT_COPPER_CAP = STAFF_CAPS.register("inert_copper", () -> new StaffCap(true, false));
	public static final RegistrySupplier<StaffCap> CHARGED_COPPER_CAP = STAFF_CAPS.register("charged_copper", () -> new StaffCap(false, true));
	public static final RegistrySupplier<StaffCap> INERT_NETHERITE_CAP = STAFF_CAPS.register("inert_netherite", () -> new StaffCap(true, false));
	public static final RegistrySupplier<StaffCap> CHARGED_NETHERITE_CAP = STAFF_CAPS.register("charged_netherite", () -> new StaffCap(false, true));
}
