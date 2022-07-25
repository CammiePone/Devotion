package dev.cammiescorner.devotion.common.components.entity;

import dev.cammiescorner.devotion.api.DevotionHelper;
import dev.cammiescorner.devotion.api.events.common.UniqueSpellCallback;
import dev.cammiescorner.devotion.api.spells.Spell;
import dev.cammiescorner.devotion.api.spells.SpellComplexity;
import dev.cammiescorner.devotion.common.registry.DevotionComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CurrentSpellComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	private int selectedSpell = 0;

	public CurrentSpellComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		selectedSpell = tag.getInt("SelectedSpell");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("SelectedSpell", selectedSpell);
	}

	public Spell getSelectedSpell() {
		return DevotionComponents.SPELL_INVENTORY_COMPONENT.get(entity).getAllSpells().get(selectedSpell);
	}

	public void setSelectedSpell(int value) {
		selectedSpell = value;
		DevotionComponents.CURRENT_SPELL_COMPONENT.sync(entity);
	}

	public void castSpell(Spell spell, World world, LivingEntity entity, Vec3d pos) {
		UniqueSpellCallback.EVENT.invoker().run(spell, world, entity, pos);
		spell.cast(world, entity, pos);

		if(entity instanceof PlayerEntity player && !player.isCreative()) {
			int cost = DevotionHelper.actualAuraCost(player, spell);

			if(spell.getSpellComplexity() != SpellComplexity.UNIQUE) {
				if(spell.isInstant()) {
					DevotionHelper.drainAura(player, cost, false);
					DevotionHelper.setSpellCooldown(entity, spell, spell.getSpellCooldown());
				}
				else if(world.getTime() % 20 == 0)
					DevotionHelper.drainAura(player, cost, false);
			}
			else if(DevotionHelper.isUniqueSpellActive(entity, spell) && world.getTime() % 20 == 0) {
				DevotionHelper.drainAura(player, 1, false);
			}
		}
	}

	public void castCurrentSpell() {
		castSpell(getSelectedSpell(), entity.world, entity, entity.getPos());
	}
}
