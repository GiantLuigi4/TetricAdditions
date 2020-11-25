package com.tfc.tetricadditions;

import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;

import java.util.HashMap;

public class Client {
	//TODO: make this not a hacky work around to forge not having an armor thing for not armor items
	public static void onRenderEntity(RenderLivingEvent<?, ?> event) {
		if (event.getRenderer().getEntityModel() instanceof BipedModel) {
			if (event instanceof RenderLivingEvent.Post) {
				BipedModel model = (BipedModel) event.getRenderer().getEntityModel();
				event.getMatrixStack().push();
//				event.getMatrixStack().translate(0,event.getEntity().getEyeHeight(Pose.STANDING)-0.2667773,0);
				if (event.getEntity().getPose() != Pose.CROUCHING) {
					event.getMatrixStack().translate(0, event.getEntity().getSize(event.getEntity().getPose()).height, 0);
				} else {
					event.getMatrixStack().translate(0, event.getEntity().getSize(Pose.STANDING).height, 0);
				}
//				model.bipedHeadwear.rotateAngleY = 0;
//				model.bipedHeadwear.translateRotate(event.getMatrixStack());
				event.getMatrixStack().rotate(
						new Quaternion(
//								-(float)Math.toDegrees(model.bipedHeadwear.rotateAngleX),
								0,
								-MathHelper.lerp(event.getPartialRenderTick(), event.getEntity().prevRotationYawHead, event.getEntity().rotationYawHead) + 180,
//								-model.bipedHeadwear.rotateAngleY,
								0,
								true
						)
				);
				if (event.getEntity().getPose() == Pose.FALL_FLYING) {
					event.getMatrixStack().rotate(new Quaternion(
							0,
							0,
							-event.getEntity().getPitch(event.getPartialRenderTick()),
							true
					));
				}
				event.getMatrixStack().scale(-1, -1, 1);
				event.getMatrixStack().translate(0, 0.4667773, 0);
				event.getMatrixStack().scale(1.11111f, 1.11111f, 1.11111f);
				HashMap<ModelRenderer, Boolean> isShow = new HashMap<>();
				
				ModelRenderer[] parts = new ModelRenderer[]{
						model.bipedHead,
						model.bipedBody,
						model.bipedLeftArm,
						model.bipedLeftLeg,
						model.bipedRightArm,
						model.bipedRightLeg,
				};
				for (ModelRenderer part : parts) {
					isShow.put(part, part.showModel);
					part.showModel = false;
				}
				
				model.isSneak = false;
//				model.bipedHeadwear.rotateAngleX = 0;
				model.bipedHeadwear.rotateAngleY = 0;
//				model.bipedHeadwear.rotateAngleZ = 0;
				model.bipedHeadwear.render(
//				model.render(
						event.getMatrixStack(),
						event.getBuffers().getBuffer(
								RenderType.getEntityCutout(
										new ResourceLocation("tetric_additions:textures/models/armor/cactus.png")
								)
						),
						event.getLight(),
						OverlayTexture.NO_OVERLAY,
						1, 1, 1, 1
				);
				
				isShow.forEach((part, val) -> part.showModel = val);
				
				event.getMatrixStack().pop();
			}
		}
	}
}
