package com.github.mclich.engmod.item;

import com.github.mclich.engmod.util.ENGTabs;
import net.minecraft.item.Food;
import net.minecraft.item.Item;

public class BarleyBreadItem extends Item
{
	public static final String ID="barley_bread";
	
	public BarleyBreadItem()
	{
		super(new Item.Properties().food(new Food.Builder().nutrition(5).saturationMod(0.6F).build()).tab(ENGTabs.FOOD));
	}
}