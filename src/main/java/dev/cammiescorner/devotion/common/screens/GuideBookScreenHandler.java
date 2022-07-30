package dev.cammiescorner.devotion.common.screens;

import dev.cammiescorner.devotion.common.registry.DevotionItems;
import dev.cammiescorner.devotion.common.registry.DevotionScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class GuideBookScreenHandler extends ScreenHandler {
	public GuideBookScreenHandler(int syncId, PlayerInventory inventory) {
		super(DevotionScreenHandlers.GUIDE_BOOK_SCREEN_HANDLER, syncId);

		// fixes mojank
		for(int i = 0; i < 36; i++)
			addSlot(new Slot(inventory, i, -500, -500));
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return player.getMainHandStack().isOf(DevotionItems.SCRIPTS_OF_DEVOTION) || player.getOffHandStack().isOf(DevotionItems.SCRIPTS_OF_DEVOTION);
	}
}
