package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.items.*;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

public class DevotionItems {
	public static final RegistryHandler<Item> ITEMS = RegistryHandler.create(Registries.ITEM, Devotion.MOD_ID);

	public static final RegistrySupplier<Item> SCRIPTS_OF_DEVOTION = ITEMS.register("scripts_of_devotion", ScriptsOfDevotionItem::new);
	public static final RegistrySupplier<Item> RESEARCH_SCROLL = ITEMS.register("research_scroll", ResearchScrollItem::new);
	public static final RegistrySupplier<Item> STAFF_CORE = ITEMS.register("staff_core", StaffCoreItem::new);
	public static final RegistrySupplier<Item> STAFF_CAP = ITEMS.register("staff_cap", StaffCapItem::new);
	public static final RegistrySupplier<Item> STAFF = ITEMS.register("staff", StaffItem::new);
	public static final RegistrySupplier<Item> AURAMETER = ITEMS.register("aurameter", AurameterItem::new);

	public static final RegistrySupplier<Item> BASIC_MAGE_HOOD = ITEMS.register("basic_mage_hood", () -> new MageRobesItem(ArmorItem.Type.HELMET, new Item.Properties().component(DevotionData.CLOSED_HOOD.get(), false)));
	public static final RegistrySupplier<Item> BASIC_MAGE_ROBE = ITEMS.register("basic_mage_robe", () -> new MageRobesItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistrySupplier<Item> BASIC_MAGE_BELT = ITEMS.register("basic_mage_belt", () -> new MageRobesItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistrySupplier<Item> BASIC_MAGE_BOOTS = ITEMS.register("basic_mage_boots", () -> new MageRobesItem(ArmorItem.Type.BOOTS, new Item.Properties()));
	public static final RegistrySupplier<Item> ENHANCER_MAGE_HOOD = ITEMS.register("enhancer_mage_hood", () -> new MageRobesItem(ArmorItem.Type.HELMET, new Item.Properties().component(DevotionData.CLOSED_HOOD.get(), false), AuraType.ENHANCEMENT));
	public static final RegistrySupplier<Item> ENHANCER_MAGE_ROBE = ITEMS.register("enhancer_mage_robe", () -> new MageRobesItem(ArmorItem.Type.CHESTPLATE, new Item.Properties(), AuraType.ENHANCEMENT));
	public static final RegistrySupplier<Item> ENHANCER_MAGE_BELT = ITEMS.register("enhancer_mage_belt", () -> new MageRobesItem(ArmorItem.Type.LEGGINGS, new Item.Properties(), AuraType.ENHANCEMENT));
	public static final RegistrySupplier<Item> ENHANCER_MAGE_BOOTS = ITEMS.register("enhancer_mage_boots", () -> new MageRobesItem(ArmorItem.Type.BOOTS, new Item.Properties(), AuraType.ENHANCEMENT));
	public static final RegistrySupplier<Item> TRANSMUTER_MAGE_HOOD = ITEMS.register("transmuter_mage_hood", () -> new MageRobesItem(ArmorItem.Type.HELMET, new Item.Properties().component(DevotionData.CLOSED_HOOD.get(), false), AuraType.TRANSMUTATION));
	public static final RegistrySupplier<Item> TRANSMUTER_MAGE_ROBE = ITEMS.register("transmuter_mage_robe", () -> new MageRobesItem(ArmorItem.Type.CHESTPLATE, new Item.Properties(), AuraType.TRANSMUTATION));
	public static final RegistrySupplier<Item> TRANSMUTER_MAGE_BELT = ITEMS.register("transmuter_mage_belt", () -> new MageRobesItem(ArmorItem.Type.LEGGINGS, new Item.Properties(), AuraType.TRANSMUTATION));
	public static final RegistrySupplier<Item> TRANSMUTER_MAGE_BOOTS = ITEMS.register("transmuter_mage_boots", () -> new MageRobesItem(ArmorItem.Type.BOOTS, new Item.Properties(), AuraType.TRANSMUTATION));
	public static final RegistrySupplier<Item> EMITTER_MAGE_HOOD = ITEMS.register("emitter_mage_hood", () -> new MageRobesItem(ArmorItem.Type.HELMET, new Item.Properties().component(DevotionData.CLOSED_HOOD.get(), false), AuraType.EMISSION));
	public static final RegistrySupplier<Item> EMITTER_MAGE_ROBE = ITEMS.register("emitter_mage_robe", () -> new MageRobesItem(ArmorItem.Type.CHESTPLATE, new Item.Properties(), AuraType.EMISSION));
	public static final RegistrySupplier<Item> EMITTER_MAGE_BELT = ITEMS.register("emitter_mage_belt", () -> new MageRobesItem(ArmorItem.Type.LEGGINGS, new Item.Properties(), AuraType.EMISSION));
	public static final RegistrySupplier<Item> EMITTER_MAGE_BOOTS = ITEMS.register("emitter_mage_boots", () -> new MageRobesItem(ArmorItem.Type.BOOTS, new Item.Properties(), AuraType.EMISSION));
	public static final RegistrySupplier<Item> CONJURER_MAGE_HOOD = ITEMS.register("conjurer_mage_hood", () -> new MageRobesItem(ArmorItem.Type.HELMET, new Item.Properties().component(DevotionData.CLOSED_HOOD.get(), false), AuraType.CONJURATION));
	public static final RegistrySupplier<Item> CONJURER_MAGE_ROBE = ITEMS.register("conjurer_mage_robe", () -> new MageRobesItem(ArmorItem.Type.CHESTPLATE, new Item.Properties(), AuraType.CONJURATION));
	public static final RegistrySupplier<Item> CONJURER_MAGE_BELT = ITEMS.register("conjurer_mage_belt", () -> new MageRobesItem(ArmorItem.Type.LEGGINGS, new Item.Properties(), AuraType.CONJURATION));
	public static final RegistrySupplier<Item> CONJURER_MAGE_BOOTS = ITEMS.register("conjurer_mage_boots", () -> new MageRobesItem(ArmorItem.Type.BOOTS, new Item.Properties(), AuraType.CONJURATION));
	public static final RegistrySupplier<Item> MANIPULATOR_MAGE_HOOD = ITEMS.register("manipulator_mage_hood", () -> new MageRobesItem(ArmorItem.Type.HELMET, new Item.Properties().component(DevotionData.CLOSED_HOOD.get(), false), AuraType.MANIPULATION));
	public static final RegistrySupplier<Item> MANIPULATOR_MAGE_ROBE = ITEMS.register("manipulator_mage_robe", () -> new MageRobesItem(ArmorItem.Type.CHESTPLATE, new Item.Properties(), AuraType.MANIPULATION));
	public static final RegistrySupplier<Item> MANIPULATOR_MAGE_BELT = ITEMS.register("manipulator_mage_belt", () -> new MageRobesItem(ArmorItem.Type.LEGGINGS, new Item.Properties(), AuraType.MANIPULATION));
	public static final RegistrySupplier<Item> MANIPULATOR_MAGE_BOOTS = ITEMS.register("manipulator_mage_boots", () -> new MageRobesItem(ArmorItem.Type.BOOTS, new Item.Properties(), AuraType.MANIPULATION));

	public static final RegistrySupplier<Item> DEATH_CULTIST_HOOD = ITEMS.register("death_cultist_hood", () -> new MageRobesItem(ArmorItem.Type.HELMET, new Item.Properties().component(DevotionData.CLOSED_HOOD.get(), false)));
	public static final RegistrySupplier<Item> DEATH_CULTIST_ROBE = ITEMS.register("death_cultist_robe", () -> new MageRobesItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistrySupplier<Item> DEATH_CULTIST_LEGGINGS = ITEMS.register("death_cultist_leggings", () -> new MageRobesItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistrySupplier<Item> DEATH_CULTIST_BOOTS = ITEMS.register("death_cultist_boots", () -> new MageRobesItem(ArmorItem.Type.BOOTS, new Item.Properties()));
	public static final RegistrySupplier<Item> DEATH_CULT_LEADER_HEADDRESS = ITEMS.register("death_cult_leader_headdress", () -> new MageRobesItem(ArmorItem.Type.HELMET, new Item.Properties(), AuraType.NONE, AuraType.values()));
	public static final RegistrySupplier<Item> DEATH_CULT_LEADER_CLOAK = ITEMS.register("death_cult_leader_cloak", () -> new MageRobesItem(ArmorItem.Type.CHESTPLATE, new Item.Properties(), AuraType.NONE, AuraType.values()));
	public static final RegistrySupplier<Item> DEATH_CULT_LEADER_LEGGINGS = ITEMS.register("death_cult_leader_leggings", () -> new MageRobesItem(ArmorItem.Type.LEGGINGS, new Item.Properties(), AuraType.NONE, AuraType.values()));
	public static final RegistrySupplier<Item> DEATH_CULT_LEADER_BOOTS = ITEMS.register("death_cult_leader_boots", () -> new MageRobesItem(ArmorItem.Type.BOOTS, new Item.Properties(), AuraType.NONE, AuraType.values()));

	public static final List<Supplier<Item>> HOOD_ITEMS = List.of(
		BASIC_MAGE_HOOD, ENHANCER_MAGE_HOOD, TRANSMUTER_MAGE_HOOD,
		EMITTER_MAGE_HOOD, CONJURER_MAGE_HOOD, MANIPULATOR_MAGE_HOOD,
		DEATH_CULTIST_HOOD
	);
}
