package dev.cammiescorner.devotion.api.actions;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.api.TriConsumer;
import dev.cammiescorner.devotion.api.registries.DevotionRegistries;
import dev.cammiescorner.devotion.common.blocks.entities.AltarFocusBlockEntity;
import dev.cammiescorner.devotion.common.registries.DevotionAltarActions;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public abstract class ConfiguredAltarAction {
	public static final Codec<Holder<ConfiguredAltarAction>> CODEC = RegistryFixedCodec.create(DevotionRegistries.CONFIGURED_ALTAR_ACTION);
	public static final Codec<ConfiguredAltarAction> DIRECT_CODEC = DevotionAltarActions.REGISTRY.byNameCodec().dispatch(ConfiguredAltarAction::getAltarAction, AltarAction::codec);
	public static final StreamCodec<RegistryFriendlyByteBuf, ConfiguredAltarAction> OBJ_STREAM_CODEC = ByteBufCodecs.registry(DevotionRegistries.CONFIGURED_ALTAR_ACTION);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ConfiguredAltarAction>> HOLDER_STREAM_CODEC = ByteBufCodecs.holderRegistry(DevotionRegistries.CONFIGURED_ALTAR_ACTION);
	private final AltarAction altarAction;

	public ConfiguredAltarAction(AltarAction altarAction) {
		this.altarAction = altarAction;
	}

	public abstract void run(ServerLevel level, @Nullable ServerPlayer player, AltarFocusBlockEntity altar);

	public AltarAction getAltarAction() {
		return altarAction;
	}

	public static ConfiguredAltarAction of(TriConsumer<ServerLevel, @Nullable ServerPlayer, AltarFocusBlockEntity> consumer, AltarAction type) {
		return new ConfiguredAltarAction(type) {
			@Override
			public void run(ServerLevel level, @Nullable ServerPlayer player, AltarFocusBlockEntity altar) {
				consumer.accept(level, player, altar);
			}
		};
	}
}
