package dev.cammiescorner.devotion.fabric.common;

import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.api.world.AuraNode;
import dev.cammiescorner.devotion.common.Duck;
import dev.cammiescorner.devotion.fabric.common.registries.DevotionComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Map;
import java.util.Set;

public class FabricDuck implements Duck {
	@Override
	public float getAura(LivingEntity entity, AuraType auraType) {
		return DevotionComponents.AURA.isProvidedBy(entity) ? entity.getComponent(DevotionComponents.AURA).getAura(auraType) : 0f;
	}

	@Override
	public void setAura(LivingEntity entity, AuraType auraType, float amount) {
		if(DevotionComponents.AURA.isProvidedBy(entity))
			entity.getComponent(DevotionComponents.AURA).setAura(auraType, amount);
	}

	@Override
	public AuraType getPrimaryAuraType(LivingEntity entity) {
		return DevotionComponents.AURA.isProvidedBy(entity) ? entity.getComponent(DevotionComponents.AURA).getPrimaryAuraType() : AuraType.NONE;
	}

	@Override
	public void setPrimaryAuraType(LivingEntity entity, AuraType primaryAuraType) {
		if(DevotionComponents.AURA.isProvidedBy(entity))
			entity.getComponent(DevotionComponents.AURA).setPrimaryAuraType(primaryAuraType);
	}

	@Override
	public long lastTimeAuraChanged(LivingEntity entity) {
		return DevotionComponents.AURA.isProvidedBy(entity) ? entity.getComponent(DevotionComponents.AURA).getLastTimeAuraChanged() : 0L;
	}

	@Override
	public float getAuraAlpha(LivingEntity entity, AuraType auraType) {
		return DevotionComponents.AURA.isProvidedBy(entity) ? entity.getComponent(DevotionComponents.AURA).getAuraAlpha() : 1f;
	}

	@Override
	public boolean drainAura(LivingEntity entity, AuraType auraType, float amount, boolean simulate) {
		return DevotionComponents.AURA.isProvidedBy(entity) && entity.getComponent(DevotionComponents.AURA).drainAura(auraType, amount, simulate);
	}

	@Override
	public boolean regenAura(LivingEntity entity, AuraType auraType, float amount, boolean simulate) {
		return DevotionComponents.AURA.isProvidedBy(entity) && entity.getComponent(DevotionComponents.AURA).regenAura(auraType, amount, simulate);
	}

	@Override
	public Set<ResourceLocation> getResearchIds(Player player) {
		return player.getComponent(DevotionComponents.KNOWN_RESEARCH).getResearchIds();
	}

	@Override
	public boolean giveResearch(Player player, Research research, boolean simulate) {
		return player.getComponent(DevotionComponents.KNOWN_RESEARCH).giveResearch(research, simulate);
	}

	@Override
	public boolean revokeResearch(Player player, Research research, boolean simulate) {
		return player.getComponent(DevotionComponents.KNOWN_RESEARCH).revokeResearch(research, simulate);
	}

	@Override
	public void addAuraNode(ChunkAccess access, BlockPos pos, AuraNode node) {
		access.getComponent(DevotionComponents.AURA_NODE).addAuraNode(pos, node);
	}

	@Override
	public void removeAuraNode(ChunkAccess access, BlockPos pos) {
		access.getComponent(DevotionComponents.AURA_NODE).removeAuraNode(pos);
	}

	@Override
	public Map<BlockPos, AuraNode> getAuraNodeMap(ChunkAccess access) {
		return access.getComponent(DevotionComponents.AURA_NODE).getAuraNodeMap();
	}
}
