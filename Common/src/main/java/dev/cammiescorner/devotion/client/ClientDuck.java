package dev.cammiescorner.devotion.client;

import dev.cammiescorner.devotion.api.spells.SpellFocus;
import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import net.minecraft.client.resources.model.ModelResourceLocation;

public interface ClientDuck {
	ModelResourceLocation getCoreItemModelLocation(StaffCore core);

	ModelResourceLocation getCapItemModelLocation(StaffCap cap);

	ModelResourceLocation getFocusItemModelLocation(SpellFocus focus);

	ModelResourceLocation getCoreStaffModelLocation(StaffCore core);

	ModelResourceLocation getCapStaffModelLocation(StaffCap cap);

	ModelResourceLocation getFocusStaffModelLocation(SpellFocus focus);
}
