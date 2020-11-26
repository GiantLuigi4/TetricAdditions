package com.tfc.tetricadditions;

import com.tfc.tetricadditions.tools.ModularArmorItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import se.mickelus.tetra.module.ItemModule;

import java.util.concurrent.atomic.AtomicInteger;

public class Server {
	public static void onEntityDamaged(LivingDamageEvent event) {
		if (event.getSource().getImmediateSource() instanceof LivingEntity) {
			AtomicInteger totalThorns = new AtomicInteger();
			event.getEntity().getArmorInventoryList().iterator().forEachRemaining((stack) -> {
				if (stack.getItem() instanceof ModularArmorItem) {
					ModularArmorItem armor = (ModularArmorItem) stack.getItem();
					for (String key : armor.getMajorModuleKeys()) {
						ItemModule major = armor.getModuleFromSlot(stack, key);
						if (major != null) {
							if (major.getName(stack).contains("cactus")) totalThorns.getAndAdd(3);
							else if (major.getName(stack).contains("prismarine")) totalThorns.getAndAdd(1);
						}
					}
					for (ItemModule minor : armor.getMinorModules(stack)) {
						if (minor != null) {
							if (minor.getName(stack).contains("cactus")) totalThorns.getAndAdd(2);
							else if (minor.getName(stack).contains("prismarine")) totalThorns.getAndAdd(1);
							System.out.println(minor.getName(stack));
						}
					}
				}
			});
			event.getSource().getImmediateSource().attackEntityFrom(DamageSource.causeThornsDamage(event.getEntity()), totalThorns.get());
		}
	}
}
