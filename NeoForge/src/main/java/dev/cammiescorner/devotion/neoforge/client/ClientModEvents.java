package dev.cammiescorner.devotion.neoforge.client;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = Devotion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
	@SubscribeEvent
	public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
		for(ResourceLocation id : DevotionStaffCores.REGISTRY.keySet())
			event.register(ModelResourceLocation.standalone(id.withPrefix("item/staff_core/")));
		for(ResourceLocation id : DevotionStaffCaps.REGISTRY.keySet())
			event.register(ModelResourceLocation.standalone(id.withPrefix("item/staff_cap/")));
	}
}
