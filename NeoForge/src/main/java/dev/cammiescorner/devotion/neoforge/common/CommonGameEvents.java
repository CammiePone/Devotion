package dev.cammiescorner.devotion.neoforge.common;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.neoforge.common.attachments.entity.AuraAttachment;
import dev.cammiescorner.devotion.neoforge.entrypoints.NeoMain;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;

@EventBusSubscriber(modid = Devotion.MOD_ID)
public class CommonGameEvents {
	@SubscribeEvent
	public static void registerEntitiesForAttachments(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();

		if(AuraAttachment.isAuraProvider(entity))
			entity.getData(NeoMain.AURA);

		if(entity instanceof Player)
			entity.getData(NeoMain.KNOWN_RESEARCH);
	}

	@SubscribeEvent
	public static void syncAuraNodes(ChunkEvent.Load event) {
		if(event.getChunk() instanceof LevelChunk levelChunk && event.getLevel() instanceof ServerLevel)
			levelChunk.getData(NeoMain.AURA_NODE).sync();
	}

	@SubscribeEvent
	public static void copyAuraStuff(PlayerEvent.Clone event) {
		Player oldPlayer = event.getOriginal();
		Player newPlayer = event.getEntity();

		AuraType primaryAuraType = MainHelper.getPrimaryAuraType(oldPlayer);
		MainHelper.setPrimaryAuraType(newPlayer, primaryAuraType);

		if(!event.isWasDeath()) {
			for(AuraType auraType : AuraType.values())
				MainHelper.setAura(newPlayer, auraType, MainHelper.getAura(oldPlayer, auraType));
		}
		// CONSIDER uncomment this if we don't use the player's aura to fill the pillars
//		else {
//			for(AuraType auraType : AuraType.values())
//				MainHelper.setAura(newPlayer, auraType, AuraAttachment.MAX_AURA * auraType.getAffinityMultiplier(primaryAuraType));
//		}
	}
}
