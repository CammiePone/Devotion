package dev.cammiescorner.devotion.common.registry;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.Spell;
import dev.cammiescorner.devotion.common.spells.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class DevotionSpells {
	//-----Spell Map-----//
	public static final LinkedHashMap<Spell, Identifier> SPELLS = new LinkedHashMap<>();

	public static final Spell EMPTY = create("empty", Spell.EMPTY);
	public static final Spell FULL_COWL = create("full_cowl", new FullCowlSpell());
	public static final Spell DISCHARGE = create("discharge", new DischargeSpell());
	public static final Spell MASTER_SPARK = create("master_spark", new MasterSparkSpell());
	public static final Spell BLACK_HOLE = create("black_hole", new BlackHoleSpell());
	public static final Spell ANIMATE_ARMOUR = create("animate_armor", new AnimateArmourSpell());
	public static final Spell TEMPORAL_DISRUPTION = create("temporal_disruption", new TemporalDisruptionSpell());

	//-----Registry-----//
	public static void register() {
		SPELLS.forEach((spell, id) -> Registry.register(Devotion.SPELL, id, spell));
	}

	private static <T extends Spell> T create(String name, T spell) {
		SPELLS.put(spell, new Identifier(Devotion.MOD_ID, name));
		return spell;
	}
}
