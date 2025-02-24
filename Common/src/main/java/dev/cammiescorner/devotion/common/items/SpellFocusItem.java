package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionSpellFoci;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SpellFocusItem extends Item {
	public SpellFocusItem() {
		super(new Properties().stacksTo(1).component(DevotionData.SPELL_FOCUS.get(), DevotionSpellFoci.BLANK.holder()));
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		return stack.get(DevotionData.SPELL_FOCUS.get()).value().getDescriptionId();
	}
}
