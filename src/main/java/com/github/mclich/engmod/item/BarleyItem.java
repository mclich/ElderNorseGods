package com.github.mclich.engmod.item;

import com.github.mclich.engmod.util.ENGTabs;
import net.minecraft.item.Item;

public class BarleyItem extends Item
{
	public static final String ID="barley";
	
	public BarleyItem()
	{
		super(new Item.Properties().tab(ENGTabs.MISC));
	}
}