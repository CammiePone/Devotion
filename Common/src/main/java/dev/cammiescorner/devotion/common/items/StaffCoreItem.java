package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StaffCoreItem extends Item {
	public StaffCoreItem() {
		super(new Properties().component(DevotionData.STAFF_CORE.get(), DevotionStaffCores.OAK_CORE.holder()));
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		return stack.get(DevotionData.STAFF_CORE.get()).value().getDescriptionId();
	}
}
