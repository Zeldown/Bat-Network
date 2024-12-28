package be.zeldown.batnetwork.lib.serial.serializer.impl;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class IntegerBufSerializer implements IBufSerializer<Integer> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		PacketSerialUtils.writeInt(buf, (int) value);
	}

	@Override
	public Integer read(PacketBuffer buf, Class<?>... clazz) {
		return PacketSerialUtils.readInt(buf);
	}

}