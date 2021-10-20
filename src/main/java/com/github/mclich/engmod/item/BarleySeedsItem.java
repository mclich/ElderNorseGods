package com.github.mclich.engmod.item;

import com.github.mclich.engmod.util.ENGBlocks;
import com.github.mclich.engmod.util.ENGTabs;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;

public class BarleySeedsItem extends BlockNamedItem
{
	public static final String ID="barley_seeds";
	
	public BarleySeedsItem()
	{
		super(ENGBlocks.BARLEY_CROP.get(), new Item.Properties().tab(ENGTabs.MISC));
	}
}