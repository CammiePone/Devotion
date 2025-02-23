package dev.cammiescorner.devotion.fabric.entrypoints;

import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.api.world.AuraNode;
import dev.cammiescorner.devotion.client.DevotionClient;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import dev.cammiescorner.devotion.fabric.client.models.item.StaffModel;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

@CalledByReflection
public class FabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModelLoadingPlugin.register(ctx -> {
			for(ResourceLocation location : DevotionStaffCores.REGISTRY.keySet()) {
				ctx.addModels(
					DevotionStaffCores.REGISTRY.get(location).getItemModelLocation(),
					DevotionStaffCores.REGISTRY.get(location).getStaffModelLocation()
				);
			}

			for(ResourceLocation location : DevotionStaffCaps.REGISTRY.keySet()) {
				ctx.addModels(
					DevotionStaffCaps.REGISTRY.get(location).getItemModelLocation(),
					DevotionStaffCaps.REGISTRY.get(location).getStaffModelLocation()
				);
			}

			ctx.modifyModelOnLoad().register((unbakedModel, context) -> {
				if(DevotionClient.STAFF_RESOURCE_LOCATION.equals(context.topLevelId())) {
					Map<StaffCore, UnbakedModel> coreModels = new HashMap<>();
					Map<StaffCap, UnbakedModel> capModels = new HashMap<>();

					for(ResourceLocation location : DevotionStaffCores.REGISTRY.keySet()) {
						StaffCore core = DevotionStaffCores.REGISTRY.get(location);
						coreModels.put(core, context.getOrLoadModel(DevotionStaffCores.REGISTRY.get(location).getStaffModelLocation()));
					}

					for(ResourceLocation location : DevotionStaffCaps.REGISTRY.keySet()) {
						StaffCap staffCap = DevotionStaffCaps.REGISTRY.get(location);
						capModels.put(staffCap, context.getOrLoadModel(DevotionStaffCaps.REGISTRY.get(location).getStaffModelLocation()));
					}

					return new StaffModel(coreModels, capModels);
				}

				return unbakedModel;
			});
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
							Vec3 pos = blockPos.getCenter();

							// TODO replace with actual render
							level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x(), pos.y(), pos.z(), 0, 0, 0);
						}
					}
				}
			}
		});
	}
}
