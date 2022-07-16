package dev.cammiescorner.devotion.api.cults;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public enum Cults {
	NONE("minecraft:air"),
	NORTH("devotion:diamond_gemstone_tablet"),
	EAST("devotion:amethyst_gemstone_tablet"),
	SOUTH("devotion:quartz_gemstone_tablet"),
	WEST("devotion:emerald_gemstone_tablet");

	private final Identifier tabletId;

	Cults(String tabletId) {
		this.tabletId = new Identifier(tabletId);
	}

	public Item getGemstoneTablet() {
		return Registry.ITEM.get(tabletId);
	}
}
