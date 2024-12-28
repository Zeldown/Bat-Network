package be.zeldown.batnetwork.lib.serial.serializer.impl;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class NBTTagCompoundBufSerializer implements IBufSerializer<CompoundNBT> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		PacketSerialUtils.writeNbt(buf, (CompoundNBT) value);
	}

	@Override
	public CompoundNBT read(PacketBuffer buf, Class<?>... clazz) {
		return PacketSerialUtils.readNbt(buf);
	}	

}