package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import net.minecraft.world.item.Item;

public class StaffItem extends Item {
	public StaffItem() {
		super(new Properties().stacksTo(1).component(DevotionData.STAFF_CORE.get(), DevotionStaffCores.OAK_CORE.holder()).component(DevotionData.STAFF_CAP.get(), DevotionStaffCaps.IRON_CAP.holder()));
	}
}
