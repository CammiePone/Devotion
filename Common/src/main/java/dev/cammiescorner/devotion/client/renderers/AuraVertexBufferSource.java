package dev.cammiescorner.devotion.client.renderers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.devotion.client.AuraFx;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class AuraVertexBufferSource implements MultiBufferSource {
	private final MultiBufferSource bufferSource;
	private final int red;
	private final int green;
	private final int blue;
	private final int alpha;

	public AuraVertexBufferSource(MultiBufferSource bufferSource, int red, int green, int blue, int alpha) {
		this.bufferSource = bufferSource;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	@Override
	public VertexConsumer getBuffer(RenderType renderType) {
		if(renderType.outline().isPresent())
			return new AuraVertexConsumer(bufferSource.getBuffer(AuraFx.getRenderType(renderType)), red, green, blue, alpha);
		else
			return new DummyVertexConsumer();
	}

	public AuraVertexConsumer getBuffer(ResourceLocation texture) {
		return new AuraVertexConsumer(bufferSource.getBuffer(AuraFx.getRenderType(texture)), red, green, blue, alpha);
	}

	public AuraVertexConsumer getBuffer() {
		return new AuraVertexConsumer(bufferSource.getBuffer(AuraFx.getRenderType()), red, green, blue, alpha);
	}

	public static class AuraVertexConsumer implements VertexConsumer {
		private final VertexConsumer delegate;
		private final int red;
		private final int green;
		private final int blue;
		private final int alpha;

		public AuraVertexConsumer(VertexConsumer delegate, int red, int green, int blue, int alpha) {
			this.delegate = delegate;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.alpha = alpha;
		}

		@Override
		public VertexConsumer addVertex(float x, float y, float z) {
			delegate.addVertex(x, y, z);
			return this;
		}

		@Override
		public VertexConsumer setColor(int red, int green, int blue, int alpha) {
			delegate.setColor(this.red, this.green, this.blue, this.alpha);
			return this;
		}

		@Override
		public VertexConsumer setUv(float u, float v) {
			delegate.setUv(u, v);
			return this;
		}

		@Override
		public VertexConsumer setUv1(int u, int v) {
			return this;
		}

		@Override
		public VertexConsumer setUv2(int u, int v) {
			return this;
		}

		@Override
		public VertexConsumer setLight(int packedLight) {
			delegate.setUv2(packedLight & 65535, packedLight >> 16 & 65535);
			return this;
		}

		@Override
		public VertexConsumer setNormal(float normalX, float normalY, float normalZ) {
			return this;
		}

		@Override
		public void addVertex(float x, float y, float z, int color, float u, float v, int packedOverlay, int packedLight, float normalX, float normalY, float normalZ) {
			delegate.addVertex(x, y, z).setColor(this.red, this.green, this.blue, this.alpha).setUv(u, v).setUv2(0, 0);
		}
	}

	public static class DummyVertexConsumer implements VertexConsumer {
		@Override
		public VertexConsumer addVertex(float x, float y, float z) { return this; }

		@Override
		public VertexConsumer setColor(int red, int green, int blue, int alpha) { return this; }

		@Override
		public VertexConsumer setUv(float u, float v) { return this; }

		@Override
		public VertexConsumer setUv1(int u, int v) { return this; }

		@Override
		public VertexConsumer setUv2(int u, int v) { return this; }

		@Override
		public VertexConsumer setNormal(float normalX, float normalY, float normalZ) { return this; }
	}
}
