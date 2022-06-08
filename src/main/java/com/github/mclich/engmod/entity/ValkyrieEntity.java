package com.github.mclich.engmod.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap.MutableAttribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ValkyrieEntity extends MonsterEntity
{
	public static final String ID="valkyrie";
	public static final String SPAWN_EGG_ID="valkyrie_spawn_egg";
	
	//private static final Ingredient TEMPTATION_ITEMS=Ingredient.of(Items.CARROT);
	
	public ValkyrieEntity(EntityType<? extends MonsterEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD, 1));
		this.moveControl=new FlyingMovementController(this, 10, true);
		this.navigation=new FlyingPathNavigator(this, worldIn);
	}
	
	public static MutableAttribute createAttributes()
	{
		return MonsterEntity.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 60D)
			.add(Attributes.MOVEMENT_SPEED, 0.35)
			.add(Attributes.ATTACK_DAMAGE, 10D)
			.add(Attributes.FOLLOW_RANGE, 32D)
			.add(Attributes.FLYING_SPEED, 1D)
			.add(Attributes.ARMOR, 16D);
	}

	@Override
	public void registerGoals()
	{
		int i=0;
		this.goalSelector.addGoal(i++, new SwimGoal(this));
		this.goalSelector.addGoal(i++, new PanicGoal(this, 1.25D));
		//this.goalSelector.addGoal(i++, new TemptGoal(this, 1.25D, ValkyrieEntity.TEMPTATION_ITEMS, false));
		this.goalSelector.addGoal(i++, new WaterAvoidingRandomWalkingGoal(this, 0.8));
		this.goalSelector.addGoal(i++, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(i, new LookRandomlyGoal(this));
	}
	
	@Override
	public int getExperienceReward(PlayerEntity player)
	{
		return 15;
	}

	@Override
	public SoundEvent getAmbientSound()
	{
		return SoundEvents.STRAY_AMBIENT;
	}

	@Override
	public SoundEvent getDeathSound()
	{
		return SoundEvents.HOSTILE_DEATH;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.HOSTILE_HURT;
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState blockOn)
	{
		this.playSound(SoundEvents.NETHER_SPROUTS_STEP, 0.15F, 1F);
	}

	@Override
	public boolean shouldDespawnInPeaceful()
	{
		return false;
	}
}