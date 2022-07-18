package dev.cammiescorner.devotion.api.spells;

public enum AuraType {
	ENHANCER("enhancement", 0x008100), TRANSMUTER("transmutation", 0xbb00c5), EMITTER("emission", 0xffd100),
	CONJURER("conjuration", 0xda0018), MANIPULATOR("manipulation", 0xe08600), SPECIALIST("specialization", 0x0070b9),
	NONE("none", 0xffffff);

	private final String name;
	private final int colour;

	AuraType(String name, int colour) {
		this.colour = colour;
		this.name = name;
	}

	public int getDecimal() {
		return colour;
	}

	public float[] getRgbF() {
		return new float[]{(colour >> 16 & 255) / 255F, (colour >> 8 & 255) / 255F, (colour & 255) / 255F};
	}

	public int[] getRgbI() {
		return new int[]{(colour >> 16 & 255), (colour >> 8 & 255), (colour & 255)};
	}

	public String getName() {
		return name;
	}
}
