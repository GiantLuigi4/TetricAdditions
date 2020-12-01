package com.tfc.tetricadditions.utils.readers;

import java.util.Collection;
import java.util.HashMap;

public class TPropObject {
	private final HashMap<String, TPropObject> objects = new HashMap<>();
	
	private final TPropObject parent;
	private final String key;
	private final int parentCount;
	
	public TPropObject(TPropObject parent, String key) {
		this.parent = parent;
		this.key = key;
		this.parentCount = parent.getParentCount() + 1;
	}
	
	public TPropObject() {
		this.parent = null;
		key = null;
		this.parentCount = 0;
	}
	
	protected HashMap<String, TPropObject> getObjects() {
		return objects;
	}
	
	public boolean contains(String key) {
		return objects.containsKey(key);
	}
	
	public int getParentCount() {
		return parentCount;
	}
	
	public TPropObject get(String key) {
		return objects.get(key);
	}
	
	public TPropObject set(String key, TPropObject object) {
		if (objects.containsKey(key)) return objects.replace(key, object);
		else return objects.put(key, object);
	}
	
	public String getType() {
		return "object";
	}
	
	public TPropObject getParent() {
		return parent;
	}
	
	public String getKey() {
		return key;
	}
	
	public Collection<String> getKeys() {
		return objects.keySet();
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		if (key != null) builder.append(key);
		
		for (TPropObject object : objects.values()) {
			for (int i = 0; i < parentCount; i++) builder.append(" ");
			builder.append(object.toString()).append("\n");
		}
		
		return builder.toString();
	}
}
