package be.zeldown.batnetwork.internal.mod.network.impl;

import java.util.UUID;
import java.util.function.BiConsumer;

import be.zeldown.batnetwork.lib.Packet;
import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.network.PacketBuffer;

@Getter
@NoArgsConstructor
public class AbstractPacket {
	
	public static BiConsumer<AbstractPacket, PacketBuffer> encode = (packet, buffer) -> {
		packet.getPacket().write(buffer);
	};
	
	private String       packetName;
	private Packet packet;
	
	public AbstractPacket(final String packetName) {
		this.packetName = packetName;
	}
	
	public AbstractPacket(final Packet packet) {
		this.packet = packet;
	}

	public static AbstractPacket decode(final PacketBuffer buffer) {
		final Packet packet = Packet.getPacket(buffer.readUtf());
		if (packet == null) {
			return null;
		}
		
		packet.setCallbackUUID((UUID) PacketSerialUtils.read(buffer));
		packet.read(buffer);
		return new AbstractPacket(packet);
	}
	
}