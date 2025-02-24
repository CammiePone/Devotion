package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.api.spells.SpellFocus;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Registry;

public class DevotionSpellFoci {
	public static final RegistryHandler<SpellFocus> SPELL_FOCI = RegistryHandler.create(DevotionRegistries.SPELL_FOCUS, Devotion.MOD_ID);
	public static final Registry<SpellFocus> REGISTRY = SPELL_FOCI.createNewRegistry(true, Devotion.id("blank_focus"));

	public static final RegistrySupplier<SpellFocus> BLANK = SPELL_FOCI.register("blank_focus", SpellFocus::new);
	public static final RegistrySupplier<SpellFocus> SCORCHING_BEAM = SPELL_FOCI.register("scorching_beam_focus", SpellFocus::new);
	public static final RegistrySupplier<SpellFocus> SLOW_TIME = SPELL_FOCI.register("slow_time_focus", SpellFocus::new);
	public static final RegistrySupplier<SpellFocus> MAGE_MACE = SPELL_FOCI.register("mage_mace_focus", SpellFocus::new);
	public static final RegistrySupplier<SpellFocus> PORTABLE_HOLE = SPELL_FOCI.register("portable_hole_focus", SpellFocus::new);
}
