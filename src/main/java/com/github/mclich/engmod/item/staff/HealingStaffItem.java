package com.github.mclich.engmod.item.staff;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;

public class HealingStaffItem extends PermanentStaffItem
{
	public static final String ID="healing_staff";

	public HealingStaffItem()
	{
		super(Rarity.COMMON, 200, 5, 15);
	}

	@Override
	public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack)
	{
		return false;
	}
	
	@Override
	public void applyEffect(World world, LivingEntity entity, ItemStack itemStack)
	{
		entity.heal(1F);
	}
}