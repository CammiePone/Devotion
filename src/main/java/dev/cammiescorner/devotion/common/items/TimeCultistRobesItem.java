package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.common.registry.DevotionMaterials;
import net.minecraft.entity.EquipmentSlot;

public class TimeCultistRobesItem extends RobesItem {
	private final boolean isLeaderRobes;

	public TimeCultistRobesItem(EquipmentSlot slot, boolean isLeaderRobes) {
		super(isLeaderRobes ? DevotionMaterials.Armour.LEADER_ROBES : DevotionMaterials.Armour.MAGE_ROBES, slot);
		this.isLeaderRobes = isLeaderRobes;
	}

	public boolean isLeaderRobes() {
		return isLeaderRobes;
	}
}
