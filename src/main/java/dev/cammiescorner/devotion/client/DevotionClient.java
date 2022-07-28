package dev.cammiescorner.devotion.client;

import com.williambl.early_features.api.LivingEntityEarlyFeatureRendererRegistrationCallback;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.models.entity.ScrollModel;
import dev.cammiescorner.devotion.client.models.equipment.MageRobesModel;
import dev.cammiescorner.devotion.client.models.equipment.TimeCultLeaderRobesModel;
import dev.cammiescorner.devotion.client.models.equipment.TimeCultRobesModel;
import dev.cammiescorner.devotion.client.renderer.blocks.AmethystAltarBlockEntityRenderer;
import dev.cammiescorner.devotion.client.renderer.entities.AuraFeatureRenderer;
import dev.cammiescorner.devotion.client.renderer.equipment.MageRobesRenderer;
import dev.cammiescorner.devotion.client.renderer.equipment.TimeCultRobesRenderer;
import dev.cammiescorner.devotion.common.packets.s2c.RefreshResearchScreenPacket;
import dev.cammiescorner.devotion.common.registry.DevotionBlockEntities;
import dev.cammiescorner.devotion.common.registry.DevotionItems;
import dev.cammiescorner.devotion.common.registry.DevotionKeyBinds;
import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class DevotionClient implements ClientModInitializer {
	public static final Identifier MAGE_ROBES = Devotion.id("textures/entity/armor/mage_robes.png");
	public static final Identifier ENHANCER_MAGE_ROBES = Devotion.id("textures/entity/armor/enhancer_mage_robes.png");
	public static final Identifier TRANSMUTER_MAGE_ROBES = Devotion.id("textures/entity/armor/transmuter_mage_robes.png");
	public static final Identifier EMITTER_MAGE_ROBES = Devotion.id("textures/entity/armor/emitter_mage_robes.png");
	public static final Identifier CONJURER_MAGE_ROBES = Devotion.id("textures/entity/armor/conjurer_mage_robes.png");
	public static final Identifier MANIPULATOR_MAGE_ROBES = Devotion.id("textures/entity/armor/manipulator_mage_robes.png");

	public static long clientTick = 0;
	public static double guideBookOffsetX = 0, guideBookOffsetY = 0;

	@Override
	public void onInitializeClient(ModContainer mod) {
		BlockEntityRendererRegistry.register(DevotionBlockEntities.AMETHYST_ALTAR, AmethystAltarBlockEntityRenderer::new);
		DevotionKeyBinds.register();
		ClientEvents.events();

		EntitiesPreRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
		ShaderEffectRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
		EntityModelLayerRegistry.registerModelLayer(ScrollModel.MODEL_LAYER, ScrollModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MageRobesModel.MODEL_LAYER, MageRobesModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(TimeCultRobesModel.MODEL_LAYER, TimeCultRobesModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(TimeCultLeaderRobesModel.MODEL_LAYER, TimeCultLeaderRobesModel::getTexturedModelData);
		ArmorRenderer.register(new MageRobesRenderer(MAGE_ROBES), DevotionItems.MAGE_HOOD, DevotionItems.MAGE_ROBE, DevotionItems.MAGE_BELT, DevotionItems.MAGE_BOOTS);
		ArmorRenderer.register(new MageRobesRenderer(ENHANCER_MAGE_ROBES), DevotionItems.ENHANCER_MAGE_HOOD, DevotionItems.ENHANCER_MAGE_ROBE, DevotionItems.ENHANCER_MAGE_BELT, DevotionItems.ENHANCER_MAGE_BOOTS);
		ArmorRenderer.register(new MageRobesRenderer(TRANSMUTER_MAGE_ROBES), DevotionItems.TRANSMUTER_MAGE_HOOD, DevotionItems.TRANSMUTER_MAGE_ROBE, DevotionItems.TRANSMUTER_MAGE_BELT, DevotionItems.TRANSMUTER_MAGE_BOOTS);
		ArmorRenderer.register(new MageRobesRenderer(EMITTER_MAGE_ROBES), DevotionItems.EMITTER_MAGE_HOOD, DevotionItems.EMITTER_MAGE_ROBE, DevotionItems.EMITTER_MAGE_BELT, DevotionItems.EMITTER_MAGE_BOOTS);
		ArmorRenderer.register(new MageRobesRenderer(CONJURER_MAGE_ROBES), DevotionItems.CONJURER_MAGE_HOOD, DevotionItems.CONJURER_MAGE_ROBE, DevotionItems.CONJURER_MAGE_BELT, DevotionItems.CONJURER_MAGE_BOOTS);
		ArmorRenderer.register(new MageRobesRenderer(MANIPULATOR_MAGE_ROBES), DevotionItems.MANIPULATOR_MAGE_HOOD, DevotionItems.MANIPULATOR_MAGE_ROBE, DevotionItems.MANIPULATOR_MAGE_BELT, DevotionItems.MANIPULATOR_MAGE_BOOTS);
		ArmorRenderer.register(new TimeCultRobesRenderer(false), DevotionItems.TIME_CULTIST_HOOD, DevotionItems.TIME_CULTIST_ROBE, DevotionItems.TIME_CULTIST_LEGGINGS, DevotionItems.TIME_CULTIST_BOOTS);
		ArmorRenderer.register(new TimeCultRobesRenderer(true), DevotionItems.TIME_CULTIST_LEADER_HOOD, DevotionItems.TIME_CULTIST_LEADER_ROBE, DevotionItems.TIME_CULTIST_LEADER_LEGGINGS, DevotionItems.TIME_CULTIST_LEADER_BOOTS);

		ClientPlayNetworking.registerGlobalReceiver(RefreshResearchScreenPacket.ID, RefreshResearchScreenPacket::handle);

		LivingEntityEarlyFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, context) -> {
			if(entityRenderer instanceof PlayerEntityRenderer playerRenderer)
				entityRenderer.addEarlyFeature(new AuraFeatureRenderer<>(playerRenderer, new HeldItemFeatureRenderer<>(playerRenderer, context.getHeldItemRenderer())));
		});

		for(Item hoodItem : Devotion.HOOD_ITEMS) {
			ModelPredicateProviderRegistry.register(hoodItem, Devotion.id("closed_hood"), (stack, world, entity, i) -> {
				NbtCompound tag = stack.getSubNbt(Devotion.MOD_ID);

				if(tag == null)
					return 0F;

				return tag.getBoolean("Closed") ? 1F : 0F;
			});
		}

		ModelPredicateProviderRegistry.register(DevotionItems.RESEARCH_SCROLL, Devotion.id("completed_research"), (stack, world, entity, i) -> {
			NbtCompound tag = stack.getSubNbt(Devotion.MOD_ID);

			if(tag == null)
				return 0F;

			return tag.getBoolean("Completed") ? 1F : 0F;
		});
	}
}
