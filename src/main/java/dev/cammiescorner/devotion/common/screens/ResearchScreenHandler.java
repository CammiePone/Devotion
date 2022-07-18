package dev.cammiescorner.devotion.common.screens;

import dev.cammiescorner.devotion.common.registry.DevotionScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class ResearchScreenHandler extends ScreenHandler {
	private final Inventory inventory;
	private final ItemStack stack;

	public ResearchScreenHandler(int syncId, Inventory inventory, ItemStack stack) {
		super(DevotionScreenHandlers.RESEARCH_SCREEN_HANDLER, syncId);
		this.inventory = inventory;
		this.stack = stack;
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if(!player.canModifyBlocks())
			return false;

		ItemStack stack = inventory.removeStack(0);
		inventory.markDirty();

		if(!player.getInventory().insertStack(stack))
			player.dropItem(stack, false);

		return true;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		return ItemStack.EMPTY;
	}

	public ItemStack getScroll() {
		return stack;
	}
}
