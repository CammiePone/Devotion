package dev.cammiescorner.devotion.client;

import dev.cammiescorner.devotion.common.registries.DevotionItems;
import dev.upcraft.sparkweave.api.platform.Services;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ClientHelper {
	public static final ResourceLocation WHITE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/misc/white.png");
	private static final ClientDuck duck = Services.getService(ClientDuck.class);

	public static boolean shouldRenderAura(LivingEntity entity) {
		LocalPlayer player = DevotionClient.client.player;

		if(player != null && player != entity && player.isHolding(DevotionItems.AURAMETER.get()))
			return true;

		return entity.isHolding(DevotionItems.STAFF.get());
	}
}
