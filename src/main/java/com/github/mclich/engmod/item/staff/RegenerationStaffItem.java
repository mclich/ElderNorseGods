package com.github.mclich.engmod.item.staff;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;

public class RegenerationStaffItem extends ReversibleStaffItem
{
	public static final String ID="regeneration_staff";
	
	public RegenerationStaffItem()
	{
		super(Rarity.COMMON, 200, 7F, 3F, 1.5F);
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack)
	{
		return false;
	}
	
	@Override
	public void applyEffect(World world, LivingEntity entity, ItemStack itemStack)
	{
		entity.heal(this.effectToApply);
	}
}