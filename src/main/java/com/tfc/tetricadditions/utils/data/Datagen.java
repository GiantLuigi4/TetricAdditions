package com.tfc.tetricadditions.utils.data;

import com.tfc.tetricadditions.utils.ArrayHelper;

import java.io.File;
import java.io.FileOutputStream;

public class Datagen {
//	private static final String[][] types = new String[][] {
//			//Helmet stuff
//			{
//				"armor/chestplate/base",
//					"1", "0", "false", "false",
//					"99","35","1",
//					"minecraft:diamond",
//					"minecraft:iron_ingot",
//					"minecraft:gold_ingot",
//					"minecraft:leather",
//					"minecraft:rabbit_hide",
//					"minecraft:cactus",
//					"minecraft:prismarine_shard",
//			},
//			{
//				"armor/chestplate/plate1",
//					"0.5", "0.5", "true", "true",
//					"99","35","1",
//					"minecraft:diamond",
//					"minecraft:iron_ingot",
//					"minecraft:gold_ingot",
//					"minecraft:leather",
//					"minecraft:rabbit_hide",
//					"minecraft:cactus",
//					"minecraft:prismarine_shard",
//			},
//			{
//				"armor/chestplate/plate2",
//					"0.2", "0.2", "true", "false",
//					"99","35","1",
//					"minecraft:diamond",
//					"minecraft:iron_ingot",
//					"minecraft:gold_ingot",
//					"minecraft:leather",
//					"minecraft:rabbit_hide",
//					"minecraft:cactus",
//					"minecraft:prismarine_shard",
//			},
//			{
//				"armor/chestplate/binding",
//					"0.18", "0.1", "true", "false",
//					"88","16","1",
//					"minecraft:leather",
//					"minecraft:rabbit_hide",
//					"minecraft:string",
//					"minecraft:vine",
//					"tetra:forged_bolt",
//			},
//	};
	
	private static final String[][] types = ArrayHelper.massMerge(
			genList("helmet", 0),
			genList("chestplate", 1),
			genList("leggings", 2),
			genList("boots", 3)
	);
	
	private static final MaterialTier[] tiers = new MaterialTier[]{
			new MaterialTier("minecraft:diamond", 8, 10, 528, true, "diamond", "%type%", "diamond", "hammer", 3),
			new MaterialTier("minecraft:gold_ingot", 5, 3, 112, false, "gold", "%type%", "gold", "hammer", 1),
			new MaterialTier("minecraft:iron_ingot", 6, 5, 240, false, "iron", "%type%", "iron", "hammer", 2),
			new MaterialTier("minecraft:leather", 3, 4, 80, false, "leather", "%type%", "leather", "cut", 2),
			new MaterialTier("minecraft:cactus", 2, 2, 40, false, "cactus", "special/cactus_%type%", "vine", "cut", 2, false, null),
			new MaterialTier("minecraft:rabbit_hide", 4, 3, 60, false, "hide", "%type%", "hide", "cut", 2),
			new MaterialTier("minecraft:prismarine_shard", 7, 5, 360, false, "prismarine", "special/prismarine_%type%", "prismarine", "hammer", 2, false, null),
			new MaterialTier("minecraft:string", 1, 1, 10, false, "string", "%type%", "string", "cutting", 1),
			new MaterialTier("minecraft:vine", 2, 2, 35, false, "vine", "%type%", "vine", "cutting", 2),
			new MaterialTier("minecraft:phantom_membrane", 1, 2, 16, false, "phantom", "special/phantom_membrane_%type%", "special", "cutting", 2, false, null),
			new MaterialTier("tetra:forged_bolt", 4, 5, 623, true, "bolt", "special/bolt_%type%", "bolt", "hammer", 4, false, null),
	};
	
