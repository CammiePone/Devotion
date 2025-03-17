package dev.cammiescorner.devotion.neoforge.client;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.renderers.AuraNodeRenderer;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import static dev.cammiescorner.devotion.client.DevotionClient.AURA_NODE_RENDERERS;


@EventBusSubscriber(modid = Devotion.MOD_ID)
public class ClientGameEvents {
	@SubscribeEvent
	public static void renderAuraNodes(RenderLevelStageEvent event) {
		if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			LocalPlayer player = Minecraft.getInstance().player;

			if(player != null && player.isHolding(DevotionItems.AURAMETER.get())) {
				ClientLevel level = Minecraft.getInstance().level;
				ClientChunkCache chunkCache = level.getChunkSource();
				ClientChunkCache.Storage storage = chunkCache.storage;

				for(int i = 0; i < storage.chunks.length(); i++) {
					LevelChunk chunk = storage.chunks.get(i);

					if(chunk != null) {
						for(BlockPos blockPos : MainHelper.getAuraNodes(chunk).keySet()) {
							if(AURA_NODE_RENDERERS.stream().noneMatch(auraNodeRenderer -> auraNodeRenderer.getBlockPos().equals(blockPos)))
								AURA_NODE_RENDERERS.add(new AuraNodeRenderer(level, blockPos));
						}
					}
				}

				// TODO clean auraNodeRenderers when not rendering that blockpos
				for(AuraNodeRenderer renderer : AURA_NODE_RENDERERS)
					renderer.render(event.getCamera(), event.getPartialTick().getGameTimeDeltaTicks(), level.getLightEmission(renderer.getBlockPos()));
			}
		}
	}
}
