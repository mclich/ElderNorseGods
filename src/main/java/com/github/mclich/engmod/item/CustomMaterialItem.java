package com.github.mclich.engmod.item;

import com.github.mclich.engmod.register.ENGTabs;

import net.minecraft.item.Item;

public class CustomMaterialItem extends Item
{
	public static final String ID="custom_material";
	
	public CustomMaterialItem()
	{
		super(new Item.Properties().tab(ENGTabs.MATERIALS));
		
	}
}