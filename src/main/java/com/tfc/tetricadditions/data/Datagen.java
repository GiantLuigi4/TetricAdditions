package com.tfc.tetricadditions.data;

public class Datagen {
	private static final String[][] types = new String[][] {
			{
				"armor/helmet/base",
					"1", "0", "false",
					"minecraft:diamond",
					"minecraft:iron_ingot",
					"minecraft:gold_ingot",
					"minecraft:leather",
					"minecraft:rabbit_hide",
					"minecraft:cactus",
					"minecraft:prismarine_shard",
			}
	};
	
	private static final MaterialTier[] tiers = new MaterialTier[] {
			new MaterialTier("minecraft:diamond",8,10,528,true,"diamond","base","diamond","hammer",3),
			new MaterialTier("minecraft:gold_ingot",5,3,112,false,"gold","base","gold","hammer",1),
			new MaterialTier("minecraft:iron_ingot",6,5,240,false,"iron","base","iron","hammer",2),
			new MaterialTier("minecraft:leather",3,4,80,false,"leather","base","leather","cut",2),
			new MaterialTier("minecraft:cactus",2,2,40,false,"cactus","special/normal_","string","cut",3),
			new MaterialTier("minecraft:rabbit_hide",4,3,60,false,"hide","base","hide","cut",2),
			new MaterialTier("minecraft:prismarine_shard",7,5,360,false,"prismarine","special/normal_","string","hammer",2),
	};
	
	public static void main(String[] args) {
		for (String[] type : types) {
			//Datagen Modules
			String module = "{\n" +
					"  \"replace\": false,\n" +
					"  \"slots\": [\"armor/helmet/base\"],\n" +
					"  \"type\": \"tetra:basic_module\",\n" +
					"  \"renderLayer\": \"highest\",\n" +
					"  \"tweakKey\": \"tetra:armor/helmet/base\",\n" +
					"  \"variants\": [";
			String typeStr = type[0];
			int startIndex = 4;
			for (int material=startIndex;material<type.length;material++) {
				module+="\n"+getTier(type[material]).toStringHelmetModule(Integer.parseInt(type[1]),Integer.parseInt(type[2]),Boolean.parseBoolean(type[3]),typeStr);
				if (material != type.length-1)
					module+=",";
			}
			module+="\n  ]\n}";
			System.out.println(module.replace("tint\": \"cactus","tint\": \"vine"));
			
			
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
}
