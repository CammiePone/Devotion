package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.api.wands.WandCap;
import dev.cammiescorner.devotion.api.wands.WandCore;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import net.minecraft.world.item.Item;

public class WandItem extends Item {
	public final WandCore wandCore;

	public WandItem(Properties properties, WandCore wandCore, WandCap defaultCaps) {
		super(properties.stacksTo(1).component(DevotionData.WAND_CAPS.get(), DevotionRegistries.WAND_CAPS.wrapAsHolder(defaultCaps)));
		this.wandCore = wandCore;
	}
}
