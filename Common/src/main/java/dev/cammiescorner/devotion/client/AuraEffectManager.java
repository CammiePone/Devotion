package dev.cammiescorner.devotion.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.DevotionConfig;
import dev.cammiescorner.velvet.api.event.EntitiesPreRenderCallback;
import dev.cammiescorner.velvet.api.event.PostWorldRenderCallbackV3;
import dev.cammiescorner.velvet.api.event.ShaderEffectRenderCallback;
import dev.cammiescorner.velvet.api.experimental.ReadableDepthRenderTarget;
import dev.cammiescorner.velvet.api.managed.ManagedCoreShader;
import dev.cammiescorner.velvet.api.managed.ManagedRenderTarget;
import dev.cammiescorner.velvet.api.managed.ManagedShaderEffect;
import dev.cammiescorner.velvet.api.managed.ShaderEffectManager;
import dev.cammiescorner.velvet.api.managed.uniform.Uniform3f;
import dev.cammiescorner.velvet.api.managed.uniform.UniformMat4;
import dev.cammiescorner.velvet.api.util.GlMatrices;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.function.Function;

import static dev.cammiescorner.devotion.client.DevotionClient.client;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

public class AuraEffectManager implements EntitiesPreRenderCallback, ShaderEffectRenderCallback, PostWorldRenderCallbackV3 {
	public static final AuraEffectManager INSTANCE = new AuraEffectManager();
	public final ManagedCoreShader auraCoreShader = ShaderEffectManager.getInstance().manageCoreShader(Devotion.id("rendertype_aura"));
	private final ManagedShaderEffect auraPostShader = ShaderEffectManager.getInstance().manage(Devotion.id("shaders/post/aura.json"), this::assignDepthTexture);
	private final ManagedRenderTarget auraRenderTarget = auraPostShader.getTarget("auras");
	private final UniformMat4 uniformProjectionMatrix = auraPostShader.findUniformMat4("ProjectionMatrix");
	private final UniformMat4 uniformModelViewMatrix = auraPostShader.findUniformMat4("ModelViewMatrix");
	private final Uniform3f uniformCameraPosition = auraPostShader.findUniform3f("CameraPosition");
	private final Uniform3f uniformCenter = auraPostShader.findUniform3f("Center");
	private final Matrix4f projectionMatrix = new Matrix4f();
	private boolean auraBufferCleared;
	private float time = 0f;
	private float lastTickDelta = 0f;

	@Override
	public void beforeEntitiesRender(Camera camera, Frustum frustum, float tickDelta) {
		auraBufferCleared = false;
	}

	@Override
	public void renderShaderEffects(float tickDelta) {
		if(auraBufferCleared) {
			auraPostShader.setUniformValue("DevotionTransStepGranularity", DevotionConfig.Client.auraGradiant);
			auraPostShader.setUniformValue("DevotionBlobsStepGranularity", DevotionConfig.Client.auraSharpness);
			auraPostShader.setUniformValue("DevotionTime", getTime(tickDelta));
			auraPostShader.setSamplerUniform("DepthSampler", ((ReadableDepthRenderTarget) client.getMainRenderTarget()).getStillDepthMap());
			auraPostShader.setUniformValue("ViewPort", 0, 0, client.getWindow().getWidth(), client.getWindow().getHeight());
			auraPostShader.render(tickDelta);
			client.getMainRenderTarget().bindWrite(true);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			auraRenderTarget.draw(client.getWindow().getWidth(), client.getWindow().getHeight(), false);
			RenderSystem.disableBlend();
		}
	}

	@Override
	public void onWorldRendered(PoseStack posingStack, Matrix4f projectionMat, Matrix4f modelViewMat, Camera camera, float tickDelta) {
		Vec3 cameraPos = camera.getPosition();
		Entity entity = camera.getEntity();

		this.projectionMatrix.set(modelViewMat);
		uniformProjectionMatrix.set(modelViewMat);
		uniformModelViewMatrix.set(projectionMat);
		uniformCameraPosition.set((float) cameraPos.x, (float) cameraPos.y, (float) cameraPos.z);
		uniformCenter.set((float) Mth.lerp(entity.getX(), entity.xo, tickDelta), (float) Mth.lerp(entity.getY(), entity.yo, tickDelta), (float) Mth.lerp(entity.getZ(), entity.zo, tickDelta));
	}

