package dev.cammiescorner.devotion.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.client.renderers.AuraVertexBufferSource;
import dev.cammiescorner.devotion.common.Color;
import dev.cammiescorner.devotion.common.registries.DevotionParticles;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

public class AuraNodeParticle extends TextureSheetParticle implements ParticleOptions {
	private final SpriteSet spriteSet;
	private Vec3 position;

	public AuraNodeParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
		super(level, x, y, z);
		this.spriteSet = spriteSet;
		this.lifetime = 1;
		this.xd = 0;
		this.yd = 0;
		this.zd = 0;
	}

	@Override
	public void tick() {
		setSpriteFromAge(spriteSet);
		super.tick();
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public ParticleType<?> getType() {
		return DevotionParticles.AURA_NODE.get();
	}

	@Override
	public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
		Color color = AuraType.NONE.getColor();
		float alpha = 1f;
		AuraVertexBufferSource auraBuffer = new AuraVertexBufferSource(
			Minecraft.getInstance().renderBuffers().bufferSource(),
			color.getRedI(), color.getGreenI(), color.getBlueI(),
			(int) alpha * 255
		);
		this.quadSize = 0.4f;
		this.alpha = 0.5f;

		super.render(auraBuffer.getBuffer(RenderType.entityTranslucent(sprite.atlasLocation())), renderInfo, partialTicks);
	}

	@Override
	public float getQuadSize(float scaleFactor) {
		return super.getQuadSize(scaleFactor);
	}

	@Override
	public void setPos(double x, double y, double z) {
		super.setPos(x, y, z);
		position = new Vec3(x, y, z);
	}

	public Vec3 getPos() {
		return position;
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		@Override
		public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ) {
			AuraNodeParticle particle = new AuraNodeParticle(clientLevel, posX, posY, posZ, spriteSet);
			particle.setSpriteFromAge(spriteSet);
			return particle;
		}
	}
}
