package com.github.mclich.engmod.item;

import com.github.mclich.engmod.effect.DrunkennessEffect;
import com.github.mclich.engmod.register.ENGTabs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

public class BeerItem extends Item
{
	public static final String ID="beer";
	
	public BeerItem()
	{
		super(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.6F).effect(DrunkennessEffect::getInstance, 1F).build()).tab(ENGTabs.FOOD));
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, Level world, LivingEntity entity)
	{
		itemStack=entity.eat(world, itemStack);
		if(entity instanceof Player player)
		{
			if(!player.getAbilities().instabuild)
			{
				if(itemStack.getCount()==0&&!player.getInventory().contains(new ItemStack(Items.GLASS_BOTTLE)))
				{
					player.getInventory().removeItem(itemStack);
					player.getInventory().add(player.getInventory().selected, new ItemStack(Items.GLASS_BOTTLE));
				}
				else player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
			}
		}
		return itemStack;
	}
	
	@Override
	public SoundEvent getEatingSound()
	{
		return null;
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack itemStack)
	{
		return UseAnim.DRINK;
	}
}