package be.zeldown.batnetwork.lib.serial.serializer.impl;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class LongBufSerializer implements IBufSerializer<Long> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		PacketSerialUtils.writeLong(buf, (long) value);
	}

	@Override
	public Long read(PacketBuffer buf, Class<?>... clazz) {
		return PacketSerialUtils.readLong(buf);
	}

}