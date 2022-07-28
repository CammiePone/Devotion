package dev.cammiescorner.devotion.api.events.client;

import dev.cammiescorner.devotion.client.screens.GuideBookScreen;
import org.quiltmc.qsl.base.api.event.Event;

public class ResearchWidgetCallback {
	public static final Event<AddWidgetEvent> ADD_WIDGETS = Event.create(AddWidgetEvent.class, callbacks -> (screen, x, y) -> {
		for(AddWidgetEvent callback : callbacks)
			callback.addWidgets(screen, x, y);
	});

	public interface AddWidgetEvent {
		void addWidgets(GuideBookScreen screen, int x, int y);
	}
}
