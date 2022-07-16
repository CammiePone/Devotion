package dev.cammiescorner.devotion.common.spells;

import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.api.spells.Spell;
import dev.cammiescorner.devotion.api.spells.SpellComplexity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FullCowlSpell extends Spell {
	public FullCowlSpell() {
		super(AuraType.ENHANCER, SpellComplexity.COMPLEX, 200, true);
	}

	@Override
	public void cast(World world, LivingEntity entity, Vec3d pos) {

	}
}
