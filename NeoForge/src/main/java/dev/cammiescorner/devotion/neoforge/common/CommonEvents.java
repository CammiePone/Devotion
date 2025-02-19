package dev.cammiescorner.devotion.neoforge.common;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.api.world.AuraNode;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.neoforge.common.attachments.chunk.AuraNodeAttachment;
import dev.cammiescorner.devotion.neoforge.common.attachments.entity.AuraAttachment;
import dev.cammiescorner.devotion.neoforge.entrypoints.NeoMain;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = Devotion.MOD_ID)
public class CommonEvents {
	@SubscribeEvent
	public static void registerEntitiesForAttachments(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();

		if(AuraAttachment.isAuraProvider(entity))
			entity.getData(NeoMain.AURA);

		if(entity instanceof Player)
			entity.getData(NeoMain.KNOWN_RESEARCH);
	}

	@SubscribeEvent
	public static void levelTick(LevelTickEvent event) {
		if(event.getLevel() instanceof ServerLevel level) {
			for(long forcedChunk : level.getForcedChunks()) {
				ChunkAccess access = level.getChunk(ChunkPos.getX(forcedChunk), ChunkPos.getZ(forcedChunk));
				AuraNodeAttachment attachment = access.getData(NeoMain.AURA_NODE);

				if(attachment.getAuraNodeMap().size() < attachment.getMaxAuraNodes()) { // TODO add timer
					float enhancementAura = Math.max(attachment.getRandom().nextFloat(-50f, 100f), 0f) * attachment.getAuraAffinity();
					float transmutationAura = Math.max(attachment.getRandom().nextFloat(-50f, 100f), 0f) * attachment.getAuraAffinity();
					float emissionAura = Math.max(attachment.getRandom().nextFloat(-50f, 100f), 0f) * attachment.getAuraAffinity();
					float conjurationAura = Math.max(attachment.getRandom().nextFloat(-50f, 100f), 0f) * attachment.getAuraAffinity();
					float manipulationAura = Math.max(attachment.getRandom().nextFloat(-50f, 100f), 0f) * attachment.getAuraAffinity();
					BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

					while(attachment.getAuraNodeMap().keySet().stream().anyMatch(pos -> pos.distSqr(blockPos) < 25)) {
						int x = attachment.getRandom().nextInt(16);
						int z = attachment.getRandom().nextInt(16);
						int y = access.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) + attachment.getRandom().nextInt(3, 6);
						blockPos.set(x, y, z);
					}

					attachment.addAuraNode(blockPos, new AuraNode(enhancementAura, transmutationAura, emissionAura, conjurationAura, manipulationAura));
				}
			}
		}
	}

	@SubscribeEvent
	public static void copyAuraStuff(PlayerEvent.Clone event) {
		Player oldPlayer = event.getOriginal();
		Player newPlayer = event.getEntity();

		AuraType primaryAuraType = MainHelper.getPrimaryAuraType(oldPlayer);
		MainHelper.setPrimaryAuraType(newPlayer, primaryAuraType);

		if(!event.isWasDeath()) {
			for(AuraType auraType : AuraType.values())
				MainHelper.setAura(newPlayer, auraType, MainHelper.getAura(oldPlayer, auraType));
		}
		// CONSIDER uncomment this if we don't use the player's aura to fill the pillars
//		else {
//			for(AuraType auraType : AuraType.values())
//				MainHelper.setAura(newPlayer, auraType, AuraAttachment.MAX_AURA * auraType.getAffinityMultiplier(primaryAuraType));
//		}
	}
}
