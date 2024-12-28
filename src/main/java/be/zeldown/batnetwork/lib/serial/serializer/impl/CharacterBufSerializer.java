package be.zeldown.batnetwork.lib.serial.serializer.impl;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.network.PacketBuffer;

public class CharacterBufSerializer implements IBufSerializer<Character> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		PacketSerialUtils.writeChar(buf, (char) value);
	}

	@Override
	public Character read(PacketBuffer buf, Class<?>... clazz) {
		return PacketSerialUtils.readChar(buf);
	}

}