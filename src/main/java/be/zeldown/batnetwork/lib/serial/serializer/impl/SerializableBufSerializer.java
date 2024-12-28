package be.zeldown.batnetwork.lib.serial.serializer.impl;

import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializable;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class SerializableBufSerializer implements IBufSerializer<IBufSerializable> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		IBufSerializable serializable = (IBufSerializable) value;
		serializable.write(buf);
	}

	@Override
	public IBufSerializable read(PacketBuffer buf, Class<?>... clazz) {
		try {
			final IBufSerializable serializable = (IBufSerializable) clazz[0].newInstance();
			serializable.read(buf);
			return serializable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}