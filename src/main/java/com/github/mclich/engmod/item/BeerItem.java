package com.github.mclich.engmod.item;

import com.github.mclich.engmod.effect.DrunkennessEffect;
import com.github.mclich.engmod.util.ENGTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class BeerItem extends Item
{
	public static final String ID="beer";
	
	public BeerItem()
	{
		super(new Item.Properties().food(new Food.Builder().nutrition(4).saturationMod(0.6F).effect(()->DrunkennessEffect.getInstance(), 1F).build()).tab(ENGTabs.FOOD));
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, World world, LivingEntity entity)
	{
		itemStack=entity.eat(world, itemStack);
		PlayerEntity player=entity instanceof PlayerEntity?(PlayerEntity)entity:null;
		if(player!=null&&Minecraft.getInstance().gameMode.getPlayerMode().isSurvival())
		{
			if(itemStack.getCount()==0&&!player.inventory.contains(new ItemStack(Items.GLASS_BOTTLE)))
			{
				player.inventory.removeItem(itemStack);
				player.inventory.add(player.inventory.selected, new ItemStack(Items.GLASS_BOTTLE));
			}
			else player.inventory.add(new ItemStack(Items.GLASS_BOTTLE));
		}
		return itemStack;
	}

	@Override
	public int getUseDuration(ItemStack itemStack)
	{
		return 32;
	}

	@Override
	public SoundEvent getDrinkingSound()
	{
		return SoundEvents.GENERIC_DRINK;
	}
	
	@Override
	public SoundEvent getEatingSound()
	{
		return null;
	}
	
	@Override
	public UseAction getUseAnimation(ItemStack itemStack)
	{
		return UseAction.DRINK;
	}
}