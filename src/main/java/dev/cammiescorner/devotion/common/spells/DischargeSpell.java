package dev.cammiescorner.devotion.common.spells;

import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.api.spells.Spell;
import dev.cammiescorner.devotion.api.spells.SpellComplexity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DischargeSpell extends Spell {
	public DischargeSpell() {
		super(AuraType.TRANSMUTER, SpellComplexity.INTRICATE, 0, false);
	}

	@Override
	public void cast(World world, LivingEntity entity, Vec3d pos) {

	}
}
