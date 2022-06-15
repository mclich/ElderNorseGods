package com.github.mclich.engmod.item.staff;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class HealingStaffItem extends ReversibleStaffItem
{
	public static final String ID="healing_staff";
	
	public HealingStaffItem()
	{
		super(Rarity.COMMON, 0xF266A0, 200, 5, 7F, 3F, 1.5F);
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack staffStack, ItemStack repairStack)
	{
		return false;
	}
	
	@Override
	public void applyEffect(Level world, LivingEntity entity, ItemStack itemStack)
	{
		entity.heal(this.effectToApply);
	}
}