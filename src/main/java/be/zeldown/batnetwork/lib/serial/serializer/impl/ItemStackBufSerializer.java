package be.zeldown.batnetwork.lib.serial.serializer.impl;

import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ItemStackBufSerializer implements IBufSerializer<ItemStack> {

	@Override
	public void write(PacketBuffer buf, Object value) {
		PacketSerialUtils.writeItemStack(buf, (ItemStack) value);
	}

	@Override
	public ItemStack read(PacketBuffer buf, Class<?>... clazz) {
		return PacketSerialUtils.readItemStack(buf);
	}

}