package dev.cammiescorner.devotion.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.client.renderers.AuraVertexBufferSource;
import dev.cammiescorner.devotion.common.Color;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import dev.cammiescorner.devotion.common.registries.DevotionParticles;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class AuraNodeParticle extends TextureSheetParticle implements ParticleOptions {
	private final SpriteSet spriteSet;
	private final BlockPos blockPos;
	private final Map<AuraType, Float> auraTypeFloatMap;
	private final List<AuraType> filledAuraTypes;
	private Vec3 position;
	private int index;

	public AuraNodeParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
		super(level, x, y, z);
		this.spriteSet = spriteSet;
		this.blockPos = BlockPos.containing(x, y, z);
		this.auraTypeFloatMap = MainHelper.getAuraNodes(level.getChunk(blockPos)).get(blockPos).viewAuraMap();
		this.filledAuraTypes = auraTypeFloatMap.keySet().stream().filter(auraType -> auraTypeFloatMap.get(auraType) > 0).toList();
		this.lifetime = filledAuraTypes.size() * 20;
		this.xd = 0;
		this.yd = 0;
		this.zd = 0;
		this.quadSize = 0.4f;
		this.alpha = 1f;
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
		if(lifetime <= 0)
			return;

		if(level.getGameTime() % 20 == 0) {
			if(index < filledAuraTypes.size() - 1)
				index++;
			else
				index = 0;
		}

		// TODO smoothly transition between nextAuraType's color and currentAuraType's color
		AuraType currentAuraType = filledAuraTypes.get(index);
		AuraType nextAuraType = filledAuraTypes.get(index + 1 < filledAuraTypes.size() - 1 ? index + 1 : 0);
		Color color = currentAuraType.getColor();
		AuraVertexBufferSource auraBuffer = new AuraVertexBufferSource(
			Minecraft.getInstance().renderBuffers().bufferSource(),
			color.getRedI(), color.getGreenI(), color.getBlueI(),
			(int) alpha * 255
		);

		if(Minecraft.getInstance().player.isHolding(DevotionItems.AURAMETER.get()))
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
