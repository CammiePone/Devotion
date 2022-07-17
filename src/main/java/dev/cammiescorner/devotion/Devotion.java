package dev.cammiescorner.devotion;

import dev.cammiescorner.devotion.api.actions.AltarAction;
import dev.cammiescorner.devotion.api.cults.Cult;
import dev.cammiescorner.devotion.api.entity.DevotionAttributes;
import dev.cammiescorner.devotion.api.spells.Spell;
import dev.cammiescorner.devotion.common.CommonEvents;
import dev.cammiescorner.devotion.common.integration.DevotionConfig;
import dev.cammiescorner.devotion.common.packets.c2s.CastSpellPacket;
import dev.cammiescorner.devotion.common.packets.c2s.SetCastingPacket;
import dev.cammiescorner.devotion.common.registry.*;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.List;

public class Devotion implements ModInitializer {
	public static final String MOD_ID = "devotion";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final DefaultedRegistry<Spell> SPELL = FabricRegistryBuilder.createDefaulted(Spell.class, id("spell"), id("empty")).buildAndRegister();
	public static final DefaultedRegistry<AltarAction> ALTAR_ACTIONS = FabricRegistryBuilder.createDefaulted(AltarAction.class, id("altar_actions"), id("empty")).buildAndRegister();
	public static final DefaultedRegistry<Cult> CULT = FabricRegistryBuilder.createDefaulted(Cult.class, id("cult"), id("empty")).buildAndRegister();
	public static final ItemGroup ITEM_GROUP = QuiltItemGroup.createWithIcon(id("general"), () -> new ItemStack(DevotionBlocks.AMETHYST_ALTAR));
	public static final List<Item> HOOD_ITEMS = List.of(
			DevotionItems.MAGE_HOOD, DevotionItems.ENHANCER_MAGE_HOOD, DevotionItems.TRANSMUTER_MAGE_HOOD, DevotionItems.EMITTER_MAGE_HOOD,
			DevotionItems.CONJURER_MAGE_HOOD, DevotionItems.MANIPULATOR_MAGE_HOOD, DevotionItems.TIME_CULTIST_HOOD
	);

	@Override
	public void onInitialize(ModContainer mod) {
		MidnightConfig.init(Devotion.MOD_ID, DevotionConfig.class);

		DevotionItems.register();
		DevotionBlocks.register();
		DevotionBlockEntities.register();
		DevotionSpells.register();
		DevotionAltarActions.register();
		DevotionSounds.register();
		DevotionCommands.register();
		DevotionRecipes.loadMeBitch();
		DevotionCults.register();

		Registry.register(Registry.ATTRIBUTE, id("casting_multiplier"), DevotionAttributes.AURA_COST);
		Registry.register(Registry.ATTRIBUTE, id("aura_regen"), DevotionAttributes.AURA_REGEN);
		Registry.register(Registry.ATTRIBUTE, id("aura_lock"), DevotionAttributes.AURA_LOCK);
		Registry.register(Registry.ATTRIBUTE, id("enhancement_affinity"), DevotionAttributes.ENHANCEMENT_AFFINITY);
		Registry.register(Registry.ATTRIBUTE, id("transmutation_affinity"), DevotionAttributes.TRANSMUTATION_AFFINITY);
		Registry.register(Registry.ATTRIBUTE, id("emission_affinity"), DevotionAttributes.EMISSION_AFFINITY);
		Registry.register(Registry.ATTRIBUTE, id("conjuration_affinity"), DevotionAttributes.CONJURATION_AFFINITY);
		Registry.register(Registry.ATTRIBUTE, id("manipulation_affinity"), DevotionAttributes.MANIPULATION_AFFINITY);

		ServerPlayNetworking.registerGlobalReceiver(CastSpellPacket.ID, CastSpellPacket::handler);
		ServerPlayNetworking.registerGlobalReceiver(SetCastingPacket.ID, SetCastingPacket::handler);

		CommonEvents.events();
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
}
