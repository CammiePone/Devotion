package dev.cammiescorner.devotion;

import com.teamresourceful.resourcefulconfig.api.loader.Configurator;
import commonnetwork.api.Network;
import dev.cammiescorner.devotion.api.Graph;
import dev.cammiescorner.devotion.api.research.BookEntry;
import dev.cammiescorner.devotion.api.research.BookTab;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraAffinity;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.networking.clientbound.ClientboundAuraPacket;
import dev.cammiescorner.devotion.common.networking.clientbound.ClientboundKnownResearchPacket;
import dev.cammiescorner.devotion.common.networking.clientbound.ClientboundRefreshResearchScreenPacket;
import dev.cammiescorner.devotion.common.networking.serverbound.ServerboundOpenCloseHoodPacket;
import dev.cammiescorner.devotion.common.registries.*;
import dev.cammiescorner.devotion.common.screens.providers.ResearchMenuProvider;
import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.event.CommandEvents;
import dev.upcraft.sparkweave.api.event.EntityTickEvents;
import dev.upcraft.sparkweave.api.event.ItemMenuInteractionEvent;
import dev.upcraft.sparkweave.api.event.RegisterCustomLecternMenuEvent;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;

public class Devotion implements MainEntryPoint {
	public static final String MOD_ID = "devotion";
	public static final Logger LOGGER = LoggerFactory.getLogger("Devotion");
	public static final Configurator CONFIGURATOR = new Configurator(MOD_ID);

	public static final ResourceKey<Registry<Research>> RESEARCH_KEY = ResourceKey.createRegistryKey(id("research"));
	public static final ResourceKey<Registry<BookTab>> BOOK_TAB_KEY = ResourceKey.createRegistryKey(id("book_tab"));
	public static final ResourceKey<Registry<BookEntry>> BOOK_ENTRY_KEY = ResourceKey.createRegistryKey(id("book_entry"));
	public static final List<Supplier<Item>> HOOD_ITEMS = List.of(
		DevotionItems.BASIC_MAGE_HOOD, DevotionItems.ENHANCER_MAGE_HOOD, DevotionItems.TRANSMUTER_MAGE_HOOD,
		DevotionItems.EMITTER_MAGE_HOOD, DevotionItems.CONJURER_MAGE_HOOD, DevotionItems.MANIPULATOR_MAGE_HOOD,
		DevotionItems.DEATH_CULTIST_HOOD
	);

	public static final Graph<AuraType> AURA_GRAPH = Util.make(new Graph<>(), (graph) -> {
		for(int i = 0; i < AuraType.values().length - 1; i++)
			graph.addNode(new Graph.Node<>(AuraType.values()[i]));

		for(int i = 0; i < graph.nodes.size(); i++) {
			Graph.Node<AuraType> node = graph.nodes.get(i);

			for(int j = 0; j < graph.nodes.size(); j++) {
				if(i == j)
					continue; // do not loop back to self

				Graph.Node<AuraType> target = graph.nodes.get(j);
				graph.addEdge(node, target);
			}
		}
	});

	@Override
	public void onInitialize(ModContainer mod) {
		CONFIGURATOR.register(DevotionConfig.class);
		RegistryService registryService = RegistryService.get();
		
		// Registries that add gameplay features (e.g. items, blocks, and entities)
		DevotionItems.ITEMS.accept(registryService);
		DevotionBlocks.BLOCKS.accept(registryService);
		DevotionBlocks.BLOCK_ENTITIES.accept(registryService);
		DevotionAltarActions.ACTIONS.accept(registryService);

		// Registries that supplement gameplay features (e.g. data components, materials, and recipes
		DevotionCreativeTabs.CREATIVE_TABS.accept(registryService);
		DevotionMaterials.ARMOR_MATERIALS.accept(registryService);
		DevotionData.DATA_COMPONENTS.accept(registryService);
		DevotionAttributes.ATTRIBUTES.accept(registryService);
		DevotionRecipes.RECIPE_SERIALIZERS.accept(registryService);
		DevotionRecipes.RECIPE_TYPES.accept(registryService);
		DevotionMenus.MENUS.accept(registryService);
		CommandEvents.REGISTER.register(DevotionCommands::register);

		Network.registerPacket(ClientboundAuraPacket.TYPE, ClientboundAuraPacket.class, ClientboundAuraPacket.CODEC, ClientboundAuraPacket::handle);
		Network.registerPacket(ClientboundKnownResearchPacket.TYPE, ClientboundKnownResearchPacket.class, ClientboundKnownResearchPacket.CODEC, ClientboundKnownResearchPacket::handle);
		Network.registerPacket(ClientboundRefreshResearchScreenPacket.TYPE, ClientboundRefreshResearchScreenPacket.class, ClientboundRefreshResearchScreenPacket.CODEC, ClientboundRefreshResearchScreenPacket::handle);

		RegisterCustomLecternMenuEvent.EVENT.register(event -> {
			event.register((level, pos, player, blockEntity, stack) -> new ResearchMenuProvider(level, stack, pos, blockEntity.bookAccess), DevotionItems.RESEARCH_SCROLL);
		});

		ItemMenuInteractionEvent.EVENT.register((menu, player, level, clickAction, slot, slotStack, cursorStack) -> {
			if(clickAction == ClickAction.SECONDARY && cursorStack.isEmpty() && Devotion.HOOD_ITEMS.stream().map(Supplier::get).anyMatch(slotStack::is)) {
				DataComponentType<Boolean> hoodData = DevotionData.CLOSED_HOOD.get();
				boolean value = !slotStack.getOrDefault(hoodData, false);

				slotStack.set(hoodData, value);
				Network.getNetworkHandler().sendToServer(new ServerboundOpenCloseHoodPacket(slot.getContainerSlot(), value));

				if(slotStack.getItem() instanceof Equipable equipable)
					level.playSeededSound(player, player.getX(), player.getY(), player.getZ(), equipable.getEquipSound().value(), SoundSource.NEUTRAL, 1f, 1f, player.getRandom().nextLong());

				return true;
			}

			return false;
		});

		EntityTickEvents.endTick(LivingEntity.class).register((entity, level) -> {
			// TODO decide on how long it should take for aura to regenerate. currently takes 10 minutes to go from no aura to max for the given aura type
			if(!level.isClientSide() && MainHelper.lastTimeAuraChanged(entity) % 240 == 0) {
				for(AuraType auraType : AuraType.values()) {
					AuraAffinity affinity = auraType.getAffinity(MainHelper.getPrimaryAuraType(entity));
					float regenAmount = affinity.getMultiplier() * 0.02f;
//					float altRegenAmount = switch(affinity) {
//						case PRIMARY -> 4f;
//						case SECONDARY -> 3f;
//						case TERTIARY -> 2f;
//						case OPPOSITE -> 1f;
//					};

					MainHelper.regenAura(entity, auraType, regenAmount, false);
				}
			}
		});
	}

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
}
