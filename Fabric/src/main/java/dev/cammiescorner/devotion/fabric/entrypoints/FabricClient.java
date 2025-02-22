package dev.cammiescorner.devotion.fabric.entrypoints;

import dev.cammiescorner.devotion.Devotion;
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
import net.minecraft.client.resources.model.BakedModel;
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
			for(ResourceLocation id : DevotionStaffCores.REGISTRY.keySet()) {
				ctx.addModels(id.withPrefix("item/staff_core/"));
				ctx.addModels(id.withPrefix("staff_part/core/"));
			}

			for(ResourceLocation id : DevotionStaffCaps.REGISTRY.keySet()) {
				ctx.addModels(id.withPrefix("item/staff_cap/"));
				ctx.addModels(id.withPrefix("staff_part/cap/"));
			}

			ctx.modifyModelAfterBake().register((bakedModel, context) -> {
				if(context.resourceId() != null && context.resourceId().equals(Devotion.id("item/staff"))) {
					Map<StaffCore, BakedModel> coreModels = new HashMap<>();
					Map<StaffCap, BakedModel> capModels = new HashMap<>();

					for(ResourceLocation location : DevotionStaffCores.REGISTRY.keySet()) {
						StaffCore core = DevotionStaffCores.REGISTRY.get(location);
						coreModels.put(core, context.baker().bake(ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "staff_part/core/" + location.getPath()), context.settings()));
					}

					for(ResourceLocation location : DevotionStaffCaps.REGISTRY.keySet()) {
						StaffCap staffCap = DevotionStaffCaps.REGISTRY.get(location);
						capModels.put(staffCap, context.baker().bake(ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "staff_part/cap/" + location.getPath()), context.settings()));
					}

					return new StaffModel(coreModels, capModels);
				}

				return bakedModel;
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
