package com.github.mclich.engmod.item;

import com.github.mclich.engmod.effect.ReplenishmentEffect;
import com.github.mclich.engmod.register.ENGCapabilities;
import com.github.mclich.engmod.register.ENGTabs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class ManaMixtureItem extends Item
{
	public static final String ID="mana_mixture";
	
	public ManaMixtureItem()
	{
		super(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).alwaysEat().effect(ReplenishmentEffect::getActivationInstance, 1F).build()).stacksTo(1).rarity(Rarity.RARE).tab(ENGTabs.FOOD));
	}
	
	@Override
	public boolean isFoil(ItemStack itemStack)
	{
		return true;
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, Level world, LivingEntity entity)
	{
		itemStack=entity.eat(world, itemStack);
		if(entity instanceof Player player)
		{
			player.getCapability(ENGCapabilities.MANA).ifPresent(mana->mana.setStatus(true));
			if(!player.getAbilities().instabuild)
			{
				if(itemStack.getCount()==0&&!player.getInventory().contains(new ItemStack(Items.BOWL)))
				{
					player.getInventory().removeItem(itemStack);
					player.getInventory().add(player.getInventory().selected, new ItemStack(Items.BOWL));
				}
				else player.getInventory().add(new ItemStack(Items.BOWL));
			}
		}
		return itemStack;
	}
}