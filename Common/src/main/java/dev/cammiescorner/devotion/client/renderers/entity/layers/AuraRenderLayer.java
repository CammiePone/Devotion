package dev.cammiescorner.devotion.client.renderers.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.client.renderers.AuraVertexBufferSource;
import dev.cammiescorner.devotion.common.Color;
import dev.cammiescorner.devotion.common.MainHelper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class AuraRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	public final List<RenderLayer<T, M>> otherFeatureRenderers;

	public AuraRenderLayer(RenderLayerParent<T, M> renderer, List<RenderLayer<T, M>> otherFeatureRenderers) {
		super(renderer);
		this.otherFeatureRenderers = List.copyOf(otherFeatureRenderers);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		AuraType primaryAuraType = MainHelper.getPrimaryAuraType(entity);
		Color auraColor = primaryAuraType.getColor();
		float aura = MainHelper.getAura(entity, primaryAuraType);
		float scale = 1f;

		if(aura > 0f || true) {
			EntityDimensions dimensions = entity.getDimensions(entity.getPose());
			AuraVertexBufferSource auraBufferSource = new AuraVertexBufferSource(bufferSource, auraColor.getRedI(), auraColor.getGreenI(), auraColor.getBlueI(), (int) (MainHelper.getAuraAlpha(entity, primaryAuraType) * 255));

			poseStack.pushPose();
			poseStack.scale(scale, scale, scale);

			for(RenderLayer<T, M> renderer : otherFeatureRenderers)
				renderer.render(poseStack, auraBufferSource, light, entity, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);

			poseStack.translate(0, -((dimensions.height() * scale) * 0.5 - dimensions.height() * 0.5), 0);
			getParentModel().renderToBuffer(poseStack, auraBufferSource.getBuffer(getTextureLocation(entity)), light, OverlayTexture.NO_OVERLAY, 0xffffff);

			poseStack.popPose();
		}
	}
}