	public static void main(String[] args) {
		for (String[] type : types) {
			String typeStr = type[0];
			int startIndex = 8;
			
			int armorPart = Integer.parseInt(type[7]);
			
			//Datagen Modules
			String module = "{\n" +
					"  \"replace\": false,\n" +
					"  \"slots\": [\"" + typeStr + "\"],\n" +
					"  \"type\": \"tetra:basic_module\",\n" +
					"  \"renderLayer\": \"highest\",\n" +
					"  \"tweakKey\": \"tetra:" + typeStr + "\",\n" +
					"  \"variants\": [";
			for (int material = startIndex; material < type.length; material++) {
				module += "\n" + getTier(type[material])
						.toStringModule(
								Double.parseDouble(type[1]),
								Double.parseDouble(type[2]),
								Boolean.parseBoolean(type[3]),
								Boolean.parseBoolean(type[4]),
								typeStr,
								armorPart
						);
				if (material != type.length - 1)
					module += ",";
			}
			module += "\n  ]\n}";
			writeFile("src/main/resources/data/tetra/modules/" + type[0] + ".json", module);
			
			//Datagen Schemas
			String schema = "{\n" +
					"  \"replace\": false,\n" +
					"  \"slots\": [\n" +
					"    \"" + typeStr + "\"\n" +
					"  ],\n" +
					"  \"materialSlotCount\": 1,\n" +
					"  \"displayType\": \"major\",\n" +
					"  \"glyph\": {\n" +
					"    \"textureX\": " + type[5] + ",\n" +
					"    \"textureY\": " + type[6] + "\n" +
					"  },\n" +
					"  \"outcomes\": [";
			for (int material = startIndex; material < type.length; material++) {
				schema += "\n" + getTier(type[material]).toStringSchema(Double.parseDouble(type[1]), Double.parseDouble(type[2]), Boolean.parseBoolean(type[3]), typeStr, armorPart);
				if (material != type.length - 1)
					schema += ",";
			}
			schema += "\n  ]\n}";
//			System.out.println(schema);
			writeFile("src/main/resources/data/tetra/schemas/" + type[0] + ".json", schema);
		}
	}
	
	public static MaterialTier getTier(String name) {
		for (MaterialTier tier : tiers) {
			if (tier.name.equals(name)) {
				return tier;
			}
		}
		return null;
	}
	
	public static void writeFile(String path, String text) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			FileOutputStream stream = new FileOutputStream(f);
			stream.write(text.getBytes());
			stream.close();
		} catch (Throwable ignored) {
		}
	}
	
	private static String[][] genList(String name, int part) {
		return new String[][]{
				{
						"armor/" + name + "/base",
						"1", "0", "false", "false",
						"99", "35", "" + part + "",
						"minecraft:diamond",
						"minecraft:iron_ingot",
						"minecraft:gold_ingot",
						"minecraft:leather",
						"minecraft:rabbit_hide",
						"minecraft:cactus",
						"minecraft:prismarine_shard",
				},
				{
						"armor/" + name + "/plate1",
						"0.5", "0.5", "true", "true",
						"99", "35", "" + part + "",
						"minecraft:diamond",
						"minecraft:iron_ingot",
						"minecraft:gold_ingot",
						"minecraft:leather",
						"minecraft:rabbit_hide",
						"minecraft:cactus",
						"minecraft:prismarine_shard",
				},
				{
						"armor/" + name + "/plate2",
						"0.2", "0.2", "true", "false",
						"99", "35", "" + part + "",
						"minecraft:diamond",
						"minecraft:iron_ingot",
						"minecraft:gold_ingot",
						"minecraft:leather",
						"minecraft:rabbit_hide",
						"minecraft:cactus",
						"minecraft:prismarine_shard",
				},
				{
						"armor/" + name + "/binding",
						"0.18", "0.1", "true", "false",
						"88", "16", "" + part + "",
						"minecraft:leather",
						"minecraft:rabbit_hide",
						"minecraft:string",
						"minecraft:vine",
						"minecraft:phantom_membrane",
						"tetra:forged_bolt",
				},
		};
	}
}
