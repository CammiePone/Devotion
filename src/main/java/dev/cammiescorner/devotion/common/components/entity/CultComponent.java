package dev.cammiescorner.devotion.common.components.entity;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.cults.Cult;
import dev.cammiescorner.devotion.common.registry.DevotionComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

public class CultComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	private final Object2ObjectMap<Cult, Integer> cultRepMap = new Object2ObjectOpenHashMap<>();

	public CultComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList nbtList = tag.getList("CultReputation", NbtElement.COMPOUND_TYPE);

		cultRepMap.clear();
		for(int i = 0; i < nbtList.size(); i++) {
			NbtCompound map = nbtList.getCompound(i);
			cultRepMap.put(Devotion.CULT.get(new Identifier(map.getString("Cult"))), map.getInt("Reputation"));
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList nbtList = new NbtList();

		cultRepMap.keySet().forEach(cult -> {
			NbtCompound map = new NbtCompound();
			map.putString("Cult", Devotion.CULT.getId(cult).toString());
			map.putInt("Reputation", cultRepMap.get(cult));
			nbtList.add(map);
		});

		tag.put("CultReputation", nbtList);
	}

	public Object2ObjectMap<Cult, Integer> getCultRepMap() {
		return cultRepMap;
	}

	public int getCultReputation(Cult cult) {
		return cultRepMap.get(cult);
	}

	public void setCultReputation(Cult cult, int amount) {
		cultRepMap.put(cult, amount);
		DevotionComponents.CULT_COMPONENT.sync(entity);
	}

	public boolean addReputation(Cult cult, int amount, boolean simulate) {
		if(getCultReputation(cult) < 10) {
			if(!simulate)
				setCultReputation(cult, getCultReputation(cult) + amount);

			return true;
		}

		return false;
	}

	public boolean reduceReputation(Cult cult, int amount, boolean simulate) {
		if(getCultReputation(cult) - amount >= -1) {
			if(!simulate)
				setCultReputation(cult, getCultReputation(cult) - amount);

			return true;
		}

		return false;
	}
}
