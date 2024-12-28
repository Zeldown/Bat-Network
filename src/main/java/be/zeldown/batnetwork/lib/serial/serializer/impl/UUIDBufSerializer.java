package be.zeldown.batnetwork.lib.serial.serializer.impl;

import java.util.UUID;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class UUIDBufSerializer implements IBufSerializer<UUID> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		PacketSerialUtils.writeString(buf, ((UUID) value).toString());
	}

	@Override
	public UUID read(PacketBuffer buf, Class<?>... clazz) {
		return UUID.fromString(PacketSerialUtils.readString(buf));
	}

}