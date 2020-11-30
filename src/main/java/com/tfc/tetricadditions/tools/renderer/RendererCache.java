package com.tfc.tetricadditions.tools.renderer;

import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;

public class RendererCache {
	public static final PlayerModel<PlayerEntity> playerModel = new PlayerModel<>(1, false);
	public static final SkeletonModel<SkeletonEntity> skeletonModel = new SkeletonModel<>(1, false);
	public static final ZombieModel<ZombieEntity> zombieModel = new ZombieModel<>(1, false);
	public static final ArmorStandArmorModel armorStandModel = new ArmorStandArmorModel(1, 64, 64);
	
	static {
		playerModel.textureWidth = 64;
		playerModel.textureHeight = 64;
		
		skeletonModel.textureWidth = 32;
		skeletonModel.textureHeight = 64;
		
		zombieModel.textureWidth = 64;
		zombieModel.textureHeight = 64;
		
		armorStandModel.textureWidth = 32;
		armorStandModel.textureHeight = 64;
	}
	
	public static BipedModel<?> getModelForEntity(LivingEntity entity) {
		if (entity == null) return null;
		BipedModel<?> model = null;
		if (entity instanceof PlayerEntity)
			model = RendererCache.playerModel;
		else if (entity instanceof SkeletonEntity)
			model = RendererCache.playerModel;
		else if (entity instanceof ZombieEntity)
			model = RendererCache.zombieModel;
		else if (entity instanceof ArmorStandEntity)
			model = RendererCache.armorStandModel;
		return model;
	}
}
