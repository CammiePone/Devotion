package dev.cammiescorner.devotion.api.world;

import dev.cammiescorner.devotion.api.spells.AuraType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;

public class AuraNode {
	private final Map<AuraType, Float> auraMap = new HashMap<>();

	public AuraNode(Map<AuraType, Float> auraMap) {
		this.auraMap.putAll(auraMap);
	}

	public float getAura(AuraType type) {
		return auraMap.get(type);
	}

	public void setAura(AuraType auraType, float aura) {
		auraMap.replace(auraType, aura);
	}

	public Map<AuraType, Float> viewAuraMap() {
		return Map.copyOf(auraMap);
	}

	public void writeAuraNode(CompoundTag compoundTag, String name) {
		CompoundTag tag = new CompoundTag();
		ListTag listTag = new ListTag();

		for(AuraType auraType : auraMap.keySet()) {
			CompoundTag auraTag = new CompoundTag();
			float aura = auraMap.get(auraType);

			auraTag.putString("AuraType", auraType.getSerializedName());
			auraTag.putFloat("Aura", aura);
			listTag.add(auraTag);
		}

		tag.put("StoredAura", listTag);
		compoundTag.put(name, tag);
	}

	public static AuraNode readAuraNode(CompoundTag compoundTag, String name) {
		CompoundTag tag = compoundTag.getCompound(name);
		ListTag listTag = tag.getList("StoredAura", Tag.TAG_COMPOUND);
		Map<AuraType, Float> auraMap = new HashMap<>();


		for(int i = 0; i < listTag.size(); i++) {
			CompoundTag auraTag = listTag.getCompound(i);
			AuraType auraType = AuraType.byName(auraTag.getString("AuraType"));
			float aura = auraTag.getFloat("Aura");

			auraMap.put(auraType, aura);
		}

		return new AuraNode(auraMap);
	}
}
