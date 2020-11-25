package com.tfc.tetricadditions.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.tfc.tetricadditions.tools.renderer.HelmetRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.tetra.items.modular.ModularItem;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.UUID;

public class ModularArmorItem extends ModularItem {
	protected final EquipmentSlotType slot;
	
	public static final String plate1Key = "armor/%piece%/plate1";
	public static final String plate2Key = "armor/%piece%/plate2";
	public static final String socketKey = "armor/%piece%/socket";
	public static final String bindingKey = "armor/%piece%/binding";
	
	public ModularArmorItem(Properties properties, EquipmentSlotType slot) {
		super(properties);
		this.slot = slot;
	}
	
	public final String getPieceName() {
		switch (slot) {
			case CHEST:
				return "chest";
			case FEET:
				return "boots";
			case LEGS:
				return "pants";
			case HEAD:
				return "helmet";
		}
		return slot.getName().toLowerCase();
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		EquipmentSlotType equipmentslottype = MobEntity.getSlotForItemStack(itemstack);
		ItemStack itemstack1 = playerIn.getItemStackFromSlot(equipmentslottype);
		if (itemstack1.isEmpty()) {
			playerIn.setItemStackToSlot(equipmentslottype, itemstack.copy());
			itemstack.setCount(0);
			return ActionResult.resultSuccess(itemstack);
		} else {
			return ActionResult.resultFail(itemstack);
		}
	}
	
	@Nullable
	@Override
	public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
		return slot;
	}
	
	@OnlyIn(Dist.CLIENT)
	public void render(IRenderTypeBuffer buffer, ItemStack stack, LivingEntity entity, MatrixStack matrixStack, int packedLightIn) {
	}
	
	private static final UUID[] ARMOR_MODIFIERS = new UUID[]{
			UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
			UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
			UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
			UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
	};
	
	private static final UUID[] ARMOR_MODIFIERS2 = new UUID[]{
			UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6C"),
			UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0F"),
			UUID.fromString("9F3D476D-C118-4544-8365-64846904B48A"),
			UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB151")
	};
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<String, AttributeModifier> modifiers = HashMultimap.create();
		if (slot == EquipmentSlotType.HEAD) {
			double armor_modifier = this.getDamageModifier(stack);
			modifiers.put(
					SharedMonsterAttributes.ARMOR.getName(),
					new AttributeModifier(
							ARMOR_MODIFIERS[0],
					"tetric_additions.armor_mod",
							armor_modifier,
							AttributeModifier.Operation.ADDITION
					)
			);
			double armorToughnessModifier = this.getSpeedModifier(stack);
			modifiers.put(
					SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(),
					new AttributeModifier(
							ARMOR_MODIFIERS2[0],
					"tetric_additions.armor_mod",
							armorToughnessModifier,
							AttributeModifier.Operation.ADDITION
					)
			);
		}
		
		return modifiers;
	}
	
	public double getSpeedModifier(ItemStack itemStack) {
		double speedModifier = (Double)this.getAllModules(itemStack).stream().map((itemModule) -> {
			return itemModule.getSpeedModifier(itemStack);
		}).reduce(0d, Double::sum);
		speedModifier = Arrays.stream(this.getSynergyData(itemStack)).mapToDouble((synergyData) -> {
			return (double)synergyData.attackSpeed;
		}).reduce(speedModifier, Double::sum);
		speedModifier = Arrays.stream(this.getSynergyData(itemStack)).mapToDouble((synergyData) -> {
			return (double)synergyData.attackSpeedMultiplier;
		}).reduce(speedModifier, (a, b) -> {
			return a * b;
		});
		speedModifier = (Double)this.getAllModules(itemStack).stream().map((itemModule) -> {
			return itemModule.getSpeedMultiplierModifier(itemStack);
		}).reduce(speedModifier, (a, b) -> {
			return a * b;
		});
//		speedModifier *= this.getCounterWeightMultiplier(itemStack);
//		if (speedModifier < -4.0D) {
//			speedModifier = -3.9D;
//		}
		
		return speedModifier;
	}
	
	public double getDamageModifier(ItemStack itemStack) {
		if (this.isBroken(itemStack)) {
			return 0.0D;
		} else {
			double damageModifier = this.getAllModules(itemStack).stream().mapToDouble((itemModule) -> {
				return itemModule.getDamageModifier(itemStack);
			}).sum();
			damageModifier = Arrays.stream(this.getSynergyData(itemStack)).mapToDouble((synergyData) -> {
				return (double)synergyData.damage;
			}).reduce(damageModifier, Double::sum);
			damageModifier = Arrays.stream(this.getSynergyData(itemStack)).mapToDouble((synergyData) -> {
				return (double)synergyData.damageMultiplier;
			}).reduce(damageModifier, (a, b) -> {
				return a * b;
			});
			return (Double)this.getAllModules(itemStack).stream().map((itemModule) -> {
				return itemModule.getDamageMultiplierModifier(itemStack);
			}).reduce(damageModifier, (a, b) -> {
				return a * b;
			});
		}
	}
}
