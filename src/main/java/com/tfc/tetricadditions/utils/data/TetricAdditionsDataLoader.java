package com.tfc.tetricadditions.utils.data;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

import java.util.Map;

public class TetricAdditionsDataLoader implements IPackFinder {
	public static void register(FMLServerAboutToStartEvent event) {
		event.getServer().getResourcePacks().addPackFinder(new TetricAdditionsDataLoader());
		TetricAdditionsDatapack.INSTANCE.setManager(event.getServer().getResourceManager());
	}
	
	@Override
	public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {
		nameToPackMap.put("TetricAdditionsData", ResourcePackInfo.createResourcePack(
				"TetricAdditionsData", true, () -> TetricAdditionsDatapack.INSTANCE,
				packInfoFactory, ResourcePackInfo.Priority.TOP
		));
	}
}
