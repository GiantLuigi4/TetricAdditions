package com.tfc.tetricadditions.utils;

import java.util.Arrays;

public class ArrayHelper {
	public static <T> T[] merge(T[] base, T[] toAppend) {
		T[] newArray = Arrays.copyOf(base, base.length + toAppend.length);
		System.arraycopy(toAppend, 0, newArray, base.length, toAppend.length);
		return newArray;
	}
	
	public static <T> T[] merge(T[] base, T toAppend) {
		T[] newArray = Arrays.copyOf(base, base.length + 1);
		newArray[newArray.length - 1] = toAppend;
		return newArray;
	}
	
	public static <T> T[] massMerge(T[] base, T[]... toAppend) {
		T[] out = base;
		for (T[] array : toAppend) out = merge(out, array);
		return out;
	}
	
	public static <T> T[] massMerge(T[] base, T... toAppend) {
		return merge(base, toAppend);
	}
}
