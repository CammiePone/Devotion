package dev.cammiescorner.devotion.client.models;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.BlockRenderView;

import java.util.function.Supplier;

public class TranslucentBakedModel extends ForwardingBakedModel {
	private static final ThreadLocal<TranslucentBakedModel> THREAD_LOCAL = ThreadLocal.withInitial(TranslucentBakedModel::new);
	protected Supplier<Float> alphaSupplier;

	public static BakedModel wrap(BakedModel model, Supplier<Float> alphaSupplier) {
		TranslucentBakedModel wrapper = THREAD_LOCAL.get();
		wrapper.wrapped = model;
		wrapper.alphaSupplier = alphaSupplier;
		return wrapper;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<RandomGenerator> randomSupplier, RenderContext context) {
		int alpha = (int) (alphaSupplier.get() * 255) << 24;

		context.pushTransform(quad -> {
			for(int vertex = 0; vertex < 4; vertex++) {
				int color = quad.spriteColor(vertex, 0);
				quad.spriteColor(vertex, 0, color & 0xFFFFFF | alpha);
			}
			return true;
		});

		super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
		context.popTransform();
	}
}
