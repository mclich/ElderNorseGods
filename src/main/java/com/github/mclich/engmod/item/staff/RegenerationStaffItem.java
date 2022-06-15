package com.github.mclich.engmod.item.staff;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class RegenerationStaffItem extends PermanentStaffItem
{
	public static final String ID="regeneration_staff";

	public RegenerationStaffItem()
	{
		super(Rarity.COMMON, 0x6679F2, 200, 5, 15);
	}

	@Override
	public boolean isValidRepairItem(ItemStack staffStack, ItemStack repairStack)
	{
		return false;
	}
	
	@Override
	public void applyEffect(Level world, LivingEntity entity, ItemStack itemStack)
	{
		entity.heal(1F);
	}
}