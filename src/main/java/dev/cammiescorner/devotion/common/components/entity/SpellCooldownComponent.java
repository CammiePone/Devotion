package dev.cammiescorner.devotion.common.components.entity;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.Spell;
import dev.cammiescorner.devotion.common.registry.DevotionComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class SpellCooldownComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity entity;
	private final Map<Spell, Integer> spellCooldowns = new HashMap<>();

	public SpellCooldownComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		for(Spell spell : Devotion.SPELL)
			if(getSpellCooldown(spell) > 0)
				setSpellCooldown(spell, getSpellCooldown(spell) - 1);
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList nbtList = tag.getList("SpellCooldowns", NbtElement.COMPOUND_TYPE);

		spellCooldowns.clear();
		for(int i = 0; i < nbtList.size(); i++) {
			NbtCompound entry = nbtList.getCompound(i);
			spellCooldowns.put(Devotion.SPELL.get(new Identifier(entry.getString("Spell"))), entry.getInt("Cooldown"));
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList nbtList = new NbtList();

		spellCooldowns.forEach((spell, cooldown) -> {
			NbtCompound entry = new NbtCompound();
			entry.putString("Spell", Devotion.SPELL.getId(spell).toString());
			entry.putInt("Cooldown", cooldown);
			nbtList.add(entry);
		});

		tag.put("SpellCooldowns", nbtList);
	}

	public int getSpellCooldown(Spell spell) {
		return spellCooldowns.get(spell);
	}

	public void setSpellCooldown(Spell spell, int value) {
		spellCooldowns.put(spell, value);
		DevotionComponents.SPELL_COOLDOWN_COMPONENT.sync(entity);
	}
}
