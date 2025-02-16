package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import net.minecraft.world.item.Item;

public class StaffCoreItem extends Item {
	public StaffCoreItem() {
		super(new Properties().component(DevotionData.STAFF_CORE.get(), DevotionStaffCores.OAK_CORE.holder()));
	}
}
