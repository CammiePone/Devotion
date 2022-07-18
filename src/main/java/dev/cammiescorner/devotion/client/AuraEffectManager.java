package dev.cammiescorner.devotion.client;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import dev.cammiescorner.devotion.api.DevotionHelper;
import dev.cammiescorner.devotion.common.integration.DevotionConfig;
import dev.cammiescorner.devotion.common.registry.DevotionComponents;
import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ManagedFramebuffer;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static dev.cammiescorner.devotion.Devotion.id;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

/**
 * Manages the Aura rendering effect. Auras are rendered to a separate framebuffer with a custom renderlayer and then
 * composited into the main framebuffer with a post shader. This uses similar techniques to
 * <a href="https://github.com/Ladysnake/Requiem/blob/ad68fa534c6b6d4c64be86162eb35e67140138c6/src/main/java/ladysnake/requiem/client/ShadowPlayerFx.java">Requiem's Shadow Player rendering.</a>
 */
public final class AuraEffectManager implements EntitiesPreRenderCallback, ShaderEffectRenderCallback {
	public static final AuraEffectManager INSTANCE = new AuraEffectManager();
	private final MinecraftClient client = MinecraftClient.getInstance();
	public final ManagedCoreShader auraCoreShader = ShaderEffectManager.getInstance().manageCoreShader(id("rendertype_aura"));
	private final ManagedShaderEffect auraPostShader = ShaderEffectManager.getInstance().manage(id("shaders/post/aura.json"), this::assignDepthTexture);
	private final ManagedFramebuffer auraFramebuffer = auraPostShader.getTarget("auras");
	private boolean auraBufferCleared;

	/**
	 * Gets the aura level of an entity as a float in the range [0..1]
	 *
	 * @param entity the entity
	 * @return the aura level
	 */
	public static float getAuraFor(Entity entity) {
		return DevotionComponents.AURA_COMPONENT.isProvidedBy(entity) && DevotionComponents.AURA_FADE_COMPONENT.isProvidedBy(entity) &&
				DevotionHelper.getAuraFade(entity) > 0 ? (DevotionHelper.getAura(entity) / (float) DevotionHelper.getMaxAura(entity)) * DevotionHelper.getAuraFade(entity) : 0;
	}

	/**
	 * Gets the aura colour of an entity as a three-length int array in the range [0.255]
	 *
	 * @param entity the entity
	 * @return the aura colour
	 */
	public static int[] getAuraColourFor(Entity entity) {
		return DevotionComponents.CURRENT_SPELL_COMPONENT.isProvidedBy(entity) ? DevotionHelper.getSelectedSpell(entity).getSpellType().getRgbInt() : new int[] { 0, 0, 0 };
	}

	@Override
	public void beforeEntitiesRender(@NotNull Camera camera, @NotNull Frustum frustum, float tickDelta) {
		auraBufferCleared = false;
	}

	@Override
	public void renderShaderEffects(float tickDelta) {
		if(this.auraBufferCleared) {
			auraPostShader.setUniformValue("TransStepGranularity", DevotionConfig.auraGradiant);
			auraPostShader.setUniformValue("BlobsStepGranularity", DevotionConfig.auraSharpness);
			auraPostShader.render(tickDelta);
			client.getFramebuffer().beginWrite(true);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
			auraFramebuffer.draw();
			RenderSystem.disableBlend();
		}
	}

	/**
	 * Binds aura framebuffer for use and clears it if necessary.
	 */
	public void beginAuraFramebufferUse() {
		Framebuffer auraFramebuffer = this.auraFramebuffer.getFramebuffer();

		if(auraFramebuffer != null) {
			auraFramebuffer.beginWrite(false);

			if(!auraBufferCleared) {
				// clear framebuffer colour (but not depth)
				float[] clearColor = auraFramebuffer.clearColor;
				RenderSystem.clearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
				RenderSystem.clear(GL_COLOR_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);

				auraBufferCleared = true;
			}
		}
	}

	/**
	 * Unbinds aura framebuffer for use and undoes changes made in {@link #beginAuraFramebufferUse()}.
	 */
	private void endAuraFramebufferUse() {
		this.client.getFramebuffer().beginWrite(false);
	}

	/**
	 * Makes the aura framebuffer use the same depth texture as the main framebuffer. Run when the aura post shader
	 * is initialised.
	 *
	 * @param managedShaderEffect shader effect being initialised
	 */
	private void assignDepthTexture(ManagedShaderEffect managedShaderEffect) {
		client.getFramebuffer().beginWrite(false);
		int depthTexturePtr = client.getFramebuffer().getDepthAttachment();

		if(depthTexturePtr > -1) {
			auraFramebuffer.beginWrite(false);
			GlStateManager._glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexturePtr, 0);
		}
	}

	/**
	 * Gets the {@link RenderLayer} for rendering auras with a given texture
	 *
	 * @param texture the identifier of the texture to use
	 * @return the render layer
	 */
	public static RenderLayer getRenderLayer(Identifier texture) {
		return texture == null ? getRenderLayer() : AuraRenderLayers.AURA_LAYER.apply(texture);
	}

	/**
	 * Gets the {@link RenderLayer} for rendering auras with the same texture as a given render layer
	 *
	 * @param base the render layer to take the texture from
	 * @return the render layer
	 */
	public static RenderLayer getRenderLayer(@NotNull RenderLayer base) {
		return AuraRenderLayers.getRenderLayerWithTextureFrom(base);
	}

	/**
	 * Gets the {@link RenderLayer} for rendering auras with a default texture
	 *
	 * @return the render layer
	 */
	public static RenderLayer getRenderLayer() {
		return AuraRenderLayers.DEFAULT_AURA_LAYER;
	}

	/**
	 * Helper for the creating and holding the aura render layers and target
	 */
	private static final class AuraRenderLayers extends RenderLayer {
		// have to extend RenderLayer to access a few of these things

		private static final Target AURA_TARGET = new Target("devotion:aura_target", AuraEffectManager.INSTANCE::beginAuraFramebufferUse, AuraEffectManager.INSTANCE::endAuraFramebufferUse);
		private static final Function<Identifier, RenderLayer> AURA_LAYER = Util.memoize(id -> RenderLayer.of("aura", VertexFormats.POSITION_COLOR_TEXTURE, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().shader(new Shader(AuraEffectManager.INSTANCE.auraCoreShader::getProgram)).writeMaskState(COLOR_MASK).transparency(TRANSLUCENT_TRANSPARENCY).target(AURA_TARGET).texture(new Texture(id, false, false)).build(false)));
		private static final Identifier WHITE_TEXTURE = new Identifier("misc/white.png");
		private static final RenderLayer DEFAULT_AURA_LAYER = AURA_LAYER.apply(WHITE_TEXTURE);

		// no need to create instances of this
		private AuraRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
			super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
		}

		/**
		 * Extracts the texture from a render layer and creates an aura render layer with it.
		 *
		 * @param base the render layer to take the texture from
		 * @return the aura render layer
		 */
		private static RenderLayer getRenderLayerWithTextureFrom(RenderLayer base) {
			if(base instanceof RenderLayer.MultiPhase multiPhase)
				return AURA_LAYER.apply(multiPhase.getPhases().texture.getId().orElse(WHITE_TEXTURE));
			else
				return DEFAULT_AURA_LAYER;
		}
	}
}
