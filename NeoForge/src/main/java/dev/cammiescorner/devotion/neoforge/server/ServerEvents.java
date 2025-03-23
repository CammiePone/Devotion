package dev.cammiescorner.devotion.neoforge.server;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.book.BookTab;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.api.research.Research;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = Devotion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ServerEvents {
	@SubscribeEvent
	public static void registerDynamicRegistries(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(DevotionRegistries.BOOK_TAB, BookTab.DIRECT_CODEC, BookTab.DIRECT_CODEC);
		event.dataPackRegistry(DevotionRegistries.RESEARCH, Research.DIRECT_CODEC, Research.DIRECT_CODEC);
	}
}
