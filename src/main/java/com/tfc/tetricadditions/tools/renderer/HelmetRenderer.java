package com.tfc.tetricadditions.tools.renderer;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;

public class HelmetRenderer {
	public static final PlayerModel<PlayerEntity> playerModel = new PlayerModel<>(1, false);
	public static final SkeletonModel<SkeletonEntity> skeletonModel = new SkeletonModel<>(1, false);
	public static final ZombieModel<ZombieEntity> zombieModel = new ZombieModel<>(1, false);
}
