package dev.cammiescorner.devotion.fabric.entrypoints;

import dev.cammiescorner.devotion.api.spells.SpellFocus;
import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.client.DevotionClient;
import dev.cammiescorner.devotion.client.renderers.AuraNodeRenderer;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import dev.cammiescorner.devotion.common.registries.DevotionSpellFoci;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import dev.cammiescorner.devotion.fabric.client.models.item.FabricStaffModel;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.HashMap;
import java.util.Map;

import static dev.cammiescorner.devotion.client.DevotionClient.AURA_NODE_RENDERERS;

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

			for(ResourceLocation location : DevotionSpellFoci.REGISTRY.keySet()) {
				ctx.addModels(
					DevotionSpellFoci.REGISTRY.get(location).getItemModelLocation(),
					DevotionSpellFoci.REGISTRY.get(location).getStaffModelLocation()
				);
			}

			ctx.modifyModelOnLoad().register((unbakedModel, context) -> {
				if(DevotionClient.STAFF_RESOURCE_LOCATION.equals(context.topLevelId())) {
					Map<StaffCore, UnbakedModel> coreModels = new HashMap<>();
					Map<StaffCap, UnbakedModel> capModels = new HashMap<>();
					Map<SpellFocus, UnbakedModel> focusModels = new HashMap<>();

					for(ResourceLocation location : DevotionStaffCores.REGISTRY.keySet()) {
						StaffCore staffCore = DevotionStaffCores.REGISTRY.get(location);
						coreModels.put(staffCore, context.getOrLoadModel(staffCore.getStaffModelLocation()));
					}

					for(ResourceLocation location : DevotionStaffCaps.REGISTRY.keySet()) {
						StaffCap staffCap = DevotionStaffCaps.REGISTRY.get(location);
						capModels.put(staffCap, context.getOrLoadModel(staffCap.getStaffModelLocation()));
					}

					for(ResourceLocation location : DevotionSpellFoci.REGISTRY.keySet()) {
						SpellFocus spellFocus = DevotionSpellFoci.REGISTRY.get(location);
						focusModels.put(spellFocus, context.getOrLoadModel(spellFocus.getStaffModelLocation()));
					}

					return new FabricStaffModel(coreModels, capModels, focusModels);
				}

				return unbakedModel;
			});
		});

		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			LocalPlayer player = Minecraft.getInstance().player;

			if(player != null && player.isHolding(DevotionItems.AURAMETER.get())) {
				ClientLevel level = context.world();
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

				for(AuraNodeRenderer renderer : AURA_NODE_RENDERERS)
					renderer.render(context.camera(), context.tickCounter().getGameTimeDeltaTicks(), level.getLightEmission(renderer.getBlockPos()));
			}
		});
	}
}
