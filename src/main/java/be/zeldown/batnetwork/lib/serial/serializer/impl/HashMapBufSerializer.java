package be.zeldown.batnetwork.lib.serial.serializer.impl;

import java.util.HashMap;
import java.util.Map;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class HashMapBufSerializer implements IBufSerializer<HashMap<?, ?>> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		HashMap<?, ?> map = (HashMap<?, ?>) value;
		PacketSerialUtils.writeMap(buf, map);
	}

	@Override
	public HashMap<?, ?> read(PacketBuffer buf, Class<?>... clazz) {
		final Map<?, ?> map = PacketSerialUtils.readMap(buf, clazz[0], clazz[1]);
		return new HashMap<>(map);
	}

}