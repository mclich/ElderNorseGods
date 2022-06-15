package com.github.mclich.engmod.entity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class ValkyrieEntity extends Monster
{
	public static final String ID="valkyrie";
	public static final String SPAWN_EGG_ID="valkyrie_spawn_egg";
	
	//private static final Ingredient TEMPTATION_ITEMS=Ingredient.of(Items.CARROT);
	
	public ValkyrieEntity(EntityType<? extends Monster> type, Level world)
	{
		super(type, world);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD, 1));
		this.moveControl=new FlyingMoveControl(this, 10, true);
		this.navigation=new FlyingPathNavigation(this, world);
	}
	
	public static Builder createAttributes()
	{
		return Monster.createMonsterAttributes()
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
		this.goalSelector.addGoal(i++, new FloatGoal(this));
		this.goalSelector.addGoal(i++, new PanicGoal(this, 1.25D));
		//this.goalSelector.addGoal(i++, new TemptGoal(this, 1.25D, ValkyrieEntity.TEMPTATION_ITEMS, false));
		this.goalSelector.addGoal(i++, new WaterAvoidingRandomStrollGoal(this, 0.8));
		this.goalSelector.addGoal(i++, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(i, new RandomLookAroundGoal(this));
	}
	
	@Override
	public int getExperienceReward(Player player)
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
	public void playStepSound(BlockPos blockPos, BlockState blockOn)
	{
		this.playSound(SoundEvents.NETHER_SPROUTS_STEP, 0.15F, 1F);
	}

	@Override
	public boolean shouldDespawnInPeaceful()
	{
		return false;
	}
}