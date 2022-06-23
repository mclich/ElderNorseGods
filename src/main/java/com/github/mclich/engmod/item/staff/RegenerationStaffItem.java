package com.github.mclich.engmod.item.staff;

import com.github.mclich.engmod.register.ENGItemTiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RegenerationStaffItem extends PermanentStaffItem
{
	public static final String ID="regeneration_staff";

	public RegenerationStaffItem()
	{
		super(ENGItemTiers.NOVICE_STAFF_TIER, 5, 15);
	}
	
	@Override
	public void applyEffect(Level world, LivingEntity entity, ItemStack itemStack)
	{
		entity.heal(1F);
	}
}