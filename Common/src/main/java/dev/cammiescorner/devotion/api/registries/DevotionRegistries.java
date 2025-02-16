package dev.cammiescorner.devotion.api.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.actions.AltarAction;
import dev.cammiescorner.devotion.api.wands.WandCap;
import dev.cammiescorner.devotion.api.wands.WandCore;
import dev.cammiescorner.devotion.common.registries.DevotionAltarActions;
import dev.cammiescorner.devotion.common.registries.DevotionWandCaps;
import dev.cammiescorner.devotion.common.registries.DevotionWandCores;
import net.minecraft.core.Registry;

public class DevotionRegistries {
	public static final Registry<WandCore> WAND_CORES = DevotionWandCores.WAND_CORES.createNewRegistry(true, Devotion.id("wooden"));
	public static final Registry<WandCap> WAND_CAPS = DevotionWandCaps.WAND_CAPS.createNewRegistry(true, Devotion.id("iron"));
	public static final Registry<AltarAction> ALTAR_ACTIONS = DevotionAltarActions.ACTIONS.createNewRegistry(true, Devotion.id("empty"));
}
