package dev.cammiescorner.devotion.api.research;

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

	public Set<Research> getParents() {
		return parents;
	}

	public Identifier getId() {
		return id;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setParents(Set<Research> parents) {
		this.parents.clear();
		this.parents.addAll(parents);
	}
}
