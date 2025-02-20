package dev.cammiescorner.devotion.common.world;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.api.world.AuraNode;
import dev.cammiescorner.devotion.common.MainHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Map;
import java.util.random.RandomGenerator;

public class AuraNodeFeature extends Feature<NoneFeatureConfiguration> {
	public AuraNodeFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		ChunkAccess access = level.getChunk(context.origin());
		Map<BlockPos, AuraNode> auraNodeMap = MainHelper.getAuraNodes(access);
		RandomGenerator random = RandomGenerator.getDefault();

		if(auraNodeMap.size() < MainHelper.getMaxAuraNodes(access)) {
			float auraAffinity = MainHelper.getAuraAffinity(access);
			float enhancementAura = Math.max(random.nextFloat(-50f, 100f), 0f) * auraAffinity;
			float transmutationAura = Math.max(random.nextFloat(-50f, 100f), 0f) * auraAffinity;
			float emissionAura = Math.max(random.nextFloat(-50f, 100f), 0f) * auraAffinity;
			float conjurationAura = Math.max(random.nextFloat(-50f, 100f), 0f) * auraAffinity;
			float manipulationAura = Math.max(random.nextFloat(-50f, 100f), 0f) * auraAffinity;
			int offsetX = random.nextInt(16);
			int offsetY = random.nextInt(3, 6);
			int offsetZ = random.nextInt(16);

			level.setBlock(context.origin().offset(offsetX, offsetY - 1, offsetZ), Blocks.GLOWSTONE.defaultBlockState(), 3);
			MainHelper.addAuraNode(access, context.origin().offset(offsetX, offsetY, offsetZ), new AuraNode(enhancementAura, transmutationAura, emissionAura, conjurationAura, manipulationAura));

			return true;
		}

		return false;
	}
}