	/**
	 * Binds aura render target for use and clears it if necessary.
	 */
	public void beginAuraRenderTargetUse() {
		RenderTarget auraRenderTarget = this.auraRenderTarget.getRenderTarget();

		if(auraRenderTarget != null) {
			auraRenderTarget.bindWrite(false);

			if(!auraBufferCleared) {
				// clear render target colour (but not depth)
				float[] clearColor = auraRenderTarget.clearChannels;
				RenderSystem.clearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
				RenderSystem.clear(GL_COLOR_BUFFER_BIT, Minecraft.ON_OSX);

				auraBufferCleared = true;
			}
		}
	}

	/**
	 * Unbinds aura render target for use and undoes changes made in {@link #beginAuraRenderTargetUse()}.
	 */
	private void endAuraRenderTargetUse() {
		client.getMainRenderTarget().bindWrite(false);
	}

	/**
	 * Makes the aura render target use the same depth texture as the main render target. Run when the aura post shader
	 * is initialised.
	 *
	 * @param managedShaderEffect shader effect being initialised
	 */
	private void assignDepthTexture(ManagedShaderEffect managedShaderEffect) {
		client.getMainRenderTarget().bindWrite(false);
		int depthTexturePtr = client.getMainRenderTarget().getDepthTextureId();

		if(depthTexturePtr > -1) {
			auraRenderTarget.beginWrite(false);
			GlStateManager._glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexturePtr, 0);
		}
	}

	/**
	 * Gets the {@link RenderType} for rendering auras with a given texture
	 *
	 * @param texture the identifier of the texture to use
	 * @return the render layer
	 */
	public static RenderType getRenderType(ResourceLocation texture) {
		return texture == null ? getRenderType() : AuraRenderType.AURA_TYPE.apply(texture);
	}

	/**
	 * Gets the {@link RenderType} for rendering auras with the same texture as a given render layer
	 *
	 * @param base the render layer to take the texture from
	 * @return the render layer
	 */
	public static RenderType getRenderType(@NotNull RenderType base) {
		return AuraRenderType.getRenderTypeWithTextureFrom(base);
	}

	/**
	 * Gets the {@link RenderType} for rendering auras with a default texture
	 *
	 * @return the render layer
	 */
	public static RenderType getRenderType() {
		return AuraRenderType.DEFAULT_AURA_TYPE;
	}

	private float getTime(float tickDelta) {
		if(tickDelta < lastTickDelta)
			time += (1f - lastTickDelta) + tickDelta;
		else
			time += tickDelta - lastTickDelta;

		lastTickDelta = tickDelta;

		while(time > 20f)
			time -= 20f;

		return time / 20f;
	}

	/**
	 * Helper for the creating and holding the aura render layers and target
	 */
	private static final class AuraRenderType extends RenderType {
		// have to extend RenderLayer to access a few of these things
		private static final OutputStateShard AURA_TARGET = new OutputStateShard("devotion:aura_target", AuraEffectManager.INSTANCE::beginAuraRenderTargetUse, AuraEffectManager.INSTANCE::endAuraRenderTargetUse);
		public static final VertexFormat POSITION_COLOR_TEX = VertexFormat.builder()
			.add("Position", VertexFormatElement.POSITION)
			.add("Color", VertexFormatElement.COLOR)
			.add("UV0", VertexFormatElement.UV0)
			.build();
		private static final Function<ResourceLocation, RenderType> AURA_TYPE = Util.memoize(id -> RenderType.create("aura", POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder().setShaderState(new ShaderStateShard(AuraEffectManager.INSTANCE.auraCoreShader::getProgram)).setWriteMaskState(COLOR_WRITE).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setOutputState(AURA_TARGET).setTextureState(new TextureStateShard(id, false, false)).createCompositeState(false)));
		private static final RenderType DEFAULT_AURA_TYPE = AURA_TYPE.apply(ClientHelper.WHITE_TEXTURE);

		// no need to create instances of this
		private AuraRenderType(String name, VertexFormat vertexFormat, VertexFormat.Mode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
			super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
		}

		/**
		 * Extracts the texture from a render layer and creates an aura render layer with it.
		 *
		 * @param base the render layer to take the texture from
		 * @return the aura render layer
		 */
		private static RenderType getRenderTypeWithTextureFrom(RenderType base) {
			if(base instanceof RenderType.CompositeRenderType compositeRenderType)
				return AURA_TYPE.apply(compositeRenderType.state().textureState.cutoutTexture().orElse(ClientHelper.WHITE_TEXTURE));
			else
				return DEFAULT_AURA_TYPE;
		}
	}
}
