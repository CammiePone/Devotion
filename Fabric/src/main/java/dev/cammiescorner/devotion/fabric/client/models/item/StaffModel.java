package dev.cammiescorner.devotion.fabric.client.models.item;

import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCaps;
import dev.cammiescorner.devotion.common.registries.DevotionStaffCores;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BuiltInModel;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.function.Supplier;

public class StaffModel extends BuiltInModel implements FabricBakedModel {
	private final Map<StaffCore, BakedModel> staffCoreModels;
	private final Map<StaffCap, BakedModel> staffCapsModels;

	public StaffModel(Map<StaffCore, BakedModel> staffCoreModels, Map<StaffCap, BakedModel> staffCapsModels, BakedModel defaultModel) {
		super(defaultModel.getTransforms(), defaultModel.getOverrides(), defaultModel.getParticleIcon(), defaultModel.usesBlockLight());
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
}
