package dev.cammiescorner.devotion.api.events.client;

import dev.cammiescorner.devotion.client.screens.GuideBookScreen;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.base.api.event.Event;

import java.util.LinkedHashMap;

public class GuideBookScreenCallback {
	public static final Event<AddResearchEvent> ADD_RESEARCH = Event.create(AddResearchEvent.class, callbacks -> (screen, x, y) -> {
		for(AddResearchEvent callback : callbacks)
			callback.addWidgets(screen, x, y);
	});

	public static final Event<AddTabEvent> ADD_TAB = Event.create(AddTabEvent.class, callbacks -> map -> {
		for(AddTabEvent callback : callbacks)
			callback.addTabs(map);
	});

	public interface AddResearchEvent {
		void addWidgets(GuideBookScreen screen, int x, int y);
	}

	public interface AddTabEvent {
		void addTabs(LinkedHashMap<Identifier, Item> map);
	}
}
