package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.actions.AltarAction;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.common.actions.EmptyAltarAction;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Registry;

public class DevotionAltarActions {
	public static final RegistryHandler<AltarAction> ACTIONS = RegistryHandler.create(DevotionRegistries.ALTAR_ACTION, Devotion.MOD_ID);
	public static final Registry<AltarAction> REGISTRY = ACTIONS.createNewRegistry(true, Devotion.id("empty"));

	public static final RegistrySupplier<AltarAction> EMPTY = ACTIONS.register("empty", EmptyAltarAction::new);
}
