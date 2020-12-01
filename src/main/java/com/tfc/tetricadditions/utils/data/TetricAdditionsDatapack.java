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
import java.util.*;
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
					System.out.println(location);
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
//		if (location.toString().contains("helmet")) System.out.println(location);
		if (location.toString().startsWith("tetra:modules/")) {
			TPropObject propObject = readers.get(location).getAsTPropObject();
			String key = ((TPropString) propObject.get("name")).get().substring("tetra:modules/".length() + 1);
			key = key.substring(0, key.length() - 1);
			String json = "{\"replace\":true,\"slots\":[\"" + key + "\"],\"type\":\"tetra:basic_module\",\"renderLayer\":\"highest\",\"tweakKey\":\"tetra:" + key + "\",\"variants\":[";
			
			final String[] pieces = new String[]{"helmet", "chestplate", "leggings", "boots"};
			
			for (String s : pieces) {
				if (location.toString().contains(s)) {
					for (TPropObject object : ((TPropArray) propObject.get("valid materials")).toArray()) {
						String material = ((TPropString) object).get();
						System.out.println(material);
						TPropertiesReader reader = readMaterial(new ResourceLocation(material));
						TPropObject object1 = reader.getAsTPropObject();
						
						String name = ((TPropString) object1.get("name")).get();
						name = name.substring(name.indexOf(":") + 1, name.length() - 1);
						
						String tint = name;
						boolean useTint = true;
						
						if (object1.contains("tint")) tint = ((TPropString) object1.get("tint")).get();
						
						if (object1.contains("useTint"))
							useTint = Boolean.parseBoolean(((TPropString) object1.get("useTint")).get());
						
						boolean loseIntegrity = Boolean.parseBoolean(object1.contains("decreasesIntegrity") ? ((TPropString) object1.get("decreasesIntegrity")).get() : "false");
						
						String durability = null;
						String armor = null;
						String toughness = null;
						float integrity = 0;
						
						if (object1.contains("overrides")) {
							TPropArray stats = ((TPropArray) object1.get("overrides"));
							for (TPropObject stat : stats.toArray()) {
								String statS = ((TPropString) stat).get();
								if (statS.startsWith("durability_" + s))
									durability = statS.replace("durability_" + s + ":", "");
								else if (statS.startsWith("armor_" + s)) armor = statS.replace("armor_" + s + ":", "");
								else if (statS.startsWith("toughness_" + s))
									toughness = statS.replace("toughness_" + s + ":", "");
								else if (statS.startsWith("integrity_" + s))
									integrity = Float.parseFloat(statS.replace("integrity_" + s + ":", ""));
							}
						}
						
						Item item = null;
						
						if (object1.contains("parent for %piece%"))
							item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((TPropString) object1.get("parent for %piece%")).get().replace("%piece%", s)));
						else if (object1.contains("parent for " + s))
							item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((TPropString) object1.get("parent for " + s)).get().replace("%piece%", s)));
						
						if (item instanceof ArmorItem) {
							ArmorItem armorItem = (ArmorItem) item;
							if (durability == null) durability = armorItem.getMaxDamage(new ItemStack(item)) + "";
							if (armor == null) armor = armorItem.getDamageReduceAmount() + "";
							if (toughness == null) toughness = armorItem.getToughness() + "";
						}
						
						String model = "%key%";
						
						float scalar = 1;
						
						if (object1.contains("scale"))
							scalar = Float.parseFloat(((TPropString) object1.get("scale")).get());
						
						if (object1.contains("model key")) model = ((TPropString) object1.get("model key")).get();
						
						model = model.replace("%key%", ((TPropString) propObject.get("key")).get());
						
						try {
							json += "{\"key\":\"" + key + "/" + name + "\"," +
									"\"durability\":" + (int) (Float.parseFloat(durability) * scalar) + "," +
									"\"integrity\":" + (Math.ceil(integrity * scalar) * (loseIntegrity ? -1 : 1)) + "," +
									"\"damage\":" + (Float.parseFloat(armor) * scalar) + "," +
									"\"attackSpeed\":" + (Float.parseFloat(toughness) * scalar) + "," +
									"\"effects\":{" +
									"\"armor\":" + (Float.parseFloat(armor) * scalar) + "," +
									"\"toughness\":" + (Float.parseFloat(toughness) * scalar) + "}," +
									"\"glyph\":{" +
									"\"tint\":\"" + tint + "_glyph\"," +
									"\"textureX\":88," +
									"\"textureY\":16" +
									"},\"models\":[{" +
									"\"location\":\"tetric_additions:items/module/" + key.substring(0, key.lastIndexOf("/")) + "/" + model + "\"," +
									"\"tint\":\"" + (useTint ? tint : "string") + "\"" +
									"}]},";
						} catch (Throwable ignored) {
							System.out.println(durability);
							System.out.println(armor);
							System.out.println(toughness);
							throw new RuntimeException(ignored);
						}
					}
					
					json = json.replace("%piece%", s).replace("/alt", "");
					json = json.substring(0, json.length() - 1);
					json += "]}";
					System.out.println(json);
				}
			}
			
			ByteArrayInputStream stream = new ByteArrayInputStream(json.getBytes());
			closeables.add(stream);
			
			return stream;
		}
		return null;
//		throw new IOException("NYI");
	}
	
	@Override
	public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String namespaceIn, String pathIn, int maxDepthIn, Predicate<String> filterIn) {
		if (pathIn.equals("module_types")) {
			if (maxDepthIn == 0) {
				return manager.getAllResourceLocations("module_types", (path) -> path.toLowerCase().endsWith(".tproperties"));
			} else {
				return ImmutableSet.of();
			}
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
			System.out.println(Arrays.toString(returnVal.toArray()));
			return ImmutableSet.of();
		} else {
			return ImmutableSet.of();
		}
	}
	
	@Override
	public boolean resourceExists(ResourcePackType type, ResourceLocation location) {
		if (location.toString().contains("modules")) {
//			if (readers.isEmpty()) return manager.getAllResourceLocations("module_types",(path)->path.toLowerCase().endsWith(".tproperties")).contains(location);
			if (readers.isEmpty())
				return getAllResourceLocations(type, "", "modules", 1, (path) -> path.toLowerCase().endsWith(".tproperties")).contains(location);
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
