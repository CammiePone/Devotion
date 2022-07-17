package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.Devotion;
import net.minecraft.item.Item;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class GuideBookItem extends Item {
	public GuideBookItem() {
		super(new QuiltItemSettings().group(Devotion.ITEM_GROUP).maxCount(1));
	}
}
