package dev.cammiescorner.devotion.fabric.client.models.item;

import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class StaffModel implements FabricBakedModel, BakedModel {
	private final Map<StaffCore, BakedModel> staffCoreModels;
	private final Map<StaffCap, BakedModel> staffCapsModels;

	public StaffModel(Map<StaffCore, BakedModel> staffCoreModels, Map<StaffCap, BakedModel> staffCapsModels) {
		this.staffCoreModels = staffCoreModels;
		this.staffCapsModels = staffCapsModels;
	}


	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		StaffCore core = stack.<Holder<StaffCore>>getOrDefault(DevotionData.STAFF_CORE.get(), DevotionStaffCores.OAK_CORE.holder()).value();
		StaffCap cap = stack.<Holder<StaffCap>>getOrDefault(DevotionData.STAFF_CAP.get(), DevotionStaffCaps.IRON_CAP.holder()).value();

		staffCoreModels.get(core).emitItemQuads(stack, randomSupplier, context);
		staffCapsModels.get(cap).emitItemQuads(stack, randomSupplier, context);
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
		List<BakedQuad> bakedQuads = new ArrayList<>();

		staffCoreModels.entrySet().stream().map(Map.Entry::getValue).map(bakedModel -> bakedModel.getQuads(state, direction, random)).forEach(bakedQuads::addAll);
		staffCapsModels.entrySet().stream().map(Map.Entry::getValue).map(bakedModel -> bakedModel.getQuads(state, direction, random)).forEach(bakedQuads::addAll);

		return bakedQuads;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean usesBlockLight() {
		return true;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return null;
	}

	@Override
	public ItemTransforms getTransforms() {
		return ItemTransforms.NO_TRANSFORMS;
	}

	@Override
	public ItemOverrides getOverrides() {
		return ItemOverrides.EMPTY;
	}
}
