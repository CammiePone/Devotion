package dev.cammiescorner.devotion.common.components.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class CanUseAuraComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	private boolean canUseAura;

	public CanUseAuraComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		canUseAura = tag.getBoolean("CanUseAura");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("CanUseAura", canUseAura);
	}

	public boolean canUseAura() {
		return canUseAura;
	}

	public void setCanUseAura(boolean canUseAura) {
		this.canUseAura = canUseAura;

	}
}
