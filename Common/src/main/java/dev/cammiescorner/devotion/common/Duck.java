package dev.cammiescorner.devotion.common;

import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.api.world.AuraNode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Map;
import java.util.Set;

public interface Duck {
	float getAura(LivingEntity entity, AuraType auraType);

	void setAura(LivingEntity entity, AuraType auraType, float amount);

	long lastTimeAuraChanged(LivingEntity entity);

	AuraType getPrimaryAuraType(LivingEntity entity);

	void setPrimaryAuraType(LivingEntity entity, AuraType primaryAuraType);

	float getAuraAlpha(LivingEntity entity, AuraType auraType);

	boolean drainAura(LivingEntity entity, AuraType auraType, float amount, boolean simulate);

	boolean regenAura(LivingEntity entity, AuraType auraType, float amount, boolean simulate);

	Set<ResourceLocation> getResearchIds(Player player);

	boolean giveResearch(Player player, Research research, boolean simulate);

	boolean revokeResearch(Player player, Research research, boolean simulate);

	void addAuraNode(ChunkAccess access, BlockPos pos, AuraNode node);

	void removeAuraNode(ChunkAccess access, BlockPos pos);

	Map<BlockPos, AuraNode> getAuraNodeMap(ChunkAccess access);

	int getMaxAuraNodes(ChunkAccess access);

	float getAuraAffinity(ChunkAccess access);
}
