package dev.cammiescorner.devotion.fabric.entrypoints;

import dev.cammiescorner.devotion.api.registries.DevotionRegistryKeys;
import dev.cammiescorner.devotion.api.research.BookEntry;
import dev.cammiescorner.devotion.api.research.BookTab;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

@CalledByReflection
public class FabricMain implements ModInitializer {
	@Override
	public void onInitialize() {
		DynamicRegistries.registerSynced(DevotionRegistryKeys.RESEARCH, Research.DIRECT_CODEC);
		DynamicRegistries.registerSynced(DevotionRegistryKeys.BOOK_TAB, BookTab.DIRECT_CODEC);
		DynamicRegistries.registerSynced(DevotionRegistryKeys.BOOK_ENTRY, BookEntry.DIRECT_CODEC);

		ServerPlayerEvents.COPY_FROM.register(RespawnCopyStrategy.EVENT_PHASE, (oldPlayer, newPlayer, alive) -> {
			AuraType primaryAuraType = MainHelper.getPrimaryAuraType(oldPlayer);
			MainHelper.setPrimaryAuraType(newPlayer, primaryAuraType);

			// CONSIDER uncomment this if we don't use the player's aura to fill the pillars
//			for(AuraType auraType : AuraType.values())
//				MainHelper.setAura(newPlayer, auraType, AuraComponent.MAX_AURA * auraType.getAffinityMultiplier(primaryAuraType));
		});
	}
}
