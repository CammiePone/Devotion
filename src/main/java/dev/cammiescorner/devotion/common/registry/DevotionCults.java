package dev.cammiescorner.devotion.common.registry;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.cults.Cult;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class DevotionCults {
	//-----Cult Map-----//
	public static final LinkedHashMap<Cult, Identifier> CULTS = new LinkedHashMap<>();

	//-----Cults-----//
	public static final Cult EMPTY = create("empty", new Cult(Items.AIR));
	public static final Cult TIME_CULT = create("time_cult", new Cult(DevotionItems.DIAMOND_GEMSTONE_TABLET));
	public static final Cult EAST_CULT = create("east_cult", new Cult(DevotionItems.AMETHYST_GEMSTONE_TABLET));
	public static final Cult SPACE_CULT = create("space_cult", new Cult(DevotionItems.QUARTZ_GEMSTONE_TABLET));
	public static final Cult WEST_CULT = create("west_cult", new Cult(DevotionItems.EMERALD_GEMSTONE_TABLET));

	//-----Registry-----//
	public static void register() {
		CULTS.keySet().forEach(cult -> Registry.register(Devotion.CULT, CULTS.get(cult), cult));
	}

	private static <T extends Cult> T create(String name, T cult) {
		CULTS.put(cult, Devotion.id(name));
		return cult;
	}
}
