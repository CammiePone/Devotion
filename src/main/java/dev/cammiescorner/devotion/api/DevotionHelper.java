package dev.cammiescorner.devotion.api;

import dev.cammiescorner.devotion.api.cults.Cult;
import dev.cammiescorner.devotion.api.entity.DevotionAttributes;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.api.spells.Spell;
import dev.cammiescorner.devotion.common.registry.DevotionComponents;
import dev.cammiescorner.devotion.common.registry.DevotionTags;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DevotionHelper {
	public static int getAura(Entity entity) {
		return DevotionComponents.AURA_COMPONENT.get(entity).getAura();
	}

	public static void setAura(Entity entity, int amount) {
		DevotionComponents.AURA_COMPONENT.get(entity).setAura(amount);
	}

	public static boolean addAura(Entity entity, int amount, boolean simulate) {
		return DevotionComponents.AURA_COMPONENT.get(entity).addAura(amount, simulate);
	}

	public static boolean drainAura(Entity entity, int amount, boolean simulate) {
		return DevotionComponents.AURA_COMPONENT.get(entity).drainAura(amount, simulate);
	}

	public static int getMaxAura(Entity entity) {
		return DevotionComponents.AURA_COMPONENT.get(entity).getMaxAura();
	}

	public static boolean isCasting(Entity entity) {
		return DevotionComponents.CASTING_COMPONENT.get(entity).isCasting();
	}

	public static void setCasting(Entity entity, boolean casting) {
		DevotionComponents.CASTING_COMPONENT.get(entity).setCasting(casting);
	}

	public static int getMaxSpells(Entity entity) {
		return DevotionComponents.SPELL_INVENTORY_COMPONENT.get(entity).getMaxSpells();
	}

	public static void setMaxSpells(Entity entity, int amount) {
		DevotionComponents.SPELL_INVENTORY_COMPONENT.get(entity).setMaxSpells(amount);
	}

	public static DefaultedList<Spell> getAllSpells(Entity entity) {
		return DevotionComponents.SPELL_INVENTORY_COMPONENT.get(entity).getAllSpells();
	}

	public static Spell getSpellInSlot(Entity entity, int index) {
		return getAllSpells(entity).get(index);
	}

	public static void setSpellInSlot(Entity entity, Spell spell, int index) {
		getAllSpells(entity).set(index, spell);
	}

	public static Spell getSelectedSpell(Entity entity) {
		return DevotionComponents.CURRENT_SPELL_COMPONENT.get(entity).getSelectedSpell();
	}

	public static void setSelectedSpell(Entity entity, int value) {
		DevotionComponents.CURRENT_SPELL_COMPONENT.get(entity).setSelectedSpell(value);
	}

	public static void castSpell(Spell spell, World world, LivingEntity entity, Vec3d pos) {
		DevotionComponents.CURRENT_SPELL_COMPONENT.get(entity).castSpell(spell, world, entity, pos);
	}

	public static void castCurrentSpell(Entity entity) {
		DevotionComponents.CURRENT_SPELL_COMPONENT.get(entity).castCurrentSpell();
	}

	public static int getSpellCooldown(Entity entity) {
		return DevotionComponents.SPELL_COOLDOWN_COMPONENT.get(entity).getSpellCooldown();
	}

	public static void setSpellCooldown(Entity entity, int value) {
		DevotionComponents.SPELL_COOLDOWN_COMPONENT.get(entity).setSpellCooldown(value);
	}

	public static boolean canCastSpell(PlayerEntity player, Spell spell) {
		return isCasting(player) && getSpellCooldown(player) <= 0 && drainAura(player, actualAuraCost(player, spell), true) && spell.getSpellType() != AuraType.NONE;
	}

	public static int actualAuraCost(PlayerEntity player, Spell spell) {
		EntityAttributeInstance instance = player.getAttributeInstance(DevotionAttributes.AURA_COST);

		if(instance != null)
			return (int) (spell.getAuraCost() * instance.getValue());

		return spell.getAuraCost();
	}

	public static boolean isUniqueSpellActive(Entity entity, Spell spell) {
		return DevotionComponents.UNIQUE_SPELLS_COMPONENT.get(entity).isActive(spell);
	}

	public static void setUniqueSpellActive(Entity entity, Spell spell, boolean active) {
		DevotionComponents.UNIQUE_SPELLS_COMPONENT.get(entity).setActive(spell, active);
	}

	public static Set<Identifier> getResearchIds(Entity entity) {
		return DevotionComponents.RESEARCH_COMPONENT.get(entity).getResearchIds();
	}

	public static boolean giveResearch(Entity entity, Research research, boolean simulate) {
		return DevotionComponents.RESEARCH_COMPONENT.get(entity).giveResearch(research, simulate);
	}

	public static boolean giveResearchById(Entity entity, Identifier researchId, boolean simulate) {
		return DevotionComponents.RESEARCH_COMPONENT.get(entity).giveResearchById(researchId, simulate);
	}

	public static boolean revokeResearch(Entity entity, Research research, boolean simulate) {
		return DevotionComponents.RESEARCH_COMPONENT.get(entity).revokeResearch(research, simulate);
	}

	public static boolean revokeResearchById(Entity entity, Identifier researchId, boolean simulate) {
		return DevotionComponents.RESEARCH_COMPONENT.get(entity).revokeResearchById(researchId, simulate);
	}

	public static Object2ObjectMap<Cult, Integer> getAllCultReputation(Entity entity) {
		return DevotionComponents.CULT_COMPONENT.get(entity).getCultRepMap();
	}

	public static int getCultReputation(Entity entity, Cult cult) {
		return DevotionComponents.CULT_COMPONENT.get(entity).getCultReputation(cult);
	}

	public static void setCultReputation(Entity entity, Cult cult, int amount) {
		DevotionComponents.CULT_COMPONENT.get(entity).setCultReputation(cult, amount);
	}

	public static boolean addReputation(Entity entity, Cult cult, int amount, boolean simulate) {
		return DevotionComponents.CULT_COMPONENT.get(entity).addReputation(cult, amount, simulate);
	}

	public static boolean reduceReputation(Entity entity, Cult cult, int amount, boolean simulate) {
		return DevotionComponents.CULT_COMPONENT.get(entity).reduceReputation(cult, amount, simulate);
	}

	public static float getAuraFade(Entity entity) {
		return DevotionComponents.AURA_FADE_COMPONENT.get(entity).getFadeTimer() * 0.1F;
	}

	public static double getAffinityMultiplier(LivingEntity entity, AuraType type) {
		return switch(type) {
			case ENHANCER -> {
				EntityAttributeInstance attribute = entity.getAttributeInstance(DevotionAttributes.ENHANCEMENT_AFFINITY);
				yield attribute != null ? attribute.getValue() : DevotionAttributes.ENHANCEMENT_AFFINITY.getDefaultValue();
			}
			case TRANSMUTER -> {
				EntityAttributeInstance attribute = entity.getAttributeInstance(DevotionAttributes.TRANSMUTATION_AFFINITY);
				yield attribute != null ? attribute.getValue() : DevotionAttributes.TRANSMUTATION_AFFINITY.getDefaultValue();
			}
			case EMITTER -> {
				EntityAttributeInstance attribute = entity.getAttributeInstance(DevotionAttributes.EMISSION_AFFINITY);
				yield attribute != null ? attribute.getValue() : DevotionAttributes.EMISSION_AFFINITY.getDefaultValue();
			}
			case CONJURER -> {
				EntityAttributeInstance attribute = entity.getAttributeInstance(DevotionAttributes.CONJURATION_AFFINITY);
				yield attribute != null ? attribute.getValue() : DevotionAttributes.CONJURATION_AFFINITY.getDefaultValue();
			}
			case MANIPULATOR -> {
				EntityAttributeInstance attribute = entity.getAttributeInstance(DevotionAttributes.MANIPULATION_AFFINITY);
				yield attribute != null ? attribute.getValue() : DevotionAttributes.MANIPULATION_AFFINITY.getDefaultValue();
			}
			default -> 1D;
		};
	}

	public static Map<AuraType, Double> getAffinityMultipliers(LivingEntity entity) {
		HashMap<AuraType, Double> map = new HashMap<>();

		for(int i = 0; i < 7; i++) {
			AuraType type = AuraType.values()[i];
			map.put(type, getAffinityMultiplier(entity, type));
		}

		return map;
	}

	public static AuraType setAffinity(LivingEntity entity) {
		return DevotionComponents.AURA_AFFINITY_COMPONENT.get(entity).getAffinity();
	}

	public static void setAffinity(LivingEntity entity, AuraType affinity) {
		DevotionComponents.AURA_AFFINITY_COMPONENT.get(entity).setAffinity(affinity);
	}

	public static boolean isValidAltarBlock(BlockState state) {
		return state.isIn(DevotionTags.ALTAR_PALETTE);
	}

	public static HashMap<BlockPos, BlockState> getStructureMap(World world) {
		return DevotionComponents.ALTAR_STRUCTURE_COMPONENT.get(world).getStructureMap();
	}

	public static void constructStructureMap(World world) {
		DevotionComponents.ALTAR_STRUCTURE_COMPONENT.get(world).constructStructureMap();
	}

	public static BlockPos getAltarOffset(World world) {
		return DevotionComponents.ALTAR_STRUCTURE_COMPONENT.get(world).getOffset();
	}
}
