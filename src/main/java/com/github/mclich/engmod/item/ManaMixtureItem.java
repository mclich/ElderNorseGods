package com.github.mclich.engmod.item;

import com.github.mclich.engmod.data.capability.ManaCapability;
import com.github.mclich.engmod.effect.ReplenishmentEffect;
import com.github.mclich.engmod.register.ENGTabs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;

public class ManaMixtureItem extends Item
{
	public static final String ID="mana_mixture";
	
	public ManaMixtureItem()
	{
		super(new Item.Properties().food(new Food.Builder().nutrition(2).saturationMod(0.6F).alwaysEat().effect(()->ReplenishmentEffect.getActivationInstance(), 1F).build()).stacksTo(1).rarity(Rarity.RARE).tab(ENGTabs.FOOD));
	}
	
	@Override
	public boolean isFoil(ItemStack itemStack)
	{
		return true;
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, World world, LivingEntity entity)
	{
		itemStack=entity.eat(world, itemStack);
		if(entity instanceof PlayerEntity)
		{
			PlayerEntity player=(PlayerEntity)entity;
			player.getCapability(ManaCapability.CAP_INSTANCE).ifPresent(mana->mana.setStatus(true));
			if(!player.abilities.instabuild)
			{
				if(itemStack.getCount()==0&&!player.inventory.contains(new ItemStack(Items.BOWL)))
				{
					player.inventory.removeItem(itemStack);
					player.inventory.add(player.inventory.selected, new ItemStack(Items.BOWL));
				}
				else player.inventory.add(new ItemStack(Items.BOWL));
			}
		}
		return itemStack;
	}
}