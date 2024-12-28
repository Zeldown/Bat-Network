package be.zeldown.batnetwork.lib.utils;

import java.lang.reflect.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PacketDataField implements Comparable<PacketDataField> {

	private Field field;
	private PacketData data;
	
	@Override
	public int compareTo(PacketDataField o) {
		return o.field.getName().compareTo(this.field.getName());
	}
	
}