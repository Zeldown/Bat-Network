package be.zeldown.batnetwork.lib;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import be.zeldown.batnetwork.internal.mod.BatNetworkMod;
import be.zeldown.batnetwork.internal.mod.network.BatNetworkNetwork;
import be.zeldown.batnetwork.internal.mod.network.impl.AbstractPacket;
import be.zeldown.batnetwork.internal.mod.network.impl.callback.AbstractCallbackPacket;
import be.zeldown.batnetwork.lib.serial.PacketSerialUtils;
import be.zeldown.batnetwork.lib.serial.serializer.IBufSerializable;
import be.zeldown.batnetwork.lib.utils.PacketData;
import be.zeldown.batnetwork.lib.utils.PacketDataField;
import be.zeldown.batnetwork.lib.utils.PacketSide;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.PacketDistributor;

@Getter
@Setter
public class Packet implements IBufSerializable {
	
	/* [ Packet ] */
	private static final Map<Class<? extends Packet>, PacketDataField[]> CACHED_FIELDS = new HashMap<>();
	private static final Map<UUID, Consumer<Object>> CALLBACK_MAP = new HashMap<>();
	
	private UUID callbackUUID;
	
	private PacketBuffer contextBuffer;
	private Dist         effectiveSide;
	private String       effectivePlayer;
	
	private Map<Field, Object> cachedFieldValues;
	
	public Packet() {
		this.cachedFieldValues = new HashMap<>();
	}
	
	public void processServer(ServerPlayerEntity player) {};
	public void processClient() {};
	
