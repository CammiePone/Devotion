package dev.cammiescorner.devotion.neoforge.client;

import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.client.ClientDuck;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class ClientNeoDuck implements ClientDuck {
	@Override
	public ModelResourceLocation getStaffCoreModel(StaffCore core) {
		ResourceLocation id = DevotionStaffCores.REGISTRY.getKey(core);

		return ModelResourceLocation.standalone(id.withPrefix("item/staff_core/"));
	}

	@Override
	public ModelResourceLocation getStaffCapModel(StaffCap cap) {
		ResourceLocation id = DevotionStaffCaps.REGISTRY.getKey(cap);

		return ModelResourceLocation.standalone(id.withPrefix("item/staff_cap/"));
	}
}
