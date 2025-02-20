package dev.cammiescorner.devotion.fabric.entrypoints;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.api.research.BookEntry;
import dev.cammiescorner.devotion.api.research.BookTab;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

@CalledByReflection
public class FabricMain implements ModInitializer {
	@Override
	public void onInitialize() {
		DynamicRegistries.registerSynced(DevotionRegistries.RESEARCH, Research.DIRECT_CODEC);
		DynamicRegistries.registerSynced(DevotionRegistries.BOOK_TAB, BookTab.DIRECT_CODEC);
		DynamicRegistries.registerSynced(DevotionRegistries.BOOK_ENTRY, BookEntry.DIRECT_CODEC);

		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.SURFACE_STRUCTURES, ResourceKey.create(Registries.PLACED_FEATURE, Devotion.id("aura_node")));

		ServerPlayerEvents.COPY_FROM.register(RespawnCopyStrategy.EVENT_PHASE, (oldPlayer, newPlayer, alive) -> {
			AuraType primaryAuraType = MainHelper.getPrimaryAuraType(oldPlayer);
			MainHelper.setPrimaryAuraType(newPlayer, primaryAuraType);

			// CONSIDER uncomment this if we don't use the player's aura to fill the pillars
//			for(AuraType auraType : AuraType.values())
//				MainHelper.setAura(newPlayer, auraType, AuraComponent.MAX_AURA * auraType.getAffinityMultiplier(primaryAuraType));
		});
	}
}