	@Override
	public void write(PacketBuffer buf) {
		this.effectiveSide = FMLEnvironment.dist;
		this.contextBuffer = buf;
		
		buf.writeUtf(Packet.getPacketName(this.getClass()));
		PacketSerialUtils.write(buf, this.callbackUUID);
		
		final Class<? extends Packet> clazz = this.getClass();
		if (!CACHED_FIELDS.containsKey(clazz)) {
			List<PacketDataField> fields = new ArrayList<>();
			for (Field field : clazz.getDeclaredFields()) {
				if (!field.isAnnotationPresent(PacketData.class)) {
					continue;
				}
				
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				
				fields.add(new PacketDataField(field, field.getAnnotation(PacketData.class)));
			}
			Collections.sort(fields);
			CACHED_FIELDS.put(clazz, fields.toArray(new PacketDataField[0]));
		}
		
		try {
			for (PacketDataField field : CACHED_FIELDS.get(clazz)) {
				if (field.getData().value() != PacketSide.BOTH && this.effectiveSide != field.getData().value().getSide()) {
					continue;
				}
				
				Object value = null;
				if (this.cachedFieldValues.containsKey(field.getField())) {
					value = this.cachedFieldValues.get(field.getField());
				} else {
					value = field.getField().get(this);
					this.cachedFieldValues.put(field.getField(), value);
				}
				
				PacketSerialUtils.write(buf, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void read(PacketBuffer buf) {
		this.effectiveSide = FMLEnvironment.dist;
		this.contextBuffer = buf;
		
		final Class<? extends Packet> clazz = this.getClass();
		if (!CACHED_FIELDS.containsKey(clazz)) {
			List<PacketDataField> fields = new ArrayList<>();
			for (Field field : clazz.getDeclaredFields()) {
				if (!field.isAnnotationPresent(PacketData.class)) {
					continue;
				}
				
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				
				fields.add(new PacketDataField(field, field.getAnnotation(PacketData.class)));
			}
			Collections.sort(fields);
			CACHED_FIELDS.put(clazz, fields.toArray(new PacketDataField[0]));
		}
		
		try {
			for (PacketDataField field : CACHED_FIELDS.get(clazz)) {
				if (field.getData().value() != PacketSide.BOTH && this.effectiveSide == field.getData().value().getSide()) {
					continue;
				}
				
				Class<?>[] typeArray = null;
				
				final Type genericType = field.getField().getGenericType();
				if (genericType instanceof ParameterizedType) {
					ParameterizedType parameterizedType = (ParameterizedType) genericType;
					Type[] parameterizedTypeArguments = parameterizedType.getActualTypeArguments();
					typeArray = new Class<?>[parameterizedTypeArguments.length];
					for (int i=0;i<parameterizedTypeArguments.length;i++) {
						typeArray[i] = (Class<?>) parameterizedTypeArguments[i];
					}
				} else {
					typeArray = new Class<?>[] {field.getField().getType()};
				}
				
				Object value = PacketSerialUtils.read(buf, typeArray);
				field.getField().set(this, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> Packet subscribe(Consumer<T> callback) {
		CALLBACK_MAP.put(this.callbackUUID = UUID.randomUUID(), (Consumer<Object>) callback);
		return this;
	}
	
	public void reply(Object reply) {
		if (reply instanceof Packet) {
			final Packet packet = (Packet) reply;
			packet.setCallbackUUID(this.callbackUUID);
			if (this.effectiveSide == Dist.CLIENT) {
				packet.send();
			} else if (this.effectiveSide == Dist.DEDICATED_SERVER) {
				final ServerPlayerEntity player = this.getEffectivePlayer();
				if (player == null) {
					return;
				}
				
				packet.send(player);
			}
			
			return;
		}

		if (this.callbackUUID == null) {
			return;
		}
		
		final AbstractCallbackPacket packet = new AbstractCallbackPacket(this.callbackUUID, reply);
		if (this.effectiveSide == Dist.CLIENT) {
			BatNetworkNetwork.inst().sendToServer(packet);
		} else if (this.effectiveSide == Dist.DEDICATED_SERVER) {
			final ServerPlayerEntity player = this.getEffectivePlayer();
			if (player == null) {
				return;
			}
			
			BatNetworkNetwork.inst().send(PacketDistributor.PLAYER.with(() -> player), packet);
		}
	}
	
	public void send(ServerPlayerEntity player) {
		if (!Packet.packetByClass.containsKey(this.getClass())) {
			throw new RuntimeException("Packet not registered: " + this.getClass().getName());
		}
		
		BatNetworkNetwork.inst().send(PacketDistributor.PLAYER.with(() -> player), new AbstractPacket(this));
	}
	
	public void send() {
		if (!Packet.packetByClass.containsKey(this.getClass())) {
			throw new RuntimeException("Packet not registered: " + this.getClass().getName());
		}
		
		BatNetworkNetwork.inst().sendToServer(new AbstractPacket(this));
	}
	
	public Consumer<Object> pollCallback() {
		return Packet.pollCallback(this.callbackUUID);
	}
	
	@OnlyIn(Dist.DEDICATED_SERVER)
	public ServerPlayerEntity getEffectivePlayer() {
		if (this.effectivePlayer == null) {
			return null;
		}
		
		return (ServerPlayerEntity) BatNetworkMod.getServer().getPlayerList().getPlayer(UUID.fromString(this.effectivePlayer));
	}
	
	public void setSide(Dist effectiveSide) {
		this.effectiveSide = effectiveSide;
	}
	
	public void setEffectivePlayer(ServerPlayerEntity player) {
		this.effectivePlayer = player.getStringUUID();
	}
	
	public static Consumer<Object> pollCallback(UUID uuid) {
		if(uuid == null) {
			return null;
		}
		
		return CALLBACK_MAP.remove(uuid);
	}

	public boolean isRemote() {
		return this.effectiveSide.isClient();
	}
	
	/* [ Registry ] */
	private static Map<String, Class<? extends Packet>> packetByName;
	private static Map<Class<? extends Packet>, String> packetByClass;
	
	static {
		packetByName  = new HashMap<>();
		packetByClass = new HashMap<>();
	}
	
	public static void registerPacket(Class<? extends Packet> packet) {		
		final String name = packet.getName();
		packetByName.put(name, packet);
		packetByClass.put(packet, name);
	}
	
	public static Packet getPacket(String name) {
		try {
			return packetByName.get(name).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getPacketName(Class<? extends Packet> clazz) {
		return packetByClass.get(clazz);
	}

}