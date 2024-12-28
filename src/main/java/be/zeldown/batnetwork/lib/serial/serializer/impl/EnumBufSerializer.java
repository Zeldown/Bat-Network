package be.zeldown.batnetwork.lib.serial.serializer.impl;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class EnumBufSerializer implements IBufSerializer<Enum<?>> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		PacketSerialUtils.writeEnum(buf, (Enum<?>) value);
	}

	@Override
	public Enum<?> read(PacketBuffer buf, Class<?>... clazz) {
		return (Enum<?>) PacketSerialUtils.readEnum(buf, clazz[0]);
	}

}