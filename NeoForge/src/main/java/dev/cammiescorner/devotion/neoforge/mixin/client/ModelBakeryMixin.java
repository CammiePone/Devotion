package dev.cammiescorner.devotion.neoforge.mixin.client;

import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.client.DevotionClient;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import dev.cammiescorner.devotion.neoforge.client.models.item.NeoStaffModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {
	@Shadow abstract UnbakedModel getModel(ResourceLocation modelLocation);

	@ModifyVariable(method = "registerModelAndLoadDependencies", at = @At("HEAD"), argsOnly = true)
	private UnbakedModel loadCustomStaffModel(UnbakedModel unbakedModel, ModelResourceLocation modelLocation) {
		if(DevotionClient.STAFF_RESOURCE_LOCATION.equals(modelLocation)) {
			Map<StaffCore, UnbakedModel> coreModels = new HashMap<>();
			Map<StaffCap, UnbakedModel> capModels = new HashMap<>();

			for(ResourceLocation location : DevotionStaffCores.REGISTRY.keySet()) {
				StaffCore core = DevotionStaffCores.REGISTRY.get(location);
				coreModels.put(core, getModel(DevotionStaffCores.REGISTRY.get(location).getStaffModelLocation()));
			}

			for(ResourceLocation location : DevotionStaffCaps.REGISTRY.keySet()) {
				StaffCap staffCap = DevotionStaffCaps.REGISTRY.get(location);
				capModels.put(staffCap, getModel(DevotionStaffCaps.REGISTRY.get(location).getStaffModelLocation()));
			}

			return new NeoStaffModel(coreModels, capModels);
		}

		return unbakedModel;
	}
}
