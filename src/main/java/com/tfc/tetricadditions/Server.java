package com.tfc.tetricadditions;

import com.tfc.tetricadditions.tools.ModularArmorItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import se.mickelus.tetra.module.ItemModule;

import java.util.concurrent.atomic.AtomicInteger;

public class Server {
	public static void onEntityDamaged(LivingDamageEvent event) {
		if (!event.getSource().damageType.equals("thorns")) {
			if (event.getSource().getTrueSource() instanceof LivingEntity) {
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
							}
						}
					}
				});
				if (totalThorns.get() != 0) {
					event.getSource().getTrueSource().attackEntityFrom(DamageSource.causeThornsDamage(event.getEntity()),
							Math.min(30, totalThorns.get() * (Math.max(0, event.getAmount()) / 3f))
					);
				}
			}
		}
		event.getEntity().getArmorInventoryList().iterator().forEachRemaining((stack) -> {
			if (event.getSource().getImmediateSource() != null || event.getSource().getTrueSource() != null) {
				if (stack.getItem() instanceof ModularArmorItem) {
					ModularArmorItem armor = (ModularArmorItem) stack.getItem();
					//TODO: add unbreaking handler
					for (String key : armor.getMajorModuleKeys()) {
						ItemModule major = armor.getModuleFromSlot(stack, key);
						if (major != null) {
						}
					}
					for (ItemModule minor : armor.getMinorModules(stack)) {
						if (minor != null) {
						}
					}
					double toughness = 1;
					for (AttributeModifier modifier : stack.getAttributeModifiers(stack.getItem().getEquipmentSlot(stack)).get(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName())) {
						toughness += modifier.getAmount();
					}
					double scalar = 0.8f;
					System.out.println(scalar);
					System.out.println(toughness);
					System.out.println(scalar * event.getAmount() / (toughness));
					System.out.println(event.getAmount() / (toughness));
					stack.setDamage((int) (stack.getDamage() + (scalar * event.getAmount()) / (toughness)));
				}
			}
		});
	}
}
