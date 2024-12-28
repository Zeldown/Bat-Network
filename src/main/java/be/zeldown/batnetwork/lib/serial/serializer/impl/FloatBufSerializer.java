package be.zeldown.batnetwork.lib.serial.serializer.impl;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class FloatBufSerializer implements IBufSerializer<Float> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		PacketSerialUtils.writeFloat(buf, (float) value);
	}

	@Override
	public Float read(PacketBuffer buf, Class<?>... clazz) {
		return PacketSerialUtils.readFloat(buf);
	}

}