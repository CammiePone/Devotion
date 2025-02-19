package dev.cammiescorner.devotion.api.world;

import dev.cammiescorner.devotion.api.spells.AuraType;
import net.minecraft.nbt.CompoundTag;

public class AuraNode {
	private float enhancementAura;
	private float transmutationAura;
	private float emissionAura;
	private float conjurationAura;
	private float manipulationAura;

	public AuraNode(float enhancementAura, float transmutationAura, float emissionAura, float conjurationAura, float manipulationAura) {
		this.enhancementAura = enhancementAura;
		this.transmutationAura = transmutationAura;
		this.emissionAura = emissionAura;
		this.conjurationAura = conjurationAura;
		this.manipulationAura = manipulationAura;
	}

	public float getAura(AuraType type) {
		return switch(type) {
			case ENHANCEMENT -> enhancementAura;
			case TRANSMUTATION -> transmutationAura;
			case EMISSION -> emissionAura;
			case CONJURATION -> conjurationAura;
			case MANIPULATION -> manipulationAura;
			case SPECIALIZATION, NONE -> 0f;
		};
	}

	public void writeAuraNode(CompoundTag compoundTag, String name) {
		CompoundTag tag = new CompoundTag();

		tag.putFloat("EnhancementAura", enhancementAura);
		tag.putFloat("TransmutationAura", transmutationAura);
		tag.putFloat("EmissionAura", emissionAura);
		tag.putFloat("ConjurationAura", conjurationAura);
		tag.putFloat("ManipulationAura", manipulationAura);

		compoundTag.put(name, tag);
	}

	public static AuraNode readAuraNode(CompoundTag compoundTag, String name) {
		CompoundTag tag = compoundTag.getCompound(name);

		float enhancementAura = tag.getInt("EnhancementAura");
		float transmutationAura = tag.getInt("TransmutationAura");
		float emissionAura = tag.getInt("EmissionAura");
		float conjurationAura = tag.getInt("ConjurationAura");
		float manipulationAura = tag.getInt("ManipulationAura");

		return new AuraNode(enhancementAura, transmutationAura, emissionAura, conjurationAura, manipulationAura);
	}

	public float getEnhancementAura() {
		return enhancementAura;
	}

	public void setEnhancementAura(float enhancementAura) {
		this.enhancementAura = enhancementAura;
	}

	public float getTransmutationAura() {
		return transmutationAura;
	}

	public void setTransmutationAura(float transmutationAura) {
		this.transmutationAura = transmutationAura;
	}

	public float getEmissionAura() {
		return emissionAura;
	}

	public void setEmissionAura(float emissionAura) {
		this.emissionAura = emissionAura;
	}

	public float getConjurationAura() {
		return conjurationAura;
	}

	public void setConjurationAura(float conjurationAura) {
		this.conjurationAura = conjurationAura;
	}

	public float getManipulationAura() {
		return manipulationAura;
	}

	public void setManipulationAura(float manipulationAura) {
		this.manipulationAura = manipulationAura;
	}
}
