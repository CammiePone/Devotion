package dev.cammiescorner.devotion.neoforge.client;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.ClientHelper;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = Devotion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
	@SubscribeEvent
	public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
		for(ResourceLocation id : DevotionStaffCores.REGISTRY.keySet()) {
			event.register(ClientHelper.getCoreItemModelLocation(DevotionStaffCores.REGISTRY.get(id)));
			event.register(ClientHelper.getCoreStaffModelLocation(DevotionStaffCores.REGISTRY.get(id)));
		}

		for(ResourceLocation id : DevotionStaffCaps.REGISTRY.keySet()) {
			event.register(ClientHelper.getCapItemModelLocation(DevotionStaffCaps.REGISTRY.get(id)));
			event.register(ClientHelper.getCapStaffModelLocation(DevotionStaffCaps.REGISTRY.get(id)));
		}
	}
}
