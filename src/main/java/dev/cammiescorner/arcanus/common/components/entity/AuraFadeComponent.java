package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class AuraFadeComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity entity;
	private int timer;

	public AuraFadeComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		if(ArcanusHelper.isCasting(entity) && timer < 10)
			setTimer(++timer);
		if(!ArcanusHelper.isCasting(entity) && timer > 0)
			setTimer(--timer);
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		timer = tag.getByte("AuraFadeTimer");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putByte("AuraFadeTimer", (byte) timer);
	}

	public int getTimer() {
		return timer;
	}

	private void setTimer(int timer) {
		this.timer = timer;
		ArcanusComponents.AURA_FADE_COMPONENT.sync(entity);
	}
}
