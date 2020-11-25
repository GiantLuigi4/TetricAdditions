package com.tfc.tetricadditions.data;

import java.util.Arrays;

public class ArrayHelper {
	public static <T> T[] merge(T[] base, T[] toAppend) {
		T[] newArray = Arrays.copyOf(base, base.length + toAppend.length);
		for (int i = 0; i < toAppend.length; i++)
			newArray[i + base.length] = toAppend[i];
		return newArray;
	}
	
	public static <T> T[] massMerge(T[] base, T[]... toAppend) {
		T[] out = base;
		for (T[] array : toAppend) out = merge(out, array);
		return out;
	}
}
