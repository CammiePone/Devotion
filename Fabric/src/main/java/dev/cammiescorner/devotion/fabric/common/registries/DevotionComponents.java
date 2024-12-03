package dev.cammiescorner.devotion.fabric.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.fabric.common.components.entity.AuraComponent;
import dev.cammiescorner.devotion.fabric.common.components.entity.KnownResearchComponent;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;

public class DevotionComponents implements EntityComponentInitializer, ScoreboardComponentInitializer {
	// Entity Components
	public static final ComponentKey<AuraComponent> AURA = createComponent("aura", AuraComponent.class);
	public static final ComponentKey<KnownResearchComponent> KNOWN_RESEARCH = createComponent("known_research", KnownResearchComponent.class);

	// BlockEntity Components

	// Scoreboard Components

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(Player.class, AURA).respawnStrategy(RespawnCopyStrategy.LOSSLESS_ONLY).end(AuraComponent::new);
		registry.beginRegistration(Player.class, KNOWN_RESEARCH).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(KnownResearchComponent::new);
	}

	@Override
	public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry registry) {

	}

	private static <T extends Component> ComponentKey<T> createComponent(String name, Class<T> component) {
		return ComponentRegistry.getOrCreate(Devotion.id(name), component);
	}
}
