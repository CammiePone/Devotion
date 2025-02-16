package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.actions.AltarAction;
import dev.cammiescorner.devotion.api.registries.DevotionRegistryKeys;
import dev.cammiescorner.devotion.common.actions.EmptyAltarAction;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;

public class DevotionAltarActions {
	public static final RegistryHandler<AltarAction> ACTIONS = RegistryHandler.create(DevotionRegistryKeys.ALTAR_ACTION, Devotion.MOD_ID);

	public static final RegistrySupplier<AltarAction> EMPTY = ACTIONS.register("empty", EmptyAltarAction::new);
}
