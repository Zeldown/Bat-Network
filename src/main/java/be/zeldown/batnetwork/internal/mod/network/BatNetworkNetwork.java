package be.zeldown.batnetwork.internal.mod.network;

import be.zeldown.batnetwork.internal.mod.Constants;
import be.zeldown.batnetwork.internal.mod.network.impl.AbstractPacket;
import be.zeldown.batnetwork.internal.mod.network.impl.AbstractPacketHandler;
import be.zeldown.batnetwork.internal.mod.network.impl.callback.AbstractCallbackPacket;
import be.zeldown.batnetwork.internal.mod.network.impl.callback.AbstractCallbackPacketHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class BatNetworkNetwork {
	
	private static final String PROTOCOL_VERSION = "1";

	private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		new ResourceLocation(Constants.MOD_ID, "main"),
		() -> BatNetworkNetwork.PROTOCOL_VERSION,
		BatNetworkNetwork.PROTOCOL_VERSION::equals,
		BatNetworkNetwork.PROTOCOL_VERSION::equals
	);
	
	public static void init() {
		BatNetworkNetwork.INSTANCE.registerMessage(0, AbstractPacket.class, AbstractPacket.encode, AbstractPacket::decode, AbstractPacketHandler::handle);
		BatNetworkNetwork.INSTANCE.registerMessage(1, AbstractCallbackPacket.class, AbstractCallbackPacket.encode, AbstractCallbackPacket::decode, AbstractCallbackPacketHandler::handle);
	}
	
	public static SimpleChannel inst() {
		return BatNetworkNetwork.INSTANCE;
	}

}