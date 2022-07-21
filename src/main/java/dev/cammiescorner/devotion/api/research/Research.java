package dev.cammiescorner.devotion.api.research;

import dev.cammiescorner.devotion.Devotion;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class Research {
	private final Set<Research> parents = new HashSet<>();
	private final Identifier id;
	private final boolean isHidden;

	public Research(Identifier id, boolean isHidden) {
		this.id = id;
		this.isHidden = isHidden;
	}

	public static Research getById(Identifier id) {
		return Devotion.RESEARCH.get(id);
	}

	public Set<Research> getParents() {
		return parents;
	}

	public Set<Identifier> getParentIds() {
		Set<Identifier> parentIds = new HashSet<>();

		for(Research parent : parents)
			parentIds.add(parent.getId());

		return parentIds;
	}

	public Identifier getId() {
		return id;
	}

	public String getTranslationKey() {
		return "research." + id.getNamespace() + "." + id.getPath();
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setParents(Set<Research> parents) {
		this.parents.clear();
		this.parents.addAll(parents);
	}
}
