package com.tfc.tetricadditions.utils.data;

import com.google.common.collect.ImmutableSet;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tfc.tetricadditions.utils.readers.TPropArray;
import com.tfc.tetricadditions.utils.readers.TPropObject;
import com.tfc.tetricadditions.utils.readers.TPropString;
import com.tfc.tetricadditions.utils.readers.TPropertiesReader;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Predicate;

public class TetricAdditionsDatapack implements IResourcePack {
	public static final TetricAdditionsDatapack INSTANCE = new TetricAdditionsDatapack(null);
	
	private final HashMap<ResourceLocation, TPropertiesReader> readers = new HashMap<>();
	private final HashMap<ResourceLocation, TPropertiesReader> readersMaterials = new HashMap<>();
	private final ArrayList<Closeable> closeables = new ArrayList<>();
	
	private IResourceManager manager;
	
	public TetricAdditionsDatapack(IResourceManager manager) {
		this.manager = manager;
	}
	
	public void setManager(IResourceManager manager) {
		this.manager = manager;
	}
	
	@Override
	public InputStream getRootResourceStream(String fileName) throws IOException {
//		icon = TetricAdditionsDatapack.class.getClassLoader().getResourceAsStream("assets/java-behavior-packs/builtin/bedrock.png");
//		return icon;
		return null;
	}
	
	private TPropertiesReader readMaterial(ResourceLocation name) throws IOException {
		if (readersMaterials.isEmpty()) {
			for (ResourceLocation location : manager.getAllResourceLocations("materials", (path) -> path.endsWith(".tproperties"))) {
				try {
//					System.out.println(location);
					TPropertiesReader reader = new TPropertiesReader(manager.getResource(location).getInputStream());
					String nameVal = ((TPropString) reader.getAsTPropObject().get("name")).get();
					nameVal = nameVal.substring(1, nameVal.length() - 1);
					readersMaterials.put(new ResourceLocation(nameVal), reader);
				} catch (Throwable ignored) {
				}
			}
		}
		return readersMaterials.get(name);
	}
	
	@Override
	public InputStream getResourceStream(ResourcePackType type, ResourceLocation location) throws IOException {
		if (!location.toString().startsWith("tetra:modules/") && !location.toString().startsWith("tetra:schemas/"))
			return null;
		
		String json;
		
		final String[] pieces = new String[]{"helmet", "chestplate", "leggings", "boots"};
		
		if (location.toString().startsWith("tetra:modules/")) {
			TPropObject moduleProperties = readers.get(location).getAsTPropObject();
			String key = ((TPropString) moduleProperties.get("name")).get().substring("tetra:modules/".length() + 1);
			key = key.substring(0, key.length() - 1);
			
			json = "{\"replace\":true,\"slots\":[\"" + key + "\"],\"type\":\"tetra:basic_module\",\"renderLayer\":\"highest\",\"tweakKey\":\"tetra:" + key + "\",\"variants\":[";
			
			for (String piece : pieces) {
				if (!location.toString().contains(piece)) continue;
				
				for (TPropObject object : ((TPropArray) moduleProperties.get("valid materials")).toArray()) {
					String material = ((TPropString) object).get();
//					System.out.println(material);
					TPropertiesReader reader = readMaterial(new ResourceLocation(material));
					TPropObject materialProperties = reader.getAsTPropObject();
					
					String name = ((TPropString) materialProperties.get("name")).get();
					name = name.substring(name.indexOf(":") + 1, name.length() - 1);
					
					String tint = name;
					boolean useTint = true;
					
					if (materialProperties.contains("tint"))
						tint = ((TPropString) materialProperties.get("tint")).get();
					
					if (materialProperties.contains("useTint"))
						useTint = Boolean.parseBoolean(((TPropString) materialProperties.get("useTint")).get());
					
					boolean loseIntegrity = Boolean.parseBoolean(moduleProperties.contains("decreasesIntegrity") ? ((TPropString) moduleProperties.get("decreasesIntegrity")).get() : "false");
					
					float durability = -999;
					float armor = -999;
					float toughness = -999;
					float integrity = 0;
					
					if (materialProperties.contains("overrides")) {
						TPropArray stats = ((TPropArray) materialProperties.get("overrides"));
						for (TPropObject stat : stats.toArray()) {
							String statS = ((TPropString) stat).get();
							if (statS.startsWith("durability_" + piece))
								durability = Float.parseFloat(statS.replace("durability_" + piece + ":", ""));
							else if (statS.startsWith("armor_" + piece))
								armor = Float.parseFloat(statS.replace("armor_" + piece + ":", ""));
							else if (statS.startsWith("toughness_" + piece))
								toughness = Float.parseFloat(statS.replace("toughness_" + piece + ":", ""));
							else if (statS.startsWith("integrity_" + piece))
								integrity = Float.parseFloat(statS.replace("integrity_" + piece + ":", ""));
						}
					}
					
					Item item = null;
					
					if (materialProperties.contains("parent for %piece%"))
						item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((TPropString) materialProperties.get("parent for %piece%")).get().replace("%piece%", piece)));
					else if (materialProperties.contains("parent for " + piece))
						item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((TPropString) materialProperties.get("parent for " + piece)).get().replace("%piece%", piece)));
					
					if (item instanceof ArmorItem) {
						ArmorItem armorItem = (ArmorItem) item;
						if (durability == -999)
							durability = armorItem.getMaxDamage(new ItemStack(item));
						if (armor == -999)
							armor = armorItem.getDamageReduceAmount();
						if (toughness == -999)
							toughness = armorItem.getToughness();
					}
					
					String model = "%key%";
					
					float scalar = 1;
					
					if (moduleProperties.contains("scale"))
						scalar = Float.parseFloat(((TPropString) moduleProperties.get("scale")).get());
					
					if (materialProperties.contains("model key"))
						model = ((TPropString) materialProperties.get("model key")).get();
					
					model = model.replace("%key%", ((TPropString) moduleProperties.get("key")).get());

//					try {
//					System.out.println(loseIntegrity);
//					System.out.println((loseIntegrity ? -1 : 1));
					json += "{\"key\":\"" + key + "/" + name + "\"," +
							"\"durability\":" + (int) (durability * scalar) + "," +
							"\"integrity\":" + Math.ceil((integrity * scalar) * (loseIntegrity ? 2f : 1)) * (loseIntegrity ? -1 : 1) + "," +
							"\"damage\":" + (armor * scalar) + "," +
							"\"attackSpeed\":" + (toughness * scalar) + "," +
							"\"effects\":{" +
							"\"armor\":" + (armor * scalar) + "," +
							"\"toughness\":" + (toughness * scalar) + "}," +
							"\"glyph\":{" +
							"\"tint\":\"" + tint + "_glyph\"," +
							"\"textureX\":88," +
							"\"textureY\":16" +
							"},\"models\":[{" +
							"\"location\":\"tetric_additions:items/module/" + key.substring(0, key.lastIndexOf("/")) + "/" + model + "\"," +
							"\"tint\":\"" + (useTint ? tint : "string") + "\"" +
							"}]},";
//					} catch (Throwable ignored) {
//						System.out.println(durability);
//						System.out.println(armor);
//						System.out.println(toughness);
//						throw new RuntimeException(ignored);
//					}
				}
				
				json = json.replace("%piece%", piece).replace("/alt", "");
				json = json.substring(0, json.length() - 1);
				json += "]}";
//				System.out.println(json);
			}
		} else {
			TPropObject moduleProperties = readers.get(location).getAsTPropObject();
			String key = ((TPropString) moduleProperties.get("name")).get().substring("tetra:modules/".length() + 1);
			key = key.substring(0, key.length() - 1);
			
			json = "{" +
					"\"replace\":false," +
					"\"slots\":[" +
					"\"" + key + "\"]," +
					"\"materialSlotCount\":1," +
					"\"displayType\":\"major\"," +
					"\"glyph\":{" +
					"\"textureX\":99," +
					"\"textureY\":35}," +
					"\"outcomes\":[";
			for (String piece : pieces) {
				if (!location.toString().contains(piece)) continue;
				
				for (TPropObject object : ((TPropArray) moduleProperties.get("valid materials")).toArray()) {
					String material = ((TPropString) object).get();
//					System.out.println(material);
					TPropertiesReader reader = readMaterial(new ResourceLocation(material));
					TPropObject materialProperties = reader.getAsTPropObject();
					
					String name = ((TPropString) materialProperties.get("name")).get();
					name = name.substring(name.indexOf(":") + 1, name.length() - 1);
					
					float scalar = 1;
					
					if (moduleProperties.contains("scale"))
						scalar = Float.parseFloat(((TPropString) moduleProperties.get("scale")).get());
					
					TPropArray caps = (TPropArray) materialProperties.get("neededCapabilities");
					
					String capabilities = "";
					for (TPropObject val : caps.toArray()) {
						String cap = ((TPropString) val).get();
						String[] parts = cap.split(":");
						capabilities += ",\"" + parts[0] + "\":" + parts[1];
					}
					if (!capabilities.isEmpty()) capabilities = capabilities.substring(1);

//					System.out.println(capabilities);
					
					int quantity = 0;
					if (piece.equals("helmet")) quantity = 5;
					else if (piece.equals("boots")) quantity = 4;
					else if (piece.equals("leggings")) quantity = 7;
					else if (piece.equals("chestplate")) quantity = 8;
					
					json +=
							"{\"material\":{" +
									"\"item\":\"" + material + "\"," +
									"\"count\":" + (int) Math.ceil(quantity * scalar) + "}," +
									"\"requiredCapabilities\":{" +
									"" + capabilities + "}," +
									"\"moduleKey\":\"" + key + "\"," +
									"\"moduleVariant\":\"" + key + "/" + name + "\"},";
				}
				
				json = json.replace("%piece%", piece).replace("/alt", "");
				json = json.substring(0, json.length() - 1);
				json += "]}";
//				System.out.println(json);
			}
		}

		ByteArrayInputStream stream = new ByteArrayInputStream(json.getBytes());
		closeables.add(stream);

		return stream;
	}
	
	@Override
	public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String namespaceIn, String pathIn, int maxDepthIn, Predicate<String> filterIn) {
		if (pathIn.equals("module_types")) {
			if (maxDepthIn == 0)
				return manager.getAllResourceLocations("module_types", (path) -> path.toLowerCase().endsWith(".tproperties"));
			else
				return ImmutableSet.of();
		} else if (pathIn.equals("modules")) {
			Collection<ResourceLocation> locations = getAllResourceLocations(type, namespaceIn, "module_types", 0, (path) -> path.toLowerCase().endsWith(".tproperties"));
			ArrayList<ResourceLocation> returnVal = new ArrayList<>();
			for (ResourceLocation location : locations) {
				try {
					TPropertiesReader reader = new TPropertiesReader(manager.getResource(location).getInputStream());
					String name = ((TPropString) reader.getAsTPropObject().get("name")).get();
					name = name.substring(1, name.length() - 1);
					
					final String[] pieces = new String[]{
							"helmet", "chestplate", "leggings", "boots"
					};
					
					for (String piece : pieces) {
						ResourceLocation loc = new ResourceLocation(name.replace("%piece%", piece) + ".json");
						readers.put(loc, reader);
						returnVal.add(loc);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
//				returnVal.add(new ResourceLocation(location.toString().replace(":module_types",":modules").replace()));
			}
//			System.out.println(Arrays.toString(returnVal.toArray()));
			return returnVal;
		} else if (pathIn.equals("schemas")) {
			Collection<ResourceLocation> locations = getAllResourceLocations(type, namespaceIn, "module_types", 0, (path) -> path.toLowerCase().endsWith(".tproperties"));
			ArrayList<ResourceLocation> returnVal = new ArrayList<>();
			for (ResourceLocation location : locations) {
				try {
					TPropertiesReader reader = new TPropertiesReader(manager.getResource(location).getInputStream());
					String name = ((TPropString) reader.getAsTPropObject().get("name")).get();
					name = name.substring(1, name.length() - 1);
					
					final String[] pieces = new String[]{
							"helmet", "chestplate", "leggings", "boots"
					};
					
					for (String piece : pieces) {
						ResourceLocation loc = new ResourceLocation(name.replace("%piece%", piece).replace("modules", "schemas") + ".json");
						readers.put(loc, reader);
						returnVal.add(loc);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
//				returnVal.add(new ResourceLocation(location.toString().replace(":module_types",":modules").replace()));
			}
//			System.out.println(Arrays.toString(returnVal.toArray()));
			return returnVal;
		} else {
			return ImmutableSet.of();
		}
	}
	
	@Override
	public boolean resourceExists(ResourcePackType type, ResourceLocation location) {
		if (location.toString().contains("modules")) {
//			if (readers.isEmpty()) return manager.getAllResourceLocations("module_types",(path)->path.toLowerCase().endsWith(".tproperties")).contains(location);
//			System.out.println("hi");
			if (readers.isEmpty())
				return getAllResourceLocations(type, "", "modules", 1, (path) -> path.toLowerCase().endsWith(".tproperties")).contains(location);
			else return readers.containsKey(location);
		} else if (location.toString().contains("schemas")) {
//			if (readers.isEmpty()) return manager.getAllResourceLocations("module_types",(path)->path.toLowerCase().endsWith(".tproperties")).contains(location);
//			System.out.println("hi");
			if (readers.isEmpty())
				return getAllResourceLocations(type, "", "schemas", 1, (path) -> path.toLowerCase().endsWith(".tproperties")).contains(location);
			else return readers.containsKey(location);
		}
		return false;
//		return location.getPath().startsWith("materials");
	}
	
	@Override
	public Set<String> getResourceNamespaces(ResourcePackType type) {
		return manager.getResourceNamespaces();
	}
	
	@Nullable
	@Override
	public <T> T getMetadata(IMetadataSectionSerializer<T> deserializer) throws IOException {
		return deserializer.deserialize(
				new GsonBuilder().setLenient().setPrettyPrinting().create().fromJson("" +
						"{\n" +
						"\"pack_format\": 6,\n" +
						"\"description\": \"Tetric Additins data loader\"\n" +
						"}", JsonObject.class)
		);
	}
	
	@Override
	public String getName() {
		return "TetricAdditionsData";
	}
	
	@Override
	public void close() throws IOException {
		for (TPropertiesReader reader : readers.values()) reader.close();
		for (TPropertiesReader reader : readersMaterials.values()) reader.close();
		for (Closeable closeable : closeables) closeable.close();
		readers.clear();
		readersMaterials.clear();
		closeables.clear();
	}
}
