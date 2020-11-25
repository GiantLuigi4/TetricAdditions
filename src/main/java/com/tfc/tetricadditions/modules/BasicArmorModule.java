package com.tfc.tetricadditions.modules;

import net.minecraft.util.ResourceLocation;
import se.mickelus.tetra.items.modular.BasicModule;
import se.mickelus.tetra.module.Priority;
import se.mickelus.tetra.module.data.ModuleData;
import se.mickelus.tetra.module.data.ModuleVariantData;

public class BasicArmorModule extends BasicModule {
	public BasicArmorModule(ResourceLocation identifier, ModuleData data) {
		super(identifier, data);
		
		ModuleVariantData data1 = new ModuleVariantData();
		data1.durability = 10;
		data1.integrity = 1;
		data1.damage = 3;
		this.variantData = new ModuleVariantData[]{
				data1
		};
	}
	
	@Override
	public Priority getRenderLayer() {
		return super.getRenderLayer();
	}
	
	@Override
	public ModuleVariantData[] getVariantData() {
		return super.getVariantData();
	}
}
