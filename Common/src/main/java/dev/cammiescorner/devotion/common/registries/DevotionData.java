package dev.cammiescorner.devotion.common.registries;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.research.RiddleData;
import dev.cammiescorner.devotion.api.staves.StaffCap;
import dev.cammiescorner.devotion.api.staves.StaffCore;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.List;

public class DevotionData {
	public static final RegistryHandler<DataComponentType<?>> DATA_COMPONENTS = RegistryHandler.create(Registries.DATA_COMPONENT_TYPE, Devotion.MOD_ID);

	public static final RegistrySupplier<DataComponentType<Holder<StaffCore>>> STAFF_CORE = DATA_COMPONENTS.register("staff_core", () -> DataComponentType.<Holder<StaffCore>>builder()
		.persistent(StaffCore.CODEC)
		.networkSynchronized(StaffCore.STREAM_CODEC)
		.cacheEncoding()
		.build()
	);
	public static final RegistrySupplier<DataComponentType<Holder<StaffCap>>> STAFF_CAPS = DATA_COMPONENTS.register("staff_caps", () -> DataComponentType.<Holder<StaffCap>>builder()
		.persistent(StaffCap.CODEC)
		.networkSynchronized(StaffCap.STREAM_CODEC)
		.cacheEncoding()
		.build()
	);
	public static final RegistrySupplier<DataComponentType<Boolean>> CLOSED_HOOD = DATA_COMPONENTS.register("closed_hood", () -> DataComponentType.<Boolean>builder()
		.persistent(Codec.BOOL)
		.networkSynchronized(ByteBufCodecs.BOOL)
		.cacheEncoding()
		.build()
	);
	public static final RegistrySupplier<DataComponentType<Boolean>> SCROLL_COMPLETED = DATA_COMPONENTS.register("scroll_completed", () -> DataComponentType.<Boolean>builder()
		.persistent(Codec.BOOL)
		.networkSynchronized(ByteBufCodecs.BOOL)
		.cacheEncoding()
		.build()
	);
	public static final RegistrySupplier<DataComponentType<Long>> SCROLL_COMPLETED_TIME = DATA_COMPONENTS.register("scroll_completed_time", () -> DataComponentType.<Long>builder()
		.persistent(Codec.LONG)
		.networkSynchronized(ByteBufCodecs.VAR_LONG)
		.cacheEncoding()
		.build()
	);
	public static final RegistrySupplier<DataComponentType<Holder<Research>>> RESEARCH = DATA_COMPONENTS.register("research", () -> DataComponentType.<Holder<Research>>builder()
		.persistent(Research.CODEC)
		.networkSynchronized(Research.STREAM_CODEC)
		.cacheEncoding()
		.build()
	);
	public static final RegistrySupplier<DataComponentType<RiddleData>> RIDDLE_DATA = DATA_COMPONENTS.register("riddle_data", () -> DataComponentType.<RiddleData>builder()
		.persistent(RiddleData.CODEC)
		.networkSynchronized(RiddleData.STREAM_CODEC)
		.cacheEncoding()
		.build()
	);
	public static final RegistrySupplier<DataComponentType<List<Integer>>> UNDO_BUFFER = DATA_COMPONENTS.register("undo_buffer", () -> DataComponentType.<List<Integer>>builder()
		.persistent(Codec.INT.listOf())
		.networkSynchronized(ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list()))
		.cacheEncoding()
		.build()
	);
}
