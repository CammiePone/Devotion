package dev.cammiescorner.devotion.common;

import dev.cammiescorner.devotion.api.DevotionHelper;
import dev.cammiescorner.devotion.common.components.world.AltarStructureComponent;
import dev.cammiescorner.devotion.common.registry.DevotionCommands;
import dev.cammiescorner.devotion.common.registry.DevotionComponents;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.minecraft.world.World;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.lifecycle.api.event.ServerWorldLoadEvents;
import org.quiltmc.qsl.networking.api.ServerPlayConnectionEvents;
import org.quiltmc.qsl.resource.loader.api.ResourceLoaderEvents;

public class CommonEvents {
	public static void events() {
		CommandRegistrationCallback.EVENT.register(DevotionCommands::init);

		ServerWorldLoadEvents.LOAD.register((server, world) -> DevotionHelper.constructStructureMap(world));
		ResourceLoaderEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
			if(server != null)
				server.getWorlds().forEach(DevotionHelper::constructStructureMap);
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			World world = handler.player.world;
			ComponentKey<AltarStructureComponent> key = DevotionComponents.ALTAR_STRUCTURE_COMPONENT;
			key.sync(world, key.get(world), player -> player.getUuid().equals(handler.player.getUuid()));
		});
	}
}
