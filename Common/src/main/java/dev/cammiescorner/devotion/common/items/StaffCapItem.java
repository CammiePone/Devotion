package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import net.minecraft.world.item.Item;

public class StaffCapItem extends Item {
	public StaffCapItem() {
		super(new Properties().component(DevotionData.STAFF_CAP.get(), DevotionStaffCaps.IRON_CAP.holder()));
	}
}
