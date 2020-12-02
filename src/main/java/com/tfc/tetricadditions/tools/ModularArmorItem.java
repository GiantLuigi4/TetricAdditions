package com.tfc.tetricadditions.tools;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.tetra.items.modular.ModularItem;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
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
	
	public static String getPieceNameForStack(ItemStack stack) {
		if (stack.getItem() instanceof ModularArmorItem) {
			EquipmentSlotType slot = ((ModularArmorItem) stack.getItem()).slot;
			switch (slot) {
				case CHEST:
					return "chest";
				case FEET:
					return "boots";
				case LEGS:
					return "leggings";
				case HEAD:
					return "helmet";
			}
			return slot.getName().toLowerCase();
		} else {
			throw new RuntimeException(new IllegalArgumentException("Item of class " + stack.getItem().getClass().getName() + " is not an ModularArmorItem."));
		}
	}
	
	public final String getPieceName() {
		switch (slot) {
			case CHEST:
				return "chestplate";
			case FEET:
				return "boots";
			case LEGS:
				return "leggings";
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
	
	@Override
	public void onArmorTick(ItemStack itemStack, World world, PlayerEntity player) {
//		double armorModifier = this.getDamageModifier(itemStack);
//		double armorToughnessModifier = this.getSpeedModifier(itemStack);
//		try {
////			if (!itemStack.getOrCreateTag().contains("AttributeModifiers")) {
////				itemStack.getOrCreateTag().put("AttributeModifiers",new ListNBT());
////			}
////			ListNBT nbt = itemStack.getOrCreateTag().getList("AttributeModifiers", 10);
////			AtomicBoolean hasTetricArmorMod = new AtomicBoolean(false);
////			nbt.forEach((nbt_check)->{
////				if (nbt_check instanceof CompoundNBT) {
////					CompoundNBT compoundNBT = (CompoundNBT) nbt_check;
////					if (compoundNBT.getString("AttributeName").equals("generic.armor")) {
////						if (compoundNBT.getString("Name").equals("tetric_additions.armor_mod")) {
////							hasTetricArmorMod.set(true);
////							compoundNBT.putDouble("Amount", (int) armor_modifier);
////						}
////					}
////				}
////			});
////			if (!hasTetricArmorMod.get()) {
////				CompoundNBT modifier = new CompoundNBT();
////				modifier.putString("AttributeName","generic.armor");
////				modifier.putString("Name","tetric_additions.armor_mod");
////				modifier.putDouble("Amount",1);
////				modifier.putString("Slot",slot.getName());
////				modifier.putInt("Operation",0);
////				modifier.putUniqueId("UUID",ARMOR_MODIFIERS[0]);
////				nbt.add(0,modifier);
////				System.out.println(nbt);
////			}
////			itemStack.getOrCreateTag().put("AttributeModifiers",nbt);
//
//			if (!itemStack.getOrCreateTag().contains("AttributeModifiers")) {
//				itemStack.getOrCreateTag().put("AttributeModifiers",new ListNBT());
//			}
//			boolean hasArmorModif = true;
//			boolean hasArmorToughnessModif = true;
//			while (hasArmorModif || hasArmorToughnessModif) {
//				ListNBT modifiers = itemStack.getOrCreateTag().getList("AttributeModifiers",10);
//				hasArmorModif = false;
//				hasArmorToughnessModif = false;
//				int index = 0;
//				int armorModifierNBT = 0;
//				int armorToughnessNBT = 0;
//				for (INBT inbt : new ArrayList<>(modifiers)) {
//					if (inbt instanceof CompoundNBT) {
//						if (
//								((CompoundNBT) inbt).getString("Name").equals("tetric_addtions.armor") ||
//										((CompoundNBT) inbt).getUniqueId("UUID").equals(ARMOR_MODIFIERS[0])
//						) {
//							armorModifierNBT = index;
//							hasArmorModif = true;
//						} else if (
//								((CompoundNBT) inbt).getString("Name").equals("tetric_addtions.armor_toughness") ||
//										((CompoundNBT) inbt).getUniqueId("UUID").equals(ARMOR_MODIFIERS2[0])
//						) {
//							armorToughnessNBT = index;
//							hasArmorToughnessModif = true;
//						} else {
//							index++;
//						}
//						if (hasArmorModif&&hasArmorToughnessModif) {
//							break;
//						}
//					}
//				}
//				if (hasArmorModif) {
//					modifiers.remove(armorModifierNBT);
//					modifiers.remove(armorToughnessNBT);
//				}
//				itemStack.getOrCreateTag().put("AttributeModifiers",modifiers);
//			}
//
//			itemStack.addAttributeModifier(
//					SharedMonsterAttributes.ARMOR.getName(),
//					new AttributeModifier(
//							ARMOR_MODIFIERS[slot.getIndex()],
//							"tetric_addtions.armor",
//							Math.round(armorModifier),
//							AttributeModifier.Operation.ADDITION
//					),
//					EquipmentSlotType.HEAD
//			);
//			itemStack.addAttributeModifier(
//					SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(),
//					new AttributeModifier(
//							ARMOR_MODIFIERS2[slot.getIndex()],
//							"tetric_addtions.armor_toughness",
//							armorToughnessModifier,
//							AttributeModifier.Operation.ADDITION
//					),
//					EquipmentSlotType.HEAD
//			);
//		} catch (Throwable ignored) {
//			ignored.printStackTrace();
//		}
	}
	
	@Nullable
	@Override
	public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
		return slot;
	}
	
	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
		return armorType.equals(slot);
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
	public void tweak(ItemStack itemStack, String slot, Map<String, Integer> tweaks) {
		super.tweak(itemStack, slot, tweaks);
		
		System.out.println(slot);
//		((ModularArmorItem)itemStack.getItem()).getSpeedModifier(itemStack)
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		if (!this.isBroken(stack) && slot == this.slot) {
			double armorModifier = this.getDamageModifier(stack);
			double armorToughnessModifier = this.getSpeedModifier(stack);
			modifiers.put(
					SharedMonsterAttributes.ARMOR.getName(),
					new AttributeModifier(
							ARMOR_MODIFIERS[slot.getIndex()],
							"tetra.armor_mod",
							armorModifier,
							AttributeModifier.Operation.ADDITION
					)
			);
			modifiers.put(
					SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(),
					new AttributeModifier(
							ARMOR_MODIFIERS2[slot.getIndex()],
							"generic.armor_toughness",
							armorToughnessModifier,
							AttributeModifier.Operation.ADDITION
					)
			);
		}
		
		return modifiers;
	}
	
	public double getSpeedModifier(ItemStack itemStack) {
		if (this.isBroken(itemStack)) {
			return 0.0D;
		} else {
			double speedModifier = this.getAllModules(itemStack).stream().map((itemModule) -> itemModule.getSpeedModifier(itemStack)).reduce(0d, Double::sum);
			speedModifier = Arrays.stream(this.getSynergyData(itemStack)).mapToDouble((synergyData) -> (double) synergyData.attackSpeed).reduce(speedModifier, Double::sum);
			speedModifier = Arrays.stream(this.getSynergyData(itemStack)).mapToDouble((synergyData) -> (double) synergyData.attackSpeedMultiplier).reduce(speedModifier, (a, b) -> a * b);
			speedModifier = this.getAllModules(itemStack).stream().map((itemModule) -> itemModule.getSpeedMultiplierModifier(itemStack)).reduce(speedModifier, (a, b) -> a * b);
			return speedModifier;
		}
	}
	
	public double getDamageModifier(ItemStack itemStack) {
		if (this.isBroken(itemStack)) {
			return 0.0D;
		} else {
			double damageModifier = this.getAllModules(itemStack).stream().mapToDouble((itemModule) -> itemModule.getDamageModifier(itemStack)).sum();
			damageModifier = Arrays.stream(this.getSynergyData(itemStack)).mapToDouble((synergyData) -> (double) synergyData.damage).reduce(damageModifier, Double::sum);
			damageModifier = Arrays.stream(this.getSynergyData(itemStack)).mapToDouble((synergyData) -> synergyData.damageMultiplier).reduce(damageModifier, (a, b) -> a * b);
			return this.getAllModules(itemStack).stream().map((itemModule) -> itemModule.getDamageMultiplierModifier(itemStack)).reduce(damageModifier, (a, b) -> a * b);
		}
	}
}
