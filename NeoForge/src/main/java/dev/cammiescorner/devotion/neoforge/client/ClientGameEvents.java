package dev.cammiescorner.devotion.neoforge.client;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.world.AuraNode;
import dev.cammiescorner.devotion.client.DevotionClient;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.Map;

@EventBusSubscriber(modid = Devotion.MOD_ID)
public class ClientGameEvents {
	@SubscribeEvent
	public static void renderAuraNodes(RenderLevelStageEvent event) {
		if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			LocalPlayer player = DevotionClient.client.player;

			if(player != null && player.isHolding(DevotionItems.AURAMETER.get())) {
				ClientLevel level = DevotionClient.client.level;
				ClientChunkCache chunkCache = level.getChunkSource();
				ClientChunkCache.Storage storage = chunkCache.storage;

				for(int i = 0; i < storage.chunks.length(); i++) {
					LevelChunk chunk = storage.chunks.get(i);

					if(chunk != null) {
						Map<BlockPos, AuraNode> auraNodeMap = MainHelper.getAuraNodes(chunk);

						for(BlockPos blockPos : auraNodeMap.keySet()) {
							// TODO replace with actual render
							level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0, 0, 0);
						}
					}
				}
			}
		}
	}
}
