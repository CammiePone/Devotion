package dev.cammiescorner.devotion.common;

import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.api.world.AuraNode;
import dev.upcraft.sparkweave.api.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Map;
import java.util.Set;

public class MainHelper {
	private static final Duck duck = Services.getService(Duck.class);

	public static float getAura(LivingEntity entity, AuraType auraType) {
		return duck.getAura(entity, auraType);
	}

	public static void setAura(LivingEntity entity, AuraType auraType, float amount) {
		duck.setAura(entity, auraType, amount);
	}

	public static AuraType getPrimaryAuraType(LivingEntity entity) {
		return duck.getPrimaryAuraType(entity);
	}

	public static void setPrimaryAuraType(LivingEntity entity, AuraType auraType) {
		duck.setPrimaryAuraType(entity, auraType);
	}

	public static long lastTimeAuraChanged(LivingEntity entity) {
		return duck.lastTimeAuraChanged(entity);
	}

	public static float getAuraAlpha(LivingEntity entity, AuraType auraType) {
		return duck.getAuraAlpha(entity, auraType);
	}

	public static boolean regenAura(LivingEntity entity, AuraType auraType, float amount, boolean simulate) {
		return duck.regenAura(entity, auraType, amount, simulate);
	}

	public static boolean drainAura(LivingEntity entity, AuraType auraType, float amount, boolean simulate) {
		return duck.drainAura(entity, auraType, amount, simulate);
	}

	public static Set<ResourceLocation> getResearchIds(Player player) {
		return duck.getResearchIds(player);
	}

	public static boolean giveResearch(Player player, Research research, boolean simulate) {
		return duck.giveResearch(player, research, simulate);
	}

	public static boolean revokeResearch(Player player, Research research, boolean simulate) {
		return duck.revokeResearch(player, research, simulate);
	}

	public static void addAuraNode(ChunkAccess access, BlockPos pos, AuraNode node) {
		duck.addAuraNode(access, pos, node);
	}

	public static void removeAuraNode(ChunkAccess access, BlockPos pos) {
		duck.removeAuraNode(access, pos);
	}

	public static Map<BlockPos, AuraNode> getAuraNodes(ChunkAccess access) {
		return duck.getAuraNodeMap(access);
	}
}
