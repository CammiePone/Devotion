package dev.cammiescorner.devotion.api.spells;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.Devotion;
import dev.upcraft.sparkweave.api.color.Color;
import net.minecraft.util.StringRepresentable;

public enum AuraType implements StringRepresentable {
	ENHANCEMENT("enhancement", 0x009222), TRANSMUTATION("transmutation", 0xcb00d4), EMISSION("emission", 0xffeC07),
	CONJURATION("conjuration", 0x9c001d), MANIPULATION("manipulation", 0xff9f00), SPECIALIZATION("specialization", 0x2847bb),
	NONE("none", 0xffffff);

	public static final Codec<AuraType> CODEC = StringRepresentable.fromEnum(AuraType::values);
	private final String name;
	private final Color color;

	AuraType(String name, int color) {
		this.color = Color.fromInt(color, Color.Ordering.RGB);
		this.name = name;
	}

	public boolean isSecondaryAffinity(AuraType primaryAuraType) {
		return switch(primaryAuraType) {
			case ENHANCEMENT -> this == EMISSION || this == TRANSMUTATION;
			case TRANSMUTATION -> this == ENHANCEMENT || this == CONJURATION;
			case EMISSION -> this == MANIPULATION || this == ENHANCEMENT;
			case CONJURATION -> this == TRANSMUTATION || this == SPECIALIZATION;
			case MANIPULATION -> this == SPECIALIZATION || this == EMISSION;
			case SPECIALIZATION -> this == CONJURATION || this == MANIPULATION;
			case NONE -> false;
		};
	}

	public boolean isTertiaryAffinity(AuraType primaryAuraType) {
		return switch(primaryAuraType) {
			case ENHANCEMENT -> this == MANIPULATION || this == CONJURATION;
			case TRANSMUTATION -> this == EMISSION || this == SPECIALIZATION;
			case EMISSION -> this == SPECIALIZATION || this == TRANSMUTATION;
			case CONJURATION -> this == MANIPULATION || this == ENHANCEMENT;
			case MANIPULATION -> this == CONJURATION || this == ENHANCEMENT;
			case SPECIALIZATION -> this == EMISSION || this == TRANSMUTATION;
			case NONE -> false;
		};
	}

	public AuraAffinity getAffinity(AuraType primaryAuraType) {
		return this == primaryAuraType ? AuraAffinity.PRIMARY : isSecondaryAffinity(primaryAuraType) ? AuraAffinity.SECONDARY : isTertiaryAffinity(primaryAuraType) ? AuraAffinity.TERTIARY : AuraAffinity.OPPOSITE;
	}

	public float getAffinityMultiplier(AuraType primaryAuraType) {
		return getAffinity(primaryAuraType).getMultiplier();
	}

	public Color getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public static AuraType byName(String name) {
		for(AuraType auraType : AuraType.values()) {
			if(auraType.getSerializedName().equals(name))
				return auraType;
		}

		Devotion.LOGGER.warn("Aura Type with name {} doesn't exist! Defaulting to NONE.", name);
		return NONE;
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}
