package dev.cammiescorner.devotion.common.components.entity;

import dev.cammiescorner.devotion.api.DevotionHelper;
import dev.cammiescorner.devotion.common.registry.DevotionComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class AuraFadeComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity entity;
	private long timer;

	public AuraFadeComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		if(DevotionHelper.isCasting(entity) && timer < 10)
			setFadeTimer(++timer);
		if(!DevotionHelper.isCasting(entity) && timer > 0)
			setFadeTimer(--timer);
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		timer = tag.getByte("AuraFadeTimer");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putByte("AuraFadeTimer", (byte) timer);
	}

	public long getFadeTimer() {
		return timer;
	}

	private void setFadeTimer(long activatedTime) {
		this.timer = activatedTime;
		DevotionComponents.AURA_FADE_COMPONENT.sync(entity);
	}
}
