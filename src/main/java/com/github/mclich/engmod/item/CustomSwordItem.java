package com.github.mclich.engmod.item;

import com.github.mclich.engmod.register.ENGTabs;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;

public class CustomSwordItem extends SwordItem
{
	public static final String ID="custom_sword";
	
	public CustomSwordItem()
	{
		super
		(
			new Tier()
			{
				@Override
				public int getUses() {return 3000;}
	
				@Override
				public float getSpeed() {return 10.0F;}
	
				@Override
				public float getAttackDamageBonus() {return 3.0F;}
	
				@Override
				public int getLevel() {return 5;}
	
				@Override
				public int getEnchantmentValue() {return 16;}
	
				@Override
				public Ingredient getRepairIngredient() {return Ingredient.of(Items.DIAMOND);}
			},
			6, -2.4F, new Item.Properties().tab(ENGTabs.COMBAT)
		);
	}

}
