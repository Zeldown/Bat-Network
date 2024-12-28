package be.zeldown.batnetwork.internal.mod;

import be.zeldown.batnetwork.internal.mod.network.BatNetworkNetwork;
import lombok.Getter;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Getter
@Mod(Constants.MOD_ID)
public class BatNetworkMod {

	private static BatNetworkMod instance;
	
	@OnlyIn(Dist.DEDICATED_SERVER) private static net.minecraft.server.MinecraftServer server;

	public BatNetworkMod() {
		BatNetworkMod.instance = this;

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setupCommon(final FMLCommonSetupEvent event) {
		BatNetworkNetwork.init();
	}

	@SubscribeEvent
	@OnlyIn(Dist.DEDICATED_SERVER)
	public void setupServer(final FMLServerStartingEvent event) {
		BatNetworkMod.server = event.getServer();
	}

	public static BatNetworkMod getInstance() {
		return BatNetworkMod.instance;
	}
	
	public static MinecraftServer getServer() {
		return BatNetworkMod.server;
	}

}