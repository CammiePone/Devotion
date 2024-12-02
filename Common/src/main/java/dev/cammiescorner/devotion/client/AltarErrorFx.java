package dev.cammiescorner.devotion.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.velvet.api.managed.ManagedCoreShader;
import dev.cammiescorner.velvet.api.managed.ShaderEffectManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class AltarErrorFx {
	private static final ManagedCoreShader SHADER = ShaderEffectManager.getInstance().manageCoreShader(Devotion.id("rendertype_altar_error"), DefaultVertexFormat.NEW_ENTITY);

	// TODO call this in DevotionClient after Laz's PR
	public static void init() {}

	public static RenderType withTexture(ResourceLocation texture) {
		return SHADER.getRenderLayer(RenderType.entityTranslucentCull(texture));
	}
}
