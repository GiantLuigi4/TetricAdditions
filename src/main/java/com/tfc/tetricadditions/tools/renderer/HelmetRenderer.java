package com.tfc.tetricadditions.tools.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;

public class HelmetRenderer extends BipedModel<PlayerEntity> {
	public HelmetRenderer(float modelSize) {
		super(modelSize);
	}
	
	public static final PlayerModel<PlayerEntity> playerModel = new PlayerModel<>(1,false);
	public static final SkeletonModel<SkeletonEntity> skeletonModel = new SkeletonModel<>(1,false);
	public static final ZombieModel<ZombieEntity> zombieModel = new ZombieModel<>(1,false);
	
	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		
		System.out.println("called renderer");
	}
}
