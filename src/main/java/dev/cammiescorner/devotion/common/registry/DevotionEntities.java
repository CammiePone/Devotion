package dev.cammiescorner.devotion.common.registry;

import dev.cammiescorner.devotion.Devotion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class DevotionEntities {
	//-----Entity Map-----//
	public static final LinkedHashMap<EntityType<?>, Identifier> ENTITIES = new LinkedHashMap<>();

	//-----Entities-----//


	//-----Registry-----//
	public static void register() {
		ENTITIES.keySet().forEach(entityType -> Registry.register(Registry.ENTITY_TYPE, ENTITIES.get(entityType), entityType));
	}

	private static <T extends Entity> EntityType<T> create(String name, EntityType<T> type) {
		ENTITIES.put(type, Devotion.id(name));
		return type;
	}
}
