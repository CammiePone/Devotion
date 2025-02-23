package dev.cammiescorner.devotion.neoforge.client;

import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.client.ClientDuck;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class ClientNeoDuck implements ClientDuck {
	@Override
	public ModelResourceLocation getCoreItemModelLocation(StaffCore core) {
		return ModelResourceLocation.standalone(core.getItemModelLocation());
	}

	@Override
	public ModelResourceLocation getCapItemModelLocation(StaffCap cap) {
		return ModelResourceLocation.standalone(cap.getItemModelLocation());
	}

	@Override
	public ModelResourceLocation getCoreStaffModelLocation(StaffCore core) {
		return ModelResourceLocation.standalone(core.getStaffModelLocation());
	}

	@Override
	public ModelResourceLocation getCapStaffModelLocation(StaffCap cap) {
		return ModelResourceLocation.standalone(cap.getStaffModelLocation());
	}
}
