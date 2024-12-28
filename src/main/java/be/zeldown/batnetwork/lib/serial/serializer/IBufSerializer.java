package be.zeldown.batnetwork.lib.serial.serializer;

import net.minecraft.network.PacketBuffer;

public interface IBufSerializer<T> {

	abstract void write(PacketBuffer buf, Object value);
	abstract T read(PacketBuffer buf, Class<?>... clazz);
	
}