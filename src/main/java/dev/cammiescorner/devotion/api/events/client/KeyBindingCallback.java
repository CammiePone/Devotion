package dev.cammiescorner.devotion.api.events.client;

import com.mojang.blaze3d.platform.InputUtil;
import org.quiltmc.qsl.base.api.event.Event;

public class KeyBindingCallback {
	public static final Event<UnpressedEvent> UNPRESSED = Event.create(UnpressedEvent.class, callbacks -> (key, modifier) -> {
		for(UnpressedEvent callback : callbacks)
			callback.unpress(key, modifier);
	});

	public interface UnpressedEvent {
		void unpress(InputUtil.Key key, int modifiers);
	}
}
