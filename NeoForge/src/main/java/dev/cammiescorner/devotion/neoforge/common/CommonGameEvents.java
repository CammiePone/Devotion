package dev.cammiescorner.devotion.neoforge.common;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.neoforge.common.attachments.entity.AuraAttachment;
import dev.cammiescorner.devotion.neoforge.entrypoints.NeoMain;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;

@EventBusSubscriber(modid = Devotion.MOD_ID)
public class CommonGameEvents {
	@SubscribeEvent
	public static void registerEntitiesForAttachments(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();

		if(AuraAttachment.isAuraProvider(entity))
			entity.getData(NeoMain.AURA);

		if(entity instanceof Player)
			entity.getData(NeoMain.KNOWN_RESEARCH);

		if(entity instanceof ServerPlayer player) {
			RegistryAccess access = player.registryAccess();

			for(Research research : access.registryOrThrow(DevotionRegistries.RESEARCH)) {
				if(research.knownByDefault() && !MainHelper.getResearchIds(player).contains(research.getId(access)))
					MainHelper.giveResearch(player, research, false);
			}
		}
	}

	@SubscribeEvent
	public static void syncAuraNodes(ChunkWatchEvent.Sent event) {
		if(event.getLevel() instanceof ServerLevel) {
			LevelChunk levelChunk = event.getChunk();
			levelChunk.getData(NeoMain.AURA_NODE).sync();
		}
	}

	@SubscribeEvent
	public static void copyAuraStuff(PlayerEvent.Clone event) {
		Player oldPlayer = event.getOriginal();
		Player newPlayer = event.getEntity();
		AuraType primaryAuraType = MainHelper.getPrimaryAuraType(oldPlayer);

		MainHelper.setPrimaryAuraType(newPlayer, primaryAuraType);

		for(AuraType auraType : AuraType.values())
			MainHelper.setAura(newPlayer, auraType, MainHelper.getAura(oldPlayer, auraType));
	}
}
