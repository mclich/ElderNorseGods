package com.github.mclich.engmod.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ValkyrieEntity extends MonsterEntity
{
	public static final String ID="valkyrie_entity";
	public static final String SPAWN_EGG_ID="valkyrie_spawn_egg";
	private static final Ingredient TEMPTATION_ITEMS=Ingredient.of(Items.CARROT);
	public ValkyrieEntity(EntityType<? extends MonsterEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD, 1));
		this.moveControl=new FlyingMovementController(this, 10, true);
		this.navigation=new FlyingPathNavigator(this, worldIn);
	}
	
	public static AttributeModifierMap.MutableAttribute createAttributes()
	{
		return MonsterEntity.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 60.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.35D)
			.add(Attributes.ATTACK_DAMAGE, 10.0D)
			.add(Attributes.FOLLOW_RANGE, 32.0D)
			.add(Attributes.FLYING_SPEED, 1.0D)
			.add(Attributes.ARMOR, 16.0D);
	}

	@Override
	public void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
		this.goalSelector.addGoal(2, new TemptGoal(this, 1.25D, ValkyrieEntity.TEMPTATION_ITEMS, false));
		this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 0.8));
		this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
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
		return SoundEvents.BEACON_DEACTIVATE;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource damageSource)
	{
		return super.getHurtSound(damageSource);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockOn)
	{
		this.playSound(SoundEvents.NETHER_SPROUTS_STEP, 0.15F, 1.0F);
	}
}