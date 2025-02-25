package dev.cammiescorner.devotion.common.world;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.api.world.AuraNode;
import dev.cammiescorner.devotion.common.MainHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.*;
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
			List<AuraType> auraTypes = new ArrayList<>(List.of(AuraType.ENHANCEMENT, AuraType.TRANSMUTATION, AuraType.EMISSION, AuraType.CONJURATION, AuraType.MANIPULATION));
			Map<AuraType, Float> auraMap = new HashMap<>();
			float auraAffinity = MainHelper.getAuraAffinity(access);
			float maximumAura = 256 * auraAffinity;
			int maxAuraTypes = random.nextInt(5);

			Collections.shuffle(auraTypes);
			auraTypes.forEach(auraType -> auraMap.put(auraType, 0f));

			for(int i = 0; i < maxAuraTypes; i++) {
				AuraType type = auraTypes.get(i);
				float aura = maximumAura * random.nextFloat();

				auraMap.replace(type, i == maxAuraTypes - 1 ? aura : maximumAura);
				maximumAura -= aura;
			}

			int offsetX = random.nextInt(16);
			int offsetY = random.nextInt(3, 6);
			int offsetZ = random.nextInt(16);

			MainHelper.addAuraNode(access, context.origin().offset(offsetX, offsetY, offsetZ), new AuraNode(auraMap.get(AuraType.ENHANCEMENT), auraMap.get(AuraType.TRANSMUTATION), auraMap.get(AuraType.EMISSION), auraMap.get(AuraType.CONJURATION), auraMap.get(AuraType.MANIPULATION)));

			return true;
		}

		return false;
	}
}
