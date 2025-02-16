package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StaffCapItem extends Item {
	public StaffCapItem() {
		super(new Properties().component(DevotionData.STAFF_CAP.get(), DevotionStaffCaps.IRON_CAP.holder()));
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		String cap = DevotionRegistries.STAFF_CAPS.getKey(stack.get(DevotionData.STAFF_CAP.get()).value()).getPath();

		return String.format("item.devotion.%s_cap", cap);
	}
}
