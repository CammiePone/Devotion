package dev.cammiescorner.devotion.common.components.entity;

import dev.cammiescorner.devotion.common.registry.DevotionComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class SpellCooldownComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity entity;
	private int spellCooldown = 0;

	public SpellCooldownComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		if(getSpellCooldown() > 0)
			setSpellCooldown(getSpellCooldown() - 1);
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		spellCooldown = tag.getInt("SpellCooldown");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("SpellCooldown", spellCooldown);
	}

	public int getSpellCooldown() {
		return spellCooldown;
	}

	public void setSpellCooldown(int value) {
		spellCooldown = value;
		DevotionComponents.SPELL_COOLDOWN_COMPONENT.sync(entity);
	}
}
