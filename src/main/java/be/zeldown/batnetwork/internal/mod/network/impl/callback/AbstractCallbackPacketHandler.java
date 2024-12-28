package be.zeldown.batnetwork.internal.mod.network.impl.callback;

import java.util.function.Consumer;
import java.util.function.Supplier;

import be.zeldown.batnetwork.lib.Packet;
import net.minecraftforge.fml.network.NetworkEvent;

public class AbstractCallbackPacketHandler {
	
	public static void handle(final AbstractCallbackPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if (message.getCallbackUUID() == null) {
				return;
			}
			
			final Consumer<Object> callback = Packet.pollCallback(message.getCallbackUUID());
			if (callback != null) {
				callback.accept(message.getData());
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
