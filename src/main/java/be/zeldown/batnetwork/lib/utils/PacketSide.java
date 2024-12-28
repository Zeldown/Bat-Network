package be.zeldown.batnetwork.lib.utils;

import net.minecraftforge.api.distmarker.Dist;

public enum PacketSide {

	CLIENT(Dist.CLIENT),
	SERVER(Dist.DEDICATED_SERVER),
	BOTH(null);
	
	private final Dist side;
	
	private PacketSide(Dist side) {
		this.side = side;
	}
	
	public Dist getSide() {
		return side;
	}
	
}