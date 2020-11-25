package com.tfc.tetricadditions.data;

public class MaterialTier {
	private final double[] multipliers = new double[]{
			0.375, 1, 0.75, 0.375
	};
	private final double[] costs = new double[]{
			5, 8, 7, 4
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
	public final String alternateTint;
	public final int toolTier;
	public final boolean useTint;
	
	public MaterialTier(String name, int chestplateValue, int chestplateIntegrity, int chestplateDurability, boolean baseProvidesToughness, String key, String textureBase, String tint, String tool, int toolTier) {
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
		this.useTint = true;
		this.alternateTint = null;
	}
	
	public MaterialTier(String name, int chestplateValue, int chestplateIntegrity, int chestplateDurability, boolean baseProvidesToughness, String key, String textureBase, String tint, String tool, int toolTier, boolean useTint, String alternateTint) {
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
		this.useTint = useTint;
		this.alternateTint = alternateTint;
	}
	
	public String toStringModule(double scalar, double integrityLose, boolean loseIntegrity, boolean useToughness, String type, int part) {
		return
				("    {\n" +
						"      \"key\": \"" + type + "/" + key + "\",\n" +
						"      \"durability\": " + (chestplateDurability * ((multipliers[part] + 1) / 2) * scalar) + ",\n" +
						"      \"integrity\": " + (Math.ceil(chestplateIntegrity * multipliers[part] * scalar) * (loseIntegrity ? -integrityLose : 1)) + ",\n" +
						"      \"damage\": " + (useToughness ? (baseProvidesToughness ? (2 * scalar * multipliers[part]) : 0) : (Math.ceil(chestplateValue * multipliers[part] * scalar))) + ",\n" +
						"      \"attackSpeed\": " + (!useToughness ? (baseProvidesToughness ? (2 * scalar) : 0) : (Math.ceil(chestplateValue * multipliers[part] * scalar * multipliers[part]))) + ",\n" +
						"      \"glyph\": {\n" +
						"        \"tint\": \"" + tint + "_glyph\",\n" +
						"        \"textureX\": 88,\n" +
						"        \"textureY\": 16\n" +
						"      },\n" +
						"      \"models\": [{\n" +
						"        \"location\": \"tetric_additions:items/module/armor/helmet/" + textureBase + "\",\n" +
						"        \"tint\": \"" + (useTint ? (alternateTint == null ? tint : alternateTint) : "string") + "\"\n" +
						"      }]\n" +
						"    }").replace("%type%", type.substring(type.lastIndexOf("/") + 1));
	}
	
	public String toStringSchema(double scalar, double integrityLose, boolean loseIntegrity, String type, int part) {
		return
				("    {\n" +
						"      \"material\": {\n" +
						"        \"item\": \"" + name + "\",\n" +
						"        \"count\": " + ((Math.max(1, ((int) (Math.floor(costs[part] * scalar)))) + "").replace(".0", "")) + "\n" +
						"      },\n" +
						"      \"requiredCapabilities\": {\n" +
						"        \"" + tool + "\": " + toolTier + "\n" +
						"      },\n" +
						"      \"moduleKey\": \"" + type + "\",\n" +
						"      \"moduleVariant\": \"" + type + "/" + key + "\"\n" +
						"    }").replace("%type%", type.substring(type.lastIndexOf("/") + 1));
	}
}
