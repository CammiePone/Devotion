package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StaffItem extends Item {
	public StaffItem() {
		super(new Properties().stacksTo(1).component(DevotionData.STAFF_CORE.get(), DevotionStaffCores.OAK_CORE.holder()).component(DevotionData.STAFF_CAP.get(), DevotionStaffCaps.IRON_CAP.holder()));
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		String core = DevotionStaffCores.REGISTRY.getKey(stack.get(DevotionData.STAFF_CORE.get()).value()).getPath();
		String cap = DevotionStaffCaps.REGISTRY.getKey(stack.get(DevotionData.STAFF_CAP.get()).value()).getPath();

		return String.format("item.devotion.%s_capped_%s_staff", cap, core);
	}
}
