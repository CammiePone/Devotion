package dev.cammiescorner.devotion.client;

import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import dev.upcraft.sparkweave.api.platform.Services;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.ModelResourceLocation;
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

	public static ModelResourceLocation getCoreItemModelLocation(StaffCore core) {
		return duck.getCoreItemModelLocation(core);
	}

	public static ModelResourceLocation getCapItemModelLocation(StaffCap cap) {
		return duck.getCapItemModelLocation(cap);
	}

	public static ModelResourceLocation getCoreStaffModelLocation(StaffCore core) {
		return duck.getCoreStaffModelLocation(core);
	}

	public static ModelResourceLocation getCapStaffModelLocation(StaffCap cap) {
		return duck.getCapStaffModelLocation(cap);
	}

	public static boolean hasSpecificTranslation(String specificTranslation) {
		return I18n.exists(specificTranslation);
	}
}
