package dev.cammiescorner.devotion.api.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.actions.AltarAction;
import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.common.registries.DevotionAltarActions;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import net.minecraft.core.Registry;

public class DevotionRegistries {
	public static final Registry<StaffCore> STAFF_CORES = DevotionStaffCores.STAFF_CORES.createNewRegistry(true, Devotion.id("oak"));
	public static final Registry<StaffCap> STAFF_CAPS = DevotionStaffCaps.STAFF_CAPS.createNewRegistry(true, Devotion.id("iron"));
	public static final Registry<AltarAction> ALTAR_ACTIONS = DevotionAltarActions.ACTIONS.createNewRegistry(true, Devotion.id("empty"));

	public static void init() {

	}
}
