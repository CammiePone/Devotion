package dev.cammiescorner.devotion.fabric.client;

import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.client.ClientDuck;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingConstants;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class ClientFabricDuck implements ClientDuck {
	@Override
	public ModelResourceLocation getStaffCoreModel(StaffCore core) {
		ResourceLocation id = DevotionStaffCores.REGISTRY.getKey(core);

		return ModelLoadingConstants.toResourceModelId(DevotionStaffCores.REGISTRY.get(id).getItemModelLocation());
	}

	@Override
	public ModelResourceLocation getStaffCapModel(StaffCap cap) {
		ResourceLocation id = DevotionStaffCaps.REGISTRY.getKey(cap);

		return ModelLoadingConstants.toResourceModelId(DevotionStaffCaps.REGISTRY.get(id).getItemModelLocation());
	}
}
