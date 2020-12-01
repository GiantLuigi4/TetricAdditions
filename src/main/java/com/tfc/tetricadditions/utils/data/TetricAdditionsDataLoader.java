package com.tfc.tetricadditions.utils.data;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

import java.util.Map;

public class TetricAdditionsDataLoader implements IPackFinder /*extends ReloadListener<Map<ResourceLocation, JsonElement>>*/ {
	public static void register(FMLServerAboutToStartEvent event) {
//		event.getServer().getResourceManager().addReloadListener(new TetricAdditionsDataLoader());
		event.getServer().getResourcePacks().addPackFinder(new TetricAdditionsDataLoader());
		TetricAdditionsDatapack.INSTANCE.setManager(event.getServer().getResourceManager());
	}
	
	@Override
	public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {
		nameToPackMap.put("TetricAdditionsData", ResourcePackInfo.createResourcePack(
				"TetricAdditionsData", true, () -> TetricAdditionsDatapack.INSTANCE,
				packInfoFactory, ResourcePackInfo.Priority.BOTTOM
		));
	}
	
	/*	@Override
	protected Map<ResourceLocation, JsonElement> prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
		Collection<ResourceLocation> locations = resourceManagerIn.getAllResourceLocations("materials",(path)->path.toLowerCase().endsWith(".tproperties"));
		int lastSize = -100;
		System.out.println("Waiting for tetra to load its resources.");
		while (DataManager.moduleData.getData().size()>lastSize) {
			lastSize = DataManager.moduleData.getData().size();
			try {
				Thread.sleep(1000);
			} catch (Throwable ignored) {
			}
		}
		System.out.println(locations.size());
		System.out.println(Arrays.toString(locations.toArray()));
		String[] materials = new String[0];
		try {
			for (ResourceLocation location : locations) {
				IResource resource = resourceManagerIn.getResource(location);
				if (resource.getInputStream())
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		DataManager.moduleData.getRawData().put();
		return new HashMap<>();
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
	}*/
}
