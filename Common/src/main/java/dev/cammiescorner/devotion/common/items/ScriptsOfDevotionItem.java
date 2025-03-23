package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.client.DevotionClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ScriptsOfDevotionItem extends Item {
	public ScriptsOfDevotionItem() {
		super(new Properties().stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack stack = player.getItemInHand(usedHand);

		if(level.isClientSide())
			Minecraft.getInstance().setScreen(DevotionClient.lastGuideBookScreen); // TODO make a packet, will crash on servers otherwise

		return InteractionResultHolder.success(stack);
	}
}
