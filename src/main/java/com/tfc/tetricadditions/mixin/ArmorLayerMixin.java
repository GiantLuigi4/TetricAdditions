package com.tfc.tetricadditions.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tfc.tetricadditions.tools.ModularArmorItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.ArmorLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ArmorLayer.class)
public abstract class ArmorLayerMixin<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> {
	@Shadow
	@Final
	protected A modelArmor;
	
	@Inject(at = @At("HEAD"), method = "renderArmorPart(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;Lnet/minecraft/entity/LivingEntity;FFFFFFLnet/minecraft/inventory/EquipmentSlotType;I)V")
	public void renderArmor(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, EquipmentSlotType slotIn, int packedLightIn, CallbackInfo ci) {
		ItemStack itemstack = entityLivingBaseIn.getItemStackFromSlot(slotIn);
		if (itemstack.getItem() instanceof ModularArmorItem) {
			ModularArmorItem armoritem = (ModularArmorItem) itemstack.getItem();
			if (armoritem.getEquipmentSlot(itemstack) == slotIn) {
				A a = armoritem.getArmorModel(entityLivingBaseIn, itemstack, slotIn, modelArmor);
				a = getArmorModelHook(entityLivingBaseIn, itemstack, slotIn, a);
				if (a != null) {
					((BipedModel) ((LayerRenderer<Entity, EntityModel<Entity>>) (Object) this).getEntityModel()).setModelAttributes(a);
					a.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
					this.setModelSlotVisible(a, slotIn);
					a.setRotationAngles(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
					armoritem.render(bufferIn, itemstack, entityLivingBaseIn, matrixStackIn, packedLightIn);
				}
			}
			if (ci.isCancellable()) {
				ci.cancel();
			}
		}
	}
	
	@Shadow
	@Deprecated
	protected abstract ResourceLocation getArmorResource(ArmorItem armor, boolean legSlotIn, @Nullable String suffixOverlayIn);
	
	@Shadow
	protected abstract A getArmorModelHook(T entity, ItemStack itemStack, EquipmentSlotType slot, A model);
	
	@Shadow
	protected abstract void setModelSlotVisible(A modelIn, EquipmentSlotType slotIn);
}
