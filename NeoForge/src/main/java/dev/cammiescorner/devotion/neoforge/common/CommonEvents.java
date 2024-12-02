package dev.cammiescorner.devotion.neoforge.common;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.neoforge.common.attachments.entity.AuraAttachment;
import dev.cammiescorner.devotion.neoforge.entrypoints.NeoMain;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = Devotion.MOD_ID)
public class CommonEvents {
	@SubscribeEvent
	public static void registerEntitiesForAttachments(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();

		if(AuraAttachment.isAuraProvider(entity))
			entity.getData(NeoMain.AURA);

		if(entity instanceof Player)
			entity.getData(NeoMain.KNOWN_RESEARCH);
	}
}
