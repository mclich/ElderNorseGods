package com.github.mclich.engmod.item.staff;

import com.github.mclich.engmod.register.ENGItemTiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HealingStaffItem extends ReversibleStaffItem
{
	public static final String ID="healing_staff";
	
	public HealingStaffItem()
	{
		super(ENGItemTiers.NOVICE_STAFF_TIER, 5, 7F, 3F, 1.5F);
	}
	
	@Override
	public void applyEffect(Level world, LivingEntity entity, ItemStack itemStack)
	{
		entity.heal(this.effectToApply);
	}
}