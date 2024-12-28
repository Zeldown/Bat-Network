package be.zeldown.batnetwork.lib.serial.serializer.impl;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class BooleanBufSerializer implements IBufSerializer<Boolean> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		PacketSerialUtils.writeBoolean(buf, (boolean) value);
	}

	@Override
	public Boolean read(PacketBuffer buf, Class<?>... clazz) {
		return PacketSerialUtils.readBoolean(buf);
	}

}