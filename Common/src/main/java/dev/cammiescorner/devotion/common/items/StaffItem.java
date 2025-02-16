package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import net.minecraft.world.item.Item;

public class StaffItem extends Item {
	public StaffItem(StaffCore staffCore, StaffCap staffCaps) {
		super(new Properties().stacksTo(1).component(DevotionData.STAFF_CORE.get(), DevotionRegistries.STAFF_CORES.wrapAsHolder(staffCore)).component(DevotionData.STAFF_CAPS.get(), DevotionRegistries.STAFF_CAPS.wrapAsHolder(staffCaps)));
	}
}
