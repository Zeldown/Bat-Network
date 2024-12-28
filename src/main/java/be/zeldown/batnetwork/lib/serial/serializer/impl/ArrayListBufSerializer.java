package be.zeldown.batnetwork.lib.serial.serializer.impl;

import java.util.ArrayList;
import java.util.Collection;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class ArrayListBufSerializer implements IBufSerializer<ArrayList<?>> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		ArrayList<?> list = (ArrayList<?>) value;
		PacketSerialUtils.writeList(buf, list);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ArrayList<?> read(PacketBuffer buf, Class<?>... clazz) {
		final Collection list = PacketSerialUtils.readList(buf, clazz[0]);
		return new ArrayList<>(list);
	}

}