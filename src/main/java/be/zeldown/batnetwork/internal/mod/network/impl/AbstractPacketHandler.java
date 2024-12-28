package be.zeldown.batnetwork.internal.mod.network.impl;

import java.util.function.Consumer;
import java.util.function.Supplier;

import be.zeldown.batnetwork.lib.Packet;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkEvent;

public class AbstractPacketHandler {
	
	public static void handle(final AbstractPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final Packet packet = message.getPacket();
			if (packet == null) {
				return;
			}
			
			final Dist side = FMLEnvironment.dist;
			packet.setSide(side);
			
			if (side == Dist.CLIENT) {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
					packet.processClient();
					processCallback(packet);
				});
			} else {
				DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> {
					final ServerPlayerEntity player = ctx.get().getSender();
					packet.setEffectivePlayer(player);
					packet.processServer(player);
					processCallback(packet);
				});
			}
		});
		ctx.get().setPacketHandled(true);
	}
	
	private static void processCallback(Packet packet) {
		final Consumer<Object> callback = packet.pollCallback();
		if (callback != null) {
			callback.accept(packet);
		}
	}

}
