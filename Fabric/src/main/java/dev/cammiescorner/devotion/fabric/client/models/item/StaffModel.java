package dev.cammiescorner.devotion.fabric.client.models.item;

import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class StaffModel implements FabricBakedModel, BakedModel, UnbakedModel {
	private final Map<StaffCore, UnbakedModel> staffCoreModels;
	private final Map<StaffCap, UnbakedModel> staffCapsModels;
	private final List<BakedModel> bakedDelegates = new ArrayList<>();

	public StaffModel(Map<StaffCore, UnbakedModel> staffCoreModels, Map<StaffCap, UnbakedModel> staffCapsModels) {
		this.staffCoreModels = staffCoreModels;
		this.staffCapsModels = staffCapsModels;
	}


	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		for(BakedModel delegate : bakedDelegates)
			delegate.emitItemQuads(stack, randomSupplier, context);
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public @Nullable BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state) {
		for(Map.Entry<StaffCore, UnbakedModel> entry : staffCoreModels.entrySet())
			bakedDelegates.add(entry.getValue().bake(baker, spriteGetter, state));
		for(Map.Entry<StaffCap, UnbakedModel> entry : staffCapsModels.entrySet())
			bakedDelegates.add(entry.getValue().bake(baker, spriteGetter, state));

		return this;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
		List<BakedQuad> bakedQuads = new ArrayList<>();

		for(BakedModel model : bakedDelegates)
			bakedQuads.addAll(model.getQuads(state, direction, random));

		return bakedQuads;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return false;
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

	@Override
	public Collection<ResourceLocation> getDependencies() {
		List<ResourceLocation> dependencies = new ArrayList<>();

		for(Map.Entry<StaffCore, UnbakedModel> entry : staffCoreModels.entrySet())
			dependencies.addAll(entry.getValue().getDependencies());
		for(Map.Entry<StaffCap, UnbakedModel> entry : staffCapsModels.entrySet())
			dependencies.addAll(entry.getValue().getDependencies());

		return dependencies;
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {
		for(Map.Entry<StaffCore, UnbakedModel> entry : staffCoreModels.entrySet())
			entry.getValue().resolveParents(resolver);
		for(Map.Entry<StaffCap, UnbakedModel> entry : staffCapsModels.entrySet())
			entry.getValue().resolveParents(resolver);
	}
}
