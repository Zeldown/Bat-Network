package be.zeldown.batnetwork.lib.serial;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import be.zeldown.batnetwork.lib.serial.serializer.ByteBufSerializerManager;
import be.zeldown.batnetwork.lib.serial.serializer.ByteBufSerializerType;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class PacketSerialUtils {
	
	private static final Gson GSON = new GsonBuilder().create();

	/* Write */
	public static void writeBoolean(PacketBuffer buf, boolean value) {
		buf.writeBoolean(value);
	}

	public static void writeByte(PacketBuffer buf, byte value) {
		buf.writeByte(value);
	}

	public static void writeChar(PacketBuffer buf, char value) {
		buf.writeChar(value);
	}

	public static void writeDouble(PacketBuffer buf, double value) {
		buf.writeDouble(value);
	}

	public static void writeFloat(PacketBuffer buf, float value) {
		buf.writeFloat(value);
	}

	public static void writeLong(PacketBuffer buf, long value) {
		buf.writeLong(value);
	}

	public static void writeInt(PacketBuffer buf, int value) {
		buf.writeInt(value);
	}

	public static void writeString(PacketBuffer buf, String value) {
        buf.writeUtf(value);
	}

	public static void writeNbt(PacketBuffer buf, CompoundNBT nbt) {
		buf.writeNbt(nbt);
	}

	public static void writeItemStack(PacketBuffer buf, ItemStack item) {
		buf.writeItem(item);
	}

	public static void writeObject(PacketBuffer buf, Object object) {
		PacketSerialUtils.writeString(buf, GSON.toJson(object));
	}
	
	public static void writeEnum(PacketBuffer buf, Enum<?> element) {
		buf.writeInt(element.ordinal());
	}
	
	public static void writeList(PacketBuffer buf, Collection<?> collection) {
		PacketSerialUtils.writeInt(buf, collection.size());
		for(Object object : collection) {
			PacketSerialUtils.write(buf, object);
		}
	}
	
	public static void writeSet(PacketBuffer buf, Set<?> set) {
		PacketSerialUtils.writeInt(buf, set.size());
		for(Object object : set) {
			PacketSerialUtils.write(buf, object);
		}
	}
	
	public static void writeMap(PacketBuffer buf, Map<?, ?> map) {
		PacketSerialUtils.writeInt(buf, map.size());
		for(Entry<?, ?> entry : map.entrySet()) {
			PacketSerialUtils.write(buf, entry.getKey());
			PacketSerialUtils.write(buf, entry.getValue());
		}
	}
	
	public static void write(PacketBuffer buf, Object object) {		
		if(object == null) {
			PacketSerialUtils.writeEnum(buf, ByteBufSerializerType.NULL);
			return;
		}
		
		final IBufSerializer<?> serializer = ByteBufSerializerManager.getSerializer(object.getClass());
		if(serializer != null) {
			PacketSerialUtils.writeEnum(buf, ByteBufSerializerType.CUSTOM);
			PacketSerialUtils.writeInt(buf, ByteBufSerializerManager.getSerializerId(serializer));
			serializer.write(buf, object);
			return;
		}

		if(object instanceof Collection<?>) {
			System.err.println("[BatNetwork][Network] You use an unmapped type of Collection.");
			System.err.println("[BatNetwork][Network] " + object + " is now serialized as JsonObject.");
		}

		PacketSerialUtils.writeEnum(buf, ByteBufSerializerType.OBJECT);
		PacketSerialUtils.writeObject(buf, object);
	}
	
	/* Read */
	public static boolean readBoolean(PacketBuffer buf) {
		return buf.readBoolean();
	}

	public static byte readByte(PacketBuffer buf) {
		return buf.readByte();
	}

	public static char readChar(PacketBuffer buf) {
		return buf.readChar();
	}

	public static double readDouble(PacketBuffer buf) {
		return buf.readDouble();
	}

	public static float readFloat(PacketBuffer buf) {
		return buf.readFloat();
	}

	public static long readLong(PacketBuffer buf) {
		return buf.readLong();
	}

	public static int readInt(PacketBuffer buf) {
		return buf.readInt();
	}

	public static String readString(PacketBuffer buf) {
		return buf.readUtf();
	}

	public static CompoundNBT readNbt(PacketBuffer buf) {
		return buf.readNbt();
	}

	public static ItemStack readItemStack(PacketBuffer buf) {
		return buf.readItem();
	}

	public static <T> T readObject(PacketBuffer buf, Type... type) {
		String value = PacketSerialUtils.readString(buf);
		return GSON.fromJson(value, type[0]);
	}
	
	public static <T> T readEnum(PacketBuffer buf, Class<T> type) {
		return type.getEnumConstants()[buf.readInt()];
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> readList(PacketBuffer buf, Class<T> type) {
		List<T> list = new ArrayList<>();
		int size = PacketSerialUtils.readInt(buf);
		for(int i=0;i<size;i++) {
			list.add((T) PacketSerialUtils.read(buf, type));
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Set<T> readSet(PacketBuffer buf, Class<T> type) {
		Set<T> set = new HashSet<>();
		int size = PacketSerialUtils.readInt(buf);
		for(int i=0;i<size;i++) {
			set.add((T) PacketSerialUtils.read(buf, type));
		}
		return set;
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> readMap(PacketBuffer buf, Class<K> keyClass, Class<V> valueClass) {
		Map<K, V> map = new HashMap<>();
		int size = PacketSerialUtils.readInt(buf);
		for(int i=0;i<size;i++) {
			K key = (K) PacketSerialUtils.read(buf, keyClass);
			V value = (V) PacketSerialUtils.read(buf, valueClass);
			map.put(key, value);
		}
		return map;
	}
	
	public static Object read(PacketBuffer buf, Class<?>... clazz) {
		try {
			final ByteBufSerializerType serialType = PacketSerialUtils.readEnum(buf, ByteBufSerializerType.class);
			if(serialType == ByteBufSerializerType.NULL) {
				return null;
			}
			
			if(serialType == ByteBufSerializerType.OBJECT) {
				return PacketSerialUtils.readObject(buf, clazz);
			}
			
			if(serialType == ByteBufSerializerType.CUSTOM) {
				final int serialId = PacketSerialUtils.readInt(buf);
				final IBufSerializer<?> serializer = ByteBufSerializerManager.getSerializer(serialId);
				if(serializer != null) {
					return serializer.read(buf, clazz);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}