package dev.cammiescorner.devotion.common.components.entity;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.Spell;
import dev.cammiescorner.devotion.common.registry.DevotionComponents;
import dev.cammiescorner.devotion.common.registry.DevotionSpells;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class SpellInventoryComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	private final DefaultedList<Spell> spells = DefaultedList.ofSize(11, DevotionSpells.EMPTY);
	private int maxSpells = 2;

	public SpellInventoryComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList listTag = tag.getList("Spells", NbtType.STRING);

		maxSpells = tag.getInt("MaxSpells");

		for(int i = 0; i < listTag.size(); i++)
			spells.set(i, Devotion.SPELL.get(new Identifier(listTag.getString(i))));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList listTag = new NbtList();

		tag.putInt("MaxSpells", maxSpells);
		spells.forEach(value -> listTag.add(NbtString.of(Devotion.SPELL.getId(value).toString())));
		tag.put("Spells", listTag);
	}

	public int getMaxSpells() {
		return maxSpells;
	}

	public void setMaxSpells(int amount) {
		maxSpells = amount;
		DevotionComponents.SPELL_INVENTORY_COMPONENT.sync(entity);
	}

	public DefaultedList<Spell> getAllSpells() {
		return spells;
	}

	public Spell getSpellInSlot(int index) {
		return getAllSpells().get(index);
	}

	public void setSpellInSlot(Spell spell, int index) {
		getAllSpells().set(index, spell);
		DevotionComponents.SPELL_INVENTORY_COMPONENT.sync(entity);
	}
}
