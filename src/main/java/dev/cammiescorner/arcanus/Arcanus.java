package dev.cammiescorner.arcanus;

import dev.cammiescorner.arcanus.api.actions.AltarAction;
import dev.cammiescorner.arcanus.api.entity.ArcanusAttributes;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.CommonEvents;
import dev.cammiescorner.arcanus.common.integration.ArcanusConfig;
import dev.cammiescorner.arcanus.common.packets.c2s.CastSpellPacket;
import dev.cammiescorner.arcanus.common.packets.c2s.SetCastingPacket;
import dev.cammiescorner.arcanus.common.registry.*;
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

public class Arcanus implements ModInitializer {
	public static final String MOD_ID = "arcanus";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final DefaultedRegistry<Spell> SPELL = FabricRegistryBuilder.createDefaulted(Spell.class, id("spell"), id("empty")).buildAndRegister();
	public static final DefaultedRegistry<AltarAction> ALTAR_ACTIONS = FabricRegistryBuilder.createDefaulted(AltarAction.class, id("altar_actions"), id("empty")).buildAndRegister();
	public static final ItemGroup ITEM_GROUP = QuiltItemGroup.createWithIcon(id("general"), () -> new ItemStack(ArcanusBlocks.AMETHYST_ALTAR));
	public static final List<Item> HOOD_ITEMS = List.of(
			ArcanusItems.MAGE_HOOD, ArcanusItems.ENHANCER_MAGE_HOOD, ArcanusItems.TRANSMUTER_MAGE_HOOD, ArcanusItems.EMITTER_MAGE_HOOD,
			ArcanusItems.CONJURER_MAGE_HOOD, ArcanusItems.MANIPULATOR_MAGE_HOOD, ArcanusItems.TIME_CULTIST_HOOD
	);

	@Override
	public void onInitialize(ModContainer mod) {
		MidnightConfig.init(Arcanus.MOD_ID, ArcanusConfig.class);

		ArcanusItems.register();
		ArcanusBlocks.register();
		ArcanusBlockEntities.register();
		ArcanusSpells.register();
		ArcanusAltarActions.register();
		ArcanusSounds.register();
		ArcanusCommands.register();
		ArcanusRecipes.loadMeBitch();

		Registry.register(Registry.ATTRIBUTE, id("casting_multiplier"), ArcanusAttributes.AURA_COST);
		Registry.register(Registry.ATTRIBUTE, id("aura_regen"), ArcanusAttributes.AURA_REGEN);
		Registry.register(Registry.ATTRIBUTE, id("aura_lock"), ArcanusAttributes.AURA_LOCK);
		Registry.register(Registry.ATTRIBUTE, id("enhancement_affinity"), ArcanusAttributes.ENHANCEMENT_AFFINITY);
		Registry.register(Registry.ATTRIBUTE, id("transmutation_affinity"), ArcanusAttributes.TRANSMUTATION_AFFINITY);
		Registry.register(Registry.ATTRIBUTE, id("emission_affinity"), ArcanusAttributes.EMISSION_AFFINITY);
		Registry.register(Registry.ATTRIBUTE, id("conjuration_affinity"), ArcanusAttributes.CONJURATION_AFFINITY);
		Registry.register(Registry.ATTRIBUTE, id("manipulation_affinity"), ArcanusAttributes.MANIPULATION_AFFINITY);

		ServerPlayNetworking.registerGlobalReceiver(CastSpellPacket.ID, CastSpellPacket::handler);
		ServerPlayNetworking.registerGlobalReceiver(SetCastingPacket.ID, SetCastingPacket::handler);

		CommonEvents.events();
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
}
