package dev.cammiescorner.devotion.fabric.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.fabric.common.components.chunk.AuraNodeComponent;
import dev.cammiescorner.devotion.fabric.common.components.entity.AuraComponent;
import dev.cammiescorner.devotion.fabric.common.components.entity.KnownResearchComponent;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentInitializer;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;

public class DevotionComponents implements EntityComponentInitializer, BlockComponentInitializer, ScoreboardComponentInitializer, ChunkComponentInitializer {
	// Entity Components
	public static final ComponentKey<AuraComponent> AURA = createComponent("aura", AuraComponent.class);
	public static final ComponentKey<KnownResearchComponent> KNOWN_RESEARCH = createComponent("known_research", KnownResearchComponent.class);

	// BlockEntity Components

	// Scoreboard Components

	// Chunk Components
	public static final ComponentKey<AuraNodeComponent> AURA_NODE = createComponent("aura_node", AuraNodeComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(Player.class, AURA).respawnStrategy(RespawnCopyStrategy.LOSSLESS_ONLY).end(AuraComponent::new);
		registry.beginRegistration(Player.class, KNOWN_RESEARCH).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(KnownResearchComponent::new);
	}

	@Override
	public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry registry) {

	}

	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {

	}

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(AURA_NODE, AuraNodeComponent::new);
	}

	private static <T extends Component> ComponentKey<T> createComponent(String name, Class<T> component) {
		return ComponentRegistry.getOrCreate(Devotion.id(name), component);
	}
}
