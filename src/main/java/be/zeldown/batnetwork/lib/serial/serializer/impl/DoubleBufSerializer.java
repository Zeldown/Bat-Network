package be.zeldown.batnetwork.lib.serial.serializer.impl;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class DoubleBufSerializer implements IBufSerializer<Double> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		PacketSerialUtils.writeDouble(buf, (double) value);
	}

	@Override
	public Double read(PacketBuffer buf, Class<?>... clazz) {
		return PacketSerialUtils.readDouble(buf);
	}

}