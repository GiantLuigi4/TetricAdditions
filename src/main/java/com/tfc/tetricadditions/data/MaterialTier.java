package com.tfc.tetricadditions.data;

public class MaterialTier {
	private final double[] multipliers = new double[] {
			0.375,1,0.75,0.375
	};
	
	public final String name;
	public final int chestplateValue;
	public final int chestplateIntegrity;
	public final int chestplateDurability;
	public final boolean baseProvidesToughness;
	public final String key;
	public final String textureBase;
	public final String tint;
	public final String tool;
	public final int toolTier;
	
	public MaterialTier(String name, int chestplateValue, int chestplateIntegrity, int chestplateDurability, boolean baseProvidesToughness, String key, String textureBase, String tint,String tool, int toolTier) {
		this.name = name;
		this.chestplateValue = chestplateValue;
		this.chestplateIntegrity = chestplateIntegrity;
		this.chestplateDurability = chestplateDurability;
		this.baseProvidesToughness = baseProvidesToughness;
		this.key = key;
		this.textureBase = textureBase;
		this.tint = tint;
		this.tool = tool;
		this.toolTier = toolTier;
	}
	
	public String toStringHelmetModule(float scalar, int integrityLose, boolean loseIntegrity, String type) {
		return
				"    {\n" +
				"      \"key\": \""+type+"/"+key+"\",\n" +
				"      \"durability\": "+(chestplateDurability*((multipliers[0]+1)/2)*scalar)+",\n" +
				"      \"integrity\": "+Math.ceil(chestplateIntegrity *multipliers[0]*scalar)+",\n" +
				"      \"damage\": "+(chestplateValue*multipliers[0]*scalar)+",\n" +
				"      \"attackSpeed\": "+(baseProvidesToughness?2:0)+",\n" +
				"      \"glyph\": {\n" +
				"        \"tint\": \""+tint+"_glyph\",\n" +
				"        \"textureX\": 88,\n" +
				"        \"textureY\": 16\n" +
				"      },\n" +
				"      \"models\": [{\n" +
				"        \"location\": \"tetric_additions:items/module/armor/helmet/"+textureBase+"\",\n" +
				"        \"tint\": \""+tint+"\"\n" +
				"      }]\n" +
				"    }";
	}
	
	public String toStringHelmetSchema(float scalar, int integrityLose, boolean loseIntegrity, String type) {
		return
				"    {\n" +
				"      \"material\": {\n" +
				"        \"item\": \""+name+"\",\n" +
				"        \"count\": "+Math.floor(5*scalar)+"\n" +
				"      },\n" +
				"      \"requiredCapabilities\": {\n" +
				"        \""+tool+"\": "+toolTier+"\n" +
				"      },\n" +
				"      \"moduleKey\": \"armor/helmet/"+type+"\",\n" +
				"      \"moduleVariant\": \"armor/helmet/"+type+"/"+key+"\"\n" +
				"    }";
	}
}
