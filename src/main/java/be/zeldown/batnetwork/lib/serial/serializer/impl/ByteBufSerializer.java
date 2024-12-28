package be.zeldown.batnetwork.lib.serial.serializer.impl;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class ByteBufSerializer implements IBufSerializer<Byte> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		PacketSerialUtils.writeByte(buf, (byte) value);
	}

	@Override
	public Byte read(PacketBuffer buf, Class<?>... clazz) {
		return PacketSerialUtils.readByte(buf);
	}

}