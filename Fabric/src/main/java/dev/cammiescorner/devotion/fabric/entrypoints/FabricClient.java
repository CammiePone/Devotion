package dev.cammiescorner.devotion.fabric.entrypoints;

import dev.cammiescorner.devotion.api.world.AuraNode;
import dev.cammiescorner.devotion.client.DevotionClient;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Map;

@CalledByReflection
public class FabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModelLoadingPlugin.register(ctx -> {
			for(ResourceLocation id : DevotionStaffCores.REGISTRY.keySet())
				ctx.addModels(id.withPrefix("item/staff_core/"));
			for(ResourceLocation id : DevotionStaffCaps.REGISTRY.keySet())
				ctx.addModels(id.withPrefix("item/staff_cap/"));
		});

		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			LocalPlayer player = DevotionClient.client.player;

			if(player != null && player.isHolding(DevotionItems.AURAMETER.get())) {
				ClientLevel level = context.world();
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
		});
	}
}
