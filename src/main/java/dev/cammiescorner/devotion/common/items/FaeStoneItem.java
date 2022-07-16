package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.Devotion;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.item.Item;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class FaeStoneItem extends Item implements FabricItem {
	public FaeStoneItem() {
		super(new QuiltItemSettings().maxCount(1).group(Devotion.ITEM_GROUP));
	}
}
