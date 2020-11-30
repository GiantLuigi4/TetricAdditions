package com.tfc.tetricadditions.tools;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tfc.tetricadditions.tools.renderer.RendererCache;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
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

public class ModularBootsItem extends ModularArmorItem {
	public static final String unlocalizedName = "modular_boots";
	
	public static final String bootPieceKey = "armor/boots/base";
	
	public ModularBootsItem(Properties properties) {
		super(properties, EquipmentSlotType.FEET);
		
		majorModuleKeys = new String[]{bootPieceKey.replace("%piece%", getPieceName()), plate1Key.replace("%piece%", getPieceName())};
		minorModuleKeys = new String[]{
				plate2Key.replace("%piece%", getPieceName()),
				bindingKey.replace("%piece%", getPieceName()),
				socketKey.replace("%piece%", getPieceName()),
//				plate2Key.replace("%piece%", getPieceName())+"_dye",
//				bindingKey.replace("%piece%", getPieceName())+"_dye",
//				socketKey.replace("%piece%", getPieceName())+"_dye"
		};
		
		requiredModules = new String[]{bootPieceKey.replace("%piece%", getPieceName())};
		
		setRegistryName(unlocalizedName);
		
		honeBase = 100;
		
		SchemaRegistry.instance.registerSchema(new RepairSchema(this));
		RemoveSchema.registerRemoveSchemas(this);
	}
	
	@Override
	public ITextComponent getDisplayName(ItemStack itemStack) {
		ItemModule module = ((ModularArmorItem) itemStack.getItem()).getModuleFromSlot(itemStack, bootPieceKey.replace("%piece%", getPieceName()));
		if (module != null) {
//			System.out.println();
			String registryName = module.getItemPrefix(itemStack);
			if (registryName != null) {
				Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
				return item.getName().appendText(" Helmet");
			} else {
				String name = module.getName(itemStack).substring("armor/boots/base/armor/boots/ba".length());
				
				if (name.equals("gold"))
					name += "en";
				
				name = name.toUpperCase().substring(0, 1) + name.substring(1);
				
				return new StringTextComponent(name + " Boots");
			}
		}
		return super.getDisplayName(itemStack);
	}
	
	@Nullable
	@Override
	@OnlyIn(Dist.CLIENT)
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
		return (A) RendererCache.getModelForEntity(entity);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(IRenderTypeBuffer buffer, ItemStack stack, LivingEntity entity, MatrixStack matrixStack, int packedLightIn) {
		super.render(buffer, stack, entity, matrixStack, packedLightIn);
		if (RendererCache.getModelForEntity(entity) == null) return;
		matrixStack.push();
//		matrixStack.scale(0.9f, 0.9f, 0.9f);
//		matrixStack.translate(0,0.25f,0);
		ModularArmorItem item = ((ModularArmorItem) stack.getItem());
		ItemModule module = item.getModuleFromSlot(stack, bootPieceKey.replace("%piece%", getPieceName()));
		if (module != null) {
			ModuleModel[] models = module.getModels(stack);
			if (models.length >= 1) {
				if (models[models.length - 1] != null) {
					String key = models[0].location.toString();
					IVertexBuilder builder;
					if (key.equals("tetric_additions:items/module/armor/boots/special/cactus_base")) {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/cactus.png"
										)
								)
						);
					} else if (key.equals("tetric_additions:items/module/armor/boots/special/prismarine_base")) {
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
					BipedModel model = RendererCache.getModelForEntity(entity);
					model.bipedLeftLeg.render(
							matrixStack, builder, packedLightIn, OverlayTexture.NO_OVERLAY,
							f, f1, f2, 1
					);
					model.bipedRightLeg.render(
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
					if (key.equals("tetric_additions:items/module/armor/boots/special/cactus_plate1")) {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/cactus_layer_2.png"
										)
								)
						);
					} else if (key.equals("tetric_additions:items/module/armor/boots/special/prismarine_plate1")) {
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
					BipedModel model = RendererCache.getModelForEntity(entity);
					matrixStack.push();
					model.bipedLeftLeg.render(
							matrixStack, builder, packedLightIn, OverlayTexture.NO_OVERLAY,
							f, f1, f2, 1
					);
					model.bipedRightLeg.render(
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
					if (key.equals("tetric_additions:items/module/armor/boots/special/cactus_plate2")) {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/cactus_layer_3.png"
										)
								)
						);
					} else if (key.equals("tetric_additions:items/module/armor/boots/special/prismarine_plate2")) {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/prismarine_layer_3.png"
										)
								)
						);
					} else {
						builder = buffer.getBuffer(
								RenderType.getEntityCutoutNoCull(
										new ResourceLocation(
												"tetric_additions:textures/models/armor/normal_layer_3.png"
										)
								)
						);
					}
					int i = models[models.length - 1].tint;
					float f = (float) (i >> 16 & 255) / 255.0F;
					float f1 = (float) (i >> 8 & 255) / 255.0F;
					float f2 = (float) (i & 255) / 255.0F;
					BipedModel model = RendererCache.getModelForEntity(entity);
					matrixStack.push();
//					matrixStack.scale(1.01f, 1.01f, 1.01f);
					model.bipedLeftLeg.render(
							matrixStack, builder, packedLightIn, OverlayTexture.NO_OVERLAY,
							f, f1, f2, 1
					);
					model.bipedRightLeg.render(
							matrixStack, builder, packedLightIn, OverlayTexture.NO_OVERLAY,
							f, f1, f2, 1
					);
					matrixStack.pop();
				}
			}
		}
		matrixStack.pop();
	}
}
