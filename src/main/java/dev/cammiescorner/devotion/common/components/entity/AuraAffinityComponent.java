package dev.cammiescorner.devotion.common.components.entity;

import dev.cammiescorner.devotion.api.entity.DevotionAttributes;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.registry.DevotionComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class AuraAffinityComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	public static final UUID ENHANCEMENT_AFFINITY_UUID = UUID.fromString("2c02e5b8-b75e-44a1-b427-aacbad48e1f8");
	public static final UUID TRANSMUTATION_AFFINITY_UUID = UUID.fromString("7da12750-1910-4a5c-99a1-98018cb64da1");
	public static final UUID EMISSION_AFFINITY_UUID = UUID.fromString("e30be356-8194-41ad-bd1a-21b63ddaf906");
	public static final UUID CONJURATION_AFFINITY_UUID = UUID.fromString("c048db0a-29f6-42c1-9cbc-848380e1f02e");
	public static final UUID MANIPULATION_AFFINITY_UUID = UUID.fromString("c048db0a-29f6-42c1-9cbc-848380e1f02e");
	private AuraType affinity = AuraType.NONE;

	public AuraAffinityComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		affinity = AuraType.values()[tag.getInt("AuraAffinity")];
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("AuraAffinity", affinity.ordinal());
	}

	public AuraType getAffinity() {
		return affinity;
	}

	public void setAffinity(AuraType affinity) {
		this.affinity = affinity;
		double enhancerMod, transmuterMod, emitterMod, conjurerMod, manipulatorMod;
		EntityAttributeInstance enhancement = entity.getAttributeInstance(DevotionAttributes.ENHANCEMENT_AFFINITY);
		EntityAttributeInstance transmutation = entity.getAttributeInstance(DevotionAttributes.TRANSMUTATION_AFFINITY);
		EntityAttributeInstance emission = entity.getAttributeInstance(DevotionAttributes.EMISSION_AFFINITY);
		EntityAttributeInstance conjuration = entity.getAttributeInstance(DevotionAttributes.CONJURATION_AFFINITY);
		EntityAttributeInstance manipulation = entity.getAttributeInstance(DevotionAttributes.MANIPULATION_AFFINITY);

		if(enhancement != null)
			enhancement.tryRemoveModifier(ENHANCEMENT_AFFINITY_UUID);
		if(transmutation != null)
			transmutation.tryRemoveModifier(TRANSMUTATION_AFFINITY_UUID);
		if(emission != null)
			emission.tryRemoveModifier(EMISSION_AFFINITY_UUID);
		if(conjuration != null)
			conjuration.tryRemoveModifier(CONJURATION_AFFINITY_UUID);
		if(manipulation != null)
			manipulation.tryRemoveModifier(MANIPULATION_AFFINITY_UUID);

		switch(affinity) {
			case ENHANCER -> {
				enhancerMod = 1D;
				transmuterMod = 0.8D;
				emitterMod = 0.8D;
				conjurerMod = 0.4D;
				manipulatorMod = 0.4D;
			}
			case TRANSMUTER -> {
				enhancerMod = 0.8D;
				transmuterMod = 1D;
				emitterMod = 0.4D;
				conjurerMod = 0.8D;
				manipulatorMod = 0.1D;
			}
			case EMITTER -> {
				enhancerMod = 0.8D;
				transmuterMod = 0.4D;
				emitterMod = 1D;
				conjurerMod = 0.4D;
				manipulatorMod = 0.8D;
			}
			case CONJURER -> {
				enhancerMod = 0.4D;
				transmuterMod = 0.8D;
				emitterMod = 0.4D;
				conjurerMod = 1D;
				manipulatorMod = 0.8D;
			}
			case MANIPULATOR -> {
				enhancerMod = 0.4D;
				transmuterMod = 0.4D;
				emitterMod = 0.8D;
				conjurerMod = 0.8D;
				manipulatorMod = 1D;
			}
			default -> {
				enhancerMod = 0.5D;
				transmuterMod = 0.5D;
				emitterMod = 0.5D;
				conjurerMod = 0.5D;
				manipulatorMod = 0.5D;
			}
		}

		if(enhancement != null)
			enhancement.addPersistentModifier(new EntityAttributeModifier(ENHANCEMENT_AFFINITY_UUID, "Enhancement Affinity", -1 + enhancerMod, EntityAttributeModifier.Operation.ADDITION));
		if(transmutation != null)
			transmutation.addPersistentModifier(new EntityAttributeModifier(TRANSMUTATION_AFFINITY_UUID, "Transmutation Affinity", -1 + transmuterMod, EntityAttributeModifier.Operation.ADDITION));
		if(emission != null)
			emission.addPersistentModifier(new EntityAttributeModifier(EMISSION_AFFINITY_UUID, "Emission Affinity", -1 + emitterMod, EntityAttributeModifier.Operation.ADDITION));
		if(conjuration != null)
			conjuration.addPersistentModifier(new EntityAttributeModifier(CONJURATION_AFFINITY_UUID, "Conjuration Affinity", -1 + conjurerMod, EntityAttributeModifier.Operation.ADDITION));
		if(manipulation != null)
			manipulation.addPersistentModifier(new EntityAttributeModifier(MANIPULATION_AFFINITY_UUID, "Manipulation Affinity", -1 + manipulatorMod, EntityAttributeModifier.Operation.ADDITION));

		DevotionComponents.AURA_AFFINITY_COMPONENT.sync(entity);
	}
}
