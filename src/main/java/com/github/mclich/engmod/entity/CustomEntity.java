package com.github.mclich.engmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;

public class CustomEntity extends MonsterEntity
{
	public static final String ID="custom_entity";
	public CustomEntity(EntityType<? extends MonsterEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
}
