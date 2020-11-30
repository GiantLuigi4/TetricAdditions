package com.tfc.tetricadditions.modules;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import se.mickelus.tetra.items.modular.BasicModule;
import se.mickelus.tetra.module.Priority;
import se.mickelus.tetra.module.data.ModuleData;

public class DyableArmorModule extends BasicModule {
	public DyableArmorModule(ResourceLocation identifier, ModuleData data) {
		super(identifier, data);
	}
	
	@Override
	public Priority getRenderLayer() {
		return super.getRenderLayer();
	}
	
	@Override
	public void addModule(ItemStack targetStack, String variantKey, PlayerEntity player) {
		super.addModule(targetStack, variantKey, player);
	}
	
	@Override
	public ItemStack[] removeModule(ItemStack targetStack) {
		return super.removeModule(targetStack);
	}
	
	@Override
	public void postRemove(ItemStack targetStack, PlayerEntity player) {
		super.postRemove(targetStack, player);
	}
}
