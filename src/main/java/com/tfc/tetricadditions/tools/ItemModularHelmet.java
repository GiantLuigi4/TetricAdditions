package com.tfc.tetricadditions.tools;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tfc.tetricadditions.tools.renderer.HelmetRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.SchemaRegistry;
import se.mickelus.tetra.module.data.ModuleModel;
import se.mickelus.tetra.module.schema.RemoveSchema;
import se.mickelus.tetra.module.schema.RepairSchema;

import javax.annotation.Nullable;

public class ItemModularHelmet extends ModularArmorItem {
	public static final String unlocalizedName = "modular_helmet";
	
	public static final String headPieceKey = "armor/helmet/base";
	
	public ItemModularHelmet(Properties properties) {
		super(properties, EquipmentSlotType.HEAD);
		
		majorModuleKeys = new String[]{headPieceKey.replace("%piece%", getPieceName()), plate1Key.replace("%piece%", getPieceName())};
		minorModuleKeys = new String[]{plate2Key.replace("%piece%", getPieceName()), bindingKey.replace("%piece%", getPieceName()), socketKey.replace("%piece%", getPieceName())};
		
		requiredModules = new String[]{headPieceKey.replace("%piece%", getPieceName())};
		
		setRegistryName(unlocalizedName);
		
		honeBase = 100;
		
		SchemaRegistry.instance.registerSchema(new RepairSchema(this));
		RemoveSchema.registerRemoveSchemas(this);
	}
	
	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
		return armorType.equals(EquipmentSlotType.HEAD);
	}
	
	@Nullable
	@Override
	@OnlyIn(Dist.CLIENT)
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
		if (entityLiving instanceof PlayerEntity || entityLiving instanceof ArmorStandEntity)
			return (A) HelmetRenderer.playerModel;
		else if (entityLiving instanceof SkeletonEntity)
			return (A) HelmetRenderer.skeletonModel;
		else if (entityLiving instanceof ZombieEntity)
			return (A) HelmetRenderer.zombieModel;
		return null;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(IRenderTypeBuffer buffer, ItemStack stack, LivingEntity entity, MatrixStack matrixStack, int packedLightIn) {
		super.render(buffer, stack, entity, matrixStack, packedLightIn);
		ModularArmorItem item = ((ModularArmorItem) stack.getItem());
		ItemModule module = item.getModuleFromSlot(stack, headPieceKey.replace("%piece%", getPieceName()));
		if (module != null) {
			ModuleModel[] models = module.getModels(stack);
			if (models.length >= 1) {
				if (models[models.length - 1] != null) {
					String key = models[0].location.toString();
					IVertexBuilder builder;
					if (key.equals("tetric_additions:items/module/armor/helmet/special/cactus_base")) {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/cactus.png"
										)
								)
						);
					} else if (key.equals("tetric_additions:items/module/armor/helmet/special/prismarine_base")) {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/prismarine.png"
										)
								)
						);
					} else {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/normal.png"
										)
								)
						);
					}
					int i = models[models.length - 1].tint;
					float f = (float) (i >> 16 & 255) / 255.0F;
					float f1 = (float) (i >> 8 & 255) / 255.0F;
					float f2 = (float) (i & 255) / 255.0F;
					BipedModel model = null;
					if (entity instanceof PlayerEntity || entity instanceof ArmorStandEntity)
						model = HelmetRenderer.playerModel;
					else if (entity instanceof SkeletonEntity)
						model = HelmetRenderer.zombieModel;
					else if (entity instanceof ZombieEntity)
						model = HelmetRenderer.zombieModel;
					model.bipedHead.render(
							matrixStack, builder, packedLightIn, OverlayTexture.NO_OVERLAY,
							f, f1, f2, 1
					);
				}
			}
		}
		module = item.getModuleFromSlot(stack, plate1Key.replace("%piece%", getPieceName()));
		if (module != null) {
			ModuleModel[] models = module.getModels(stack);
			if (models.length >= 1) {
				if (models[models.length - 1] != null) {
					String key = models[0].location.toString();
					IVertexBuilder builder;
					if (key.equals("tetric_additions:items/module/armor/helmet/special/cactus_plate1")) {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/cactus.png"
										)
								)
						);
					} else if (key.equals("tetric_additions:items/module/armor/helmet/special/prismarine_plate1")) {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/prismarine.png"
										)
								)
						);
					} else {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/normal.png"
										)
								)
						);
					}
					int i = models[models.length - 1].tint;
					float f = (float) (i >> 16 & 255) / 255.0F;
					float f1 = (float) (i >> 8 & 255) / 255.0F;
					float f2 = (float) (i & 255) / 255.0F;
					BipedModel model = null;
					if (entity instanceof PlayerEntity || entity instanceof ArmorStandEntity)
						model = HelmetRenderer.playerModel;
					else if (entity instanceof SkeletonEntity)
						model = HelmetRenderer.zombieModel;
					else if (entity instanceof ZombieEntity)
						model = HelmetRenderer.zombieModel;
					matrixStack.push();
					matrixStack.scale(0.91f, 0.91f, 0.91f);
					model.bipedHeadwear.render(
							matrixStack, builder, packedLightIn, OverlayTexture.NO_OVERLAY,
							f, f1, f2, 1
					);
					matrixStack.pop();
				}
			}
		}
		module = item.getModuleFromSlot(stack, plate2Key.replace("%piece%", getPieceName()));
		if (module != null) {
			ModuleModel[] models = module.getModels(stack);
			if (models.length >= 1) {
				if (models[models.length - 1] != null) {
					String key = models[0].location.toString();
					IVertexBuilder builder;
					if (key.equals("tetric_additions:items/module/armor/helmet/special/cactus_plate2")) {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/cactus_layer_2.png"
										)
								)
						);
					} else if (key.equals("tetric_additions:items/module/armor/helmet/special/prismarine_plate2")) {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/prismarine_layer_2.png"
										)
								)
						);
					} else {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/normal_layer_2.png"
										)
								)
						);
					}
					int i = models[models.length - 1].tint;
					float f = (float) (i >> 16 & 255) / 255.0F;
					float f1 = (float) (i >> 8 & 255) / 255.0F;
					float f2 = (float) (i & 255) / 255.0F;
					BipedModel model = null;
					if (entity instanceof PlayerEntity || entity instanceof ArmorStandEntity)
						model = HelmetRenderer.playerModel;
					else if (entity instanceof SkeletonEntity)
						model = HelmetRenderer.zombieModel;
					else if (entity instanceof ZombieEntity)
						model = HelmetRenderer.zombieModel;
					matrixStack.push();
					matrixStack.scale(0.95f, 0.95f, 0.95f);
					model.bipedHeadwear.render(
							matrixStack, builder, packedLightIn, OverlayTexture.NO_OVERLAY,
							f, f1, f2, 1
					);
					matrixStack.pop();
				}
			}
		}
	}
	
	@Override
	public ITextComponent getDisplayName(ItemStack itemStack) {
		ItemModule module = ((ModularArmorItem) itemStack.getItem()).getModuleFromSlot(itemStack, headPieceKey.replace("%piece%", getPieceName()));
		if (module != null) {
//			System.out.println();
			String registryName = module.getItemPrefix(itemStack);
			if (registryName != null) {
				Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
				return item.getName().appendText(" Helmet");
			} else {
				String name = module.getName(itemStack).substring("armor/helmet/base/armor/helmet/b".length());
				if (name.equals("gold")) {
					name += "en";
				}
				name = name.toUpperCase().substring(0, 1) + name.substring(1);
				if (name.equals("Leather")) {
					return new StringTextComponent(name + " Cap");
				} else {
					return new StringTextComponent(name + " Helmet");
				}
			}
		}
		return super.getDisplayName(itemStack);
	}
}
