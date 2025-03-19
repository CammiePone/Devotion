package dev.cammiescorner.devotion.client.renderers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import dev.upcraft.sparkweave.api.color.Color;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public class AuraNodeRenderer {
	private static final ResourceLocation TEXTURE = Devotion.id("textures/misc/aura_node.png");
	private final Map<AuraType, Float> auraTypeFloatMap;
	private final List<AuraType> filledAuraTypes;
	private final ClientLevel level;
	private final BlockPos pos;
	private final int cycleSpeed;
	private int index;

	public AuraNodeRenderer(ClientLevel level, BlockPos pos) {
		this.level = level;
		this.pos = pos;
		this.auraTypeFloatMap = MainHelper.getAuraNodes(level.getChunk(pos)).get(pos).viewAuraMap();
		this.filledAuraTypes = auraTypeFloatMap.keySet().stream().filter(auraType -> auraTypeFloatMap.get(auraType) > 0).toList();
		this.cycleSpeed = 40 + level.getRandom().nextIntBetweenInclusive(-10, 10);
	}

	public void render(Camera camera, float partialTicks, int packedLight) {
		if(level.getGameTime() % cycleSpeed == 0) {
			if(index < filledAuraTypes.size() - 1)
				index++;
			else
				index = 0;
		}

		// TODO smoothly transition between nextAuraType's color and currentAuraType's color
		AuraType currentAuraType = filledAuraTypes.get(index);
		AuraType nextAuraType = filledAuraTypes.get(index + 1 >= filledAuraTypes.size() ? 0 : index + 1);
		Color color = currentAuraType.getColor().lerp(nextAuraType.getColor(), (level.getGameTime() % (float) cycleSpeed) / cycleSpeed + partialTicks);
		AuraVertexBufferSource auraBuffer = new AuraVertexBufferSource(
			Minecraft.getInstance().renderBuffers().bufferSource(),
			color.red(), color.green(), color.blue(), 255
		);

		if(Minecraft.getInstance().player.isHolding(DevotionItems.AURAMETER.get())) {
			VertexConsumer consumer = auraBuffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
			Vec3 relativePos = pos.getCenter().subtract(camera.getPosition());
			Quaternionf quaternionf = new Quaternionf();
			SingleQuadParticle.FacingCameraMode.LOOKAT_XYZ.setRotation(quaternionf, camera, partialTicks);

			renderRotatedQuad(consumer, quaternionf, (float) relativePos.x(), (float) relativePos.y(), (float) relativePos.z(), packedLight);
		}
	}

	protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, int packedLight) {
		renderVertex(buffer, quaternion, x, y, z, 1f, -1f, 0.4f, 1, 1, packedLight);
		renderVertex(buffer, quaternion, x, y, z, 1f, 1f, 0.4f, 1, 0, packedLight);
		renderVertex(buffer, quaternion, x, y, z, -1f, 1f, 0.4f, 0, 0, packedLight);
		renderVertex(buffer, quaternion, x, y, z, -1f, -1f, 0.4f, 0, 1, packedLight);
	}

	public void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
		Vector3f vector3f = new Vector3f(xOffset, yOffset, 0f).rotate(quaternion).mul(quadSize).add(x, y, z);
		buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(1f, 1f, 1f, 1f).setLight(packedLight);
	}

	public BlockPos getBlockPos() {
		return pos;
	}
}
