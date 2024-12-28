package be.zeldown.batnetwork.lib.serial.serializer;

import net.minecraft.network.PacketBuffer;

public interface IBufSerializable {

	abstract void write(PacketBuffer buf);
	abstract void read(PacketBuffer buf);
	
}