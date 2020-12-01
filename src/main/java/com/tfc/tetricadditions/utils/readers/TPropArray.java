package com.tfc.tetricadditions.utils.readers;

public class TPropArray extends TPropObject {
	private int index = 0;
	
	public TPropArray(TPropObject parent, String key) {
		super(parent, key);
	}
	
	public TPropArray() {
	}
	
	public void add(TPropObject val) {
		set((index++) + "", val);
	}
	
	public TPropObject get(int index) {
		return get("" + index);
	}
	
	public int getSize() {
		return index;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		if (this.getKey() != null) builder.append(this.getKey()).append(" are\n");
		
		for (TPropObject object : getObjects().values()) {
			for (int i = 0; i < getParentCount(); i++) builder.append(" ");
			builder.append(object.toString()).append("\n");
		}
		
		return builder.toString();
	}
	
	public TPropObject[] toArray() {
		return getObjects().values().toArray(new TPropObject[0]);
	}
}
