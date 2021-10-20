package com.github.mclich.engmod.item;

import com.github.mclich.engmod.util.ENGBlocks;
import com.github.mclich.engmod.util.ENGTabs;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;

public class HopItem extends BlockNamedItem
{
	public static final String ID="hop";
	
	public HopItem()
	{
		super(ENGBlocks.HOP_BUSH.get(), new Item.Properties().tab(ENGTabs.MISC));
	}
}