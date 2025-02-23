package dev.cammiescorner.devotion.fabric.client;

import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.client.ClientDuck;
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingConstants;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class ClientFabricDuck implements ClientDuck {
	@Override
	public ModelResourceLocation getCoreItemModelLocation(StaffCore core) {
		return ModelLoadingConstants.toResourceModelId(core.getItemModelLocation());
	}

	@Override
	public ModelResourceLocation getCapItemModelLocation(StaffCap cap) {
		return ModelLoadingConstants.toResourceModelId(cap.getItemModelLocation());
	}

	@Override
	public ModelResourceLocation getCoreStaffModelLocation(StaffCore core) {
		return ModelLoadingConstants.toResourceModelId(core.getStaffModelLocation());
	}

	@Override
	public ModelResourceLocation getCapStaffModelLocation(StaffCap cap) {
		return ModelLoadingConstants.toResourceModelId(cap.getStaffModelLocation());
	}
}
