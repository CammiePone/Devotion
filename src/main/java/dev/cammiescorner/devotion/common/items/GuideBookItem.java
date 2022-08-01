package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.screens.GuideBookScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class GuideBookItem extends Item {
	public GuideBookItem() {
		super(new QuiltItemSettings().group(Devotion.ITEM_GROUP).maxCount(1));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);

		if(world.isClient()) {
			MinecraftClient client = MinecraftClient.getInstance();
			client.setScreen(new GuideBookScreen());
		}

		return TypedActionResult.success(stack);
	}
}
