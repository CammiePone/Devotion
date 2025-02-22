package dev.cammiescorner.devotion.fabric.client.models.item;

import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.common.registries.DevotionData;
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

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class StaffModel implements FabricBakedModel, BakedModel, UnbakedModel {
	private final Map<StaffCore, BakedModel> staffCoreBaked = new HashMap<>();
	private final Map<StaffCap, BakedModel> staffCapsBaked = new HashMap<>();
	private final Map<StaffCore, UnbakedModel> staffCoreUnbaked;
	private final Map<StaffCap, UnbakedModel> staffCapsUnbaked;

	public StaffModel(Map<StaffCore, UnbakedModel> staffCoreUnbaked, Map<StaffCap, UnbakedModel> staffCapsUnbaked) {
		this.staffCoreUnbaked = staffCoreUnbaked;
		this.staffCapsUnbaked = staffCapsUnbaked;
	}
	
	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		staffCoreBaked.get(stack.get(DevotionData.STAFF_CORE.get()).value()).emitItemQuads(stack, randomSupplier, context);
		staffCapsBaked.get(stack.get(DevotionData.STAFF_CAP.get()).value()).emitItemQuads(stack, randomSupplier, context);
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public @Nullable BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state) {
		for(Map.Entry<StaffCore, UnbakedModel> entry : staffCoreUnbaked.entrySet())
			staffCoreBaked.put(entry.getKey(), entry.getValue().bake(baker, spriteGetter, state));
		for(Map.Entry<StaffCap, UnbakedModel> entry : staffCapsUnbaked.entrySet())
			staffCapsBaked.put(entry.getKey(), entry.getValue().bake(baker, spriteGetter, state));

		return this;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
		List<BakedQuad> bakedQuads = new ArrayList<>();

		for(Map.Entry<StaffCore, BakedModel> entry : staffCoreBaked.entrySet())
			bakedQuads.addAll(entry.getValue().getQuads(state, direction, random));
		for(Map.Entry<StaffCap, BakedModel> entry : staffCapsBaked.entrySet())
			bakedQuads.addAll(entry.getValue().getQuads(state, direction, random));

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

		for(Map.Entry<StaffCore, UnbakedModel> entry : staffCoreUnbaked.entrySet())
			dependencies.addAll(entry.getValue().getDependencies());
		for(Map.Entry<StaffCap, UnbakedModel> entry : staffCapsUnbaked.entrySet())
			dependencies.addAll(entry.getValue().getDependencies());

		return dependencies;
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {
		for(Map.Entry<StaffCore, UnbakedModel> entry : staffCoreUnbaked.entrySet())
			entry.getValue().resolveParents(resolver);
		for(Map.Entry<StaffCap, UnbakedModel> entry : staffCapsUnbaked.entrySet())
			entry.getValue().resolveParents(resolver);
	}
}
