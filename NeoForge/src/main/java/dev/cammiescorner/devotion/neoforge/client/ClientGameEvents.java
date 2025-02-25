package dev.cammiescorner.devotion.neoforge.client;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.world.AuraNode;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import dev.cammiescorner.devotion.common.registries.DevotionParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.Map;



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
						Map<BlockPos, AuraNode> auraNodeMap = MainHelper.getAuraNodes(chunk);

						for(BlockPos blockPos : auraNodeMap.keySet()) {
							Vec3 pos = blockPos.getCenter();

							// TODO replace with actual render
							level.addParticle(DevotionParticles.AURA_NODE.get(), pos.x(), pos.y(), pos.z(), 0, 0, 0);
						}
					}
				}
			}
		}
	}
}
