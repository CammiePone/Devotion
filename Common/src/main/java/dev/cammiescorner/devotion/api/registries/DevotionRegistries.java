package dev.cammiescorner.devotion.api.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.actions.AltarAction;
import dev.cammiescorner.devotion.api.actions.ConfiguredAltarAction;
import dev.cammiescorner.devotion.api.book.BookTab;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.SpellFocus;
import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class DevotionRegistries {
	public static final ResourceKey<Registry<StaffCore>> STAFF_CORE = ResourceKey.createRegistryKey(Devotion.id("staff_core"));
	public static final ResourceKey<Registry<StaffCap>> STAFF_CAP = ResourceKey.createRegistryKey(Devotion.id("staff_cap"));
	public static final ResourceKey<Registry<SpellFocus>> SPELL_FOCUS = ResourceKey.createRegistryKey(Devotion.id("spell_focus"));
	public static final ResourceKey<Registry<BookTab>> BOOK_TAB = ResourceKey.createRegistryKey(Devotion.id("book_tabs"));
	public static final ResourceKey<Registry<Research>> RESEARCH = ResourceKey.createRegistryKey(Devotion.id("research_entries"));
	public static final ResourceKey<Registry<AltarAction>> ALTAR_ACTION = ResourceKey.createRegistryKey(Devotion.id("altar_action"));
	public static final ResourceKey<Registry<ConfiguredAltarAction>> CONFIGURED_ALTAR_ACTION = ResourceKey.createRegistryKey(Devotion.id("configured_altar_action"));
}
