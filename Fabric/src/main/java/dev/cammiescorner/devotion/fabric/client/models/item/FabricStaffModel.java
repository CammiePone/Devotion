package dev.cammiescorner.devotion.fabric.client.models.item;

import dev.cammiescorner.devotion.api.spells.SpellFocus;
import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionSpellFoci;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
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

public class FabricStaffModel implements FabricBakedModel, BakedModel, UnbakedModel {
	private final Map<StaffCore, BakedModel> bakedCoreModels = new HashMap<>();
	private final Map<StaffCap, BakedModel> bakedCapModels = new HashMap<>();
	private final Map<SpellFocus, BakedModel> bakedFocusModels = new HashMap<>();
	private final Map<StaffCore, UnbakedModel> unbakedCoreModels;
	private final Map<StaffCap, UnbakedModel> unbakedCapModels;
	private final Map<SpellFocus, UnbakedModel> unbakedFocusModels;

	public FabricStaffModel(Map<StaffCore, UnbakedModel> unbakedCoreModels, Map<StaffCap, UnbakedModel> unbakedCapModels, Map<SpellFocus, UnbakedModel> unbakedFocusModels) {
		this.unbakedCoreModels = unbakedCoreModels;
		this.unbakedCapModels = unbakedCapModels;
		this.unbakedFocusModels = unbakedFocusModels;
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
		bakedCoreModels.get(stack.get(DevotionData.STAFF_CORE.get()).value()).emitItemQuads(stack, randomSupplier, context);
		bakedCapModels.get(stack.get(DevotionData.STAFF_CAP.get()).value()).emitItemQuads(stack, randomSupplier, context);
		bakedFocusModels.get(stack.get(DevotionData.SPELL_FOCUS.get()).value()).emitItemQuads(stack, randomSupplier, context);
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public @Nullable BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state) {
		for(ResourceLocation location : DevotionStaffCores.REGISTRY.keySet()) {
			StaffCore staffCore = DevotionStaffCores.REGISTRY.get(location);

			bakedCoreModels.put(staffCore, baker.bake(staffCore.getStaffModelLocation(), state));
		}

		for(ResourceLocation location : DevotionStaffCaps.REGISTRY.keySet()) {
			StaffCap staffCap = DevotionStaffCaps.REGISTRY.get(location);

			bakedCapModels.put(staffCap, baker.bake(staffCap.getStaffModelLocation(), state));
		}

		for(ResourceLocation location : DevotionSpellFoci.REGISTRY.keySet()) {
			SpellFocus spellFocus = DevotionSpellFoci.REGISTRY.get(location);

			bakedFocusModels.put(spellFocus, baker.bake(spellFocus.getStaffModelLocation(), state));
		}

		return this;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
		List<BakedQuad> bakedQuads = new ArrayList<>();

		for(Map.Entry<StaffCore, BakedModel> entry : bakedCoreModels.entrySet())
			bakedQuads.addAll(entry.getValue().getQuads(state, direction, random));
		for(Map.Entry<StaffCap, BakedModel> entry : bakedCapModels.entrySet())
			bakedQuads.addAll(entry.getValue().getQuads(state, direction, random));
		for(Map.Entry<SpellFocus, BakedModel> entry : bakedFocusModels.entrySet())
			bakedQuads.addAll(entry.getValue().getQuads(state, direction, random));

		return bakedQuads;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean usesBlockLight() {
		return false;
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
		return bakedCoreModels.get(DevotionStaffCores.OAK_CORE.get()).getTransforms();
	}

	@Override
	public ItemOverrides getOverrides() {
		return bakedCoreModels.get(DevotionStaffCores.OAK_CORE.get()).getOverrides();
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		List<ResourceLocation> dependencies = new ArrayList<>();

		for(Map.Entry<StaffCore, UnbakedModel> entry : unbakedCoreModels.entrySet())
			dependencies.addAll(entry.getValue().getDependencies());
		for(Map.Entry<StaffCap, UnbakedModel> entry : unbakedCapModels.entrySet())
			dependencies.addAll(entry.getValue().getDependencies());
		for(Map.Entry<SpellFocus, UnbakedModel> entry : unbakedFocusModels.entrySet())
			dependencies.addAll(entry.getValue().getDependencies());

		return dependencies;
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {
		for(Map.Entry<StaffCore, UnbakedModel> entry : unbakedCoreModels.entrySet())
			entry.getValue().resolveParents(resolver);
		for(Map.Entry<StaffCap, UnbakedModel> entry : unbakedCapModels.entrySet())
			entry.getValue().resolveParents(resolver);
		for(Map.Entry<SpellFocus, UnbakedModel> entry : unbakedFocusModels.entrySet())
			entry.getValue().resolveParents(resolver);
	}
}
