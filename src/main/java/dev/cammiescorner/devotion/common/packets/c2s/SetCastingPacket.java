package dev.cammiescorner.devotion.common.packets.c2s;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.DevotionHelper;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;

public class SetCastingPacket {
	public static final Identifier ID = Devotion.id("set_casting");

	public static void send(boolean casting) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBoolean(casting);

		ClientSidePacketRegistryImpl.INSTANCE.sendToServer(ID, buf);
	}

	public static void handler(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		boolean casting = buf.readBoolean();

		server.execute(() -> {
			DevotionHelper.setCasting(player, casting);
		});
	}
}
