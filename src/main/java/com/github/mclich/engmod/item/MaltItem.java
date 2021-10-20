package com.github.mclich.engmod.item;

import com.github.mclich.engmod.util.ENGTabs;
import net.minecraft.item.Item;

public class MaltItem extends Item
{
	public static final String ID="malt";
	
	public MaltItem()
	{
		super(new Item.Properties().tab(ENGTabs.MISC));
	}
}