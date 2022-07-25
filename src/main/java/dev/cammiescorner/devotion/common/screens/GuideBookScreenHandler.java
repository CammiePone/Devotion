package dev.cammiescorner.devotion.common.screens;

import dev.cammiescorner.devotion.common.registry.DevotionItems;
import dev.cammiescorner.devotion.common.registry.DevotionScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class GuideBookScreenHandler extends ScreenHandler {
	private final PlayerInventory inventory;

	public GuideBookScreenHandler(int syncId, PlayerInventory inventory) {
		super(DevotionScreenHandlers.GUIDE_BOOK_SCREEN_HANDLER, syncId);
		this.inventory = inventory;
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		return null;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return player.getMainHandStack().isOf(DevotionItems.SCRIPTS_OF_DEVOTION) || player.getOffHandStack().isOf(DevotionItems.SCRIPTS_OF_DEVOTION);
	}
}
