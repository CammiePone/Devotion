package dev.cammiescorner.devotion.fabric.entrypoints;

import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.resources.ResourceLocation;

@CalledByReflection
public class FabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModelLoadingPlugin.register(ctx -> {
			for(ResourceLocation id : DevotionStaffCores.REGISTRY.keySet())
				ctx.addModels(id.withPrefix("item/staff_core/"));
			for(ResourceLocation id : DevotionStaffCaps.REGISTRY.keySet())
				ctx.addModels(id.withPrefix("item/staff_cap/"));
		});
	}
}
