package be.zeldown.batnetwork.lib.serial.serializer.impl;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class StringBufSerializer implements IBufSerializer<String> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		PacketSerialUtils.writeString(buf, (String) value);
	}

	@Override
	public String read(PacketBuffer buf, Class<?>... clazz) {
		return PacketSerialUtils.readString(buf);
	}

}