package be.zeldown.batnetwork.lib.serial.serializer.impl;

import java.util.HashSet;
import java.util.Set;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class HashSetBufSerializer implements IBufSerializer<HashSet<?>> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		HashSet<?> set = (HashSet<?>) value;
		PacketSerialUtils.writeSet(buf, set);
	}

	@Override
	public HashSet<?> read(PacketBuffer buf, Class<?>... clazz) {
		final Set<?> list = PacketSerialUtils.readSet(buf, clazz[0]);
		return new HashSet<>(list);
	}

}