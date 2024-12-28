package be.zeldown.batnetwork.internal.mod.network.impl.callback;

import java.util.UUID;
import java.util.function.BiConsumer;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.network.PacketBuffer;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AbstractCallbackPacket {
	
	public static BiConsumer<AbstractCallbackPacket, PacketBuffer> encode = (packet, buffer) -> {
		buffer.writeUtf(packet.callbackUUID.toString());
		
		PacketSerialUtils.writeString(buffer, packet.data.getClass().getName());
		PacketSerialUtils.writeObject(buffer, packet.data);
	};
	
	private UUID   callbackUUID;
	private Object data;

	public static AbstractCallbackPacket decode(final PacketBuffer buffer) {
		UUID callbackUUID = UUID.fromString(buffer.readUtf());
		Object data = null;
		
		try {
			final String className = buffer.readUtf();
			data = PacketSerialUtils.readObject(buffer, Class.forName(className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if (data == null) {
			return null;
		}
		
		return new AbstractCallbackPacket(callbackUUID, data);
	}

}