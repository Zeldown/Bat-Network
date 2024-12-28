package be.zeldown.batnetwork.lib.serial.serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import be.zeldown.batnetwork.lib.serial.serializer.impl.ArrayListBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.BooleanBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.ByteBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.CharacterBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.DoubleBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.EnumBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.FloatBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.HashMapBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.HashSetBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.IntegerBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.ItemStackBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.LongBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.NBTTagCompoundBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.SerializableBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.StringBufSerializer;
import be.zeldown.batnetwork.lib.serial.serializer.impl.UUIDBufSerializer;

import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ByteBufSerializerManager {

	private static Map<Class<?>, IBufSerializer<?>> SERIALIZERS;
	private static Map<Integer, IBufSerializer<?>>  SERIALIZERS_ID;
	private static Map<IBufSerializer<?>, Integer>  SERIALIZERS_ID_REVERSE;
	
	static {
		SERIALIZERS            = new HashMap<>();
		SERIALIZERS_ID         = new HashMap<>();
		SERIALIZERS_ID_REVERSE = new HashMap<>();
		
		addSerializer(Boolean.class, new BooleanBufSerializer());
		addSerializer(Byte.class, new ByteBufSerializer());
		addSerializer(Character.class, new CharacterBufSerializer());
		addSerializer(Double.class, new DoubleBufSerializer());
		addSerializer(Float.class, new FloatBufSerializer());
		addSerializer(Long.class, new LongBufSerializer());
		addSerializer(Integer.class, new IntegerBufSerializer());
		addSerializer(String.class, new StringBufSerializer());
		addSerializer(UUID.class, new UUIDBufSerializer());
		addSerializer(CompoundNBT.class, new NBTTagCompoundBufSerializer());
		addSerializer(ItemStack.class, new ItemStackBufSerializer());
		addSerializer(ArrayList.class, new ArrayListBufSerializer());
		addSerializer(HashSet.class, new HashSetBufSerializer());
		addSerializer(HashMap.class, new HashMapBufSerializer());
		addSerializer(Enum.class, new EnumBufSerializer());
		addSerializer(IBufSerializable.class, new SerializableBufSerializer());
	}
	
	public static void addSerializer(Class<?> clazz, IBufSerializer<?> serializer) {
		final int id = SERIALIZERS_ID.size();
		SERIALIZERS.put(clazz, serializer);
		SERIALIZERS_ID.put(id, serializer);
		SERIALIZERS_ID_REVERSE.put(serializer, id);
	}
	
	public static IBufSerializer<?> getSerializer(Class<?> clazz) {
		IBufSerializer<?> serializer = SERIALIZERS.get(clazz);
		if(serializer != null) {
			return serializer;
		}
		
		for(Entry<Class<?>, IBufSerializer<?>> entry : SERIALIZERS.entrySet()) {
			if(entry.getKey().isAssignableFrom(clazz)) {
				return entry.getValue();
			}
		}
		
		return null;
	}
	
	public static IBufSerializer<?> getSerializer(int id) {
		return SERIALIZERS_ID.get(id);
	}
	
	public static int getSerializerId(IBufSerializer<?> serializer) {
		return SERIALIZERS_ID_REVERSE.get(serializer);
	}
	
}