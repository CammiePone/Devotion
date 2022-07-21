package dev.cammiescorner.devotion.common.components.entity;

import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.common.registry.DevotionComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class ResearchComponent implements AutoSyncedComponent {
	private static final Set<Identifier> RESEARCH_IDS = new HashSet<>();
	private final LivingEntity entity;

	public ResearchComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList nbtList = tag.getList("UnlockedResearch", NbtElement.STRING_TYPE);
		RESEARCH_IDS.clear();

		for(int i = 0; i < nbtList.size(); i++)
			RESEARCH_IDS.add(new Identifier(nbtList.getString(i)));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList nbtList = new NbtList();

		for(Identifier researchId : RESEARCH_IDS)
			nbtList.add(NbtString.of(researchId.toString()));

		tag.put("UnlockedResearch", nbtList);
	}

	/**
	 * Do *NOT* use this for altering the set! It will *NOT* sync data if you do!
	 */
	public Set<Identifier> getResearchIds() {
		return RESEARCH_IDS;
	}

	public boolean giveResearch(Research research, boolean simulate) {
		if(!RESEARCH_IDS.contains(research.getId())) {
			if(!simulate) {
				RESEARCH_IDS.add(research.getId());
				DevotionComponents.RESEARCH_COMPONENT.sync(entity);
			}

			return true;
		}

		return false;
	}

	public boolean giveResearchById(Identifier researchId, boolean simulate) {
		return giveResearch(Research.getById(researchId), simulate);
	}

	public boolean revokeResearch(Research research, boolean simulate) {
		if(RESEARCH_IDS.contains(research.getId())) {
			if(!simulate) {
				RESEARCH_IDS.remove(research.getId());
				DevotionComponents.RESEARCH_COMPONENT.sync(entity);
			}

			return true;
		}

		return false;
	}

	public boolean revokeResearchById(Identifier researchId, boolean simulate) {
		return revokeResearch(Research.getById(researchId), simulate);
	}
}
