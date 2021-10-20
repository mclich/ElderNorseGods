package com.github.mclich.engmod.util;

import java.util.Collections;
import com.github.mclich.engmod.util.ENGItems.EggHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public abstract class ENGTabs
{
	private static final int DELAY=3*20;
	
	public static final ItemGroup COMBAT=new ItemGroup("engmod_combat_tab")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ENGItems.CUSTOM_SWORD.get());
		}
	};
	
	public static final ItemGroup MATERIALS=new ItemGroup("engmod_materials_tab")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ENGItems.CUSTOM_MATERIAL.get());
		}
	};
	
	public static final ItemGroup BLOCKS=new ItemGroup("engmod_blocks_tab")
	{
		private int counter=-1;
		
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ENGItems.BREWERY_ITEM.get());
		}
		
		@Override
		public ItemStack getIconItem()
		{
			counter++;
			if(counter>=ENGTabs.DELAY*3) counter=0;
			if(counter>=0&&counter<ENGTabs.DELAY) return new ItemStack(ENGItems.BREWERY_ITEM.get());
			else if(counter>=ENGTabs.DELAY&&counter<ENGTabs.DELAY*2) return new ItemStack(ENGItems.BARLEY_HAY_BLOCK_ITEM.get());
			else if(counter>=ENGTabs.DELAY*2&&counter<ENGTabs.DELAY*3) return new ItemStack(ENGItems.CUSTOM_BLOCK_ITEM.get());
			return this.makeIcon();
		}
		
		/*
		@Override
		public void fillItemList(NonNullList<ItemStack> items)
		{
			super.fillItemList(items);
			Collections.sort(items, (itemStack1, itemStack2)->Float.compare(((BlockItem)itemStack1.getItem()).getBlock().getStateForPlacement(null).getDestroySpeed(null, null), ((BlockItem)itemStack2.getItem()).getBlock().getStateForPlacement(null).getDestroySpeed(null, null)));
		}
		*/
	};
	
	public static final ItemGroup MISC=new ItemGroup("engmod_misc_tab")
	{
		private int counter=-1;
		
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ENGItems.BARLEY_SEEDS.get());
		}
		
		@Override
		public ItemStack getIconItem()
		{
			counter++;
			if(counter>=ENGTabs.DELAY*5) counter=0;
			if(counter>=0&&counter<ENGTabs.DELAY) return new ItemStack(ENGItems.BARLEY.get());
			else if(counter>=ENGTabs.DELAY&&counter<ENGTabs.DELAY*2) return new ItemStack(ENGItems.BARLEY_SEEDS.get());
			else if(counter>=ENGTabs.DELAY*2&&counter<ENGTabs.DELAY*3) return new ItemStack(ENGItems.HOP.get());
			else if(counter>=ENGTabs.DELAY*3&&counter<ENGTabs.DELAY*4) return new ItemStack(ENGItems.MALT.get());
			else if(counter>=ENGTabs.DELAY*4&&counter<ENGTabs.DELAY*5) return new ItemStack(EggHandler.VALKYRIE_SPAWN_EGG);
			return this.makeIcon();
		}
	};
	
	public static final ItemGroup FOOD=new ItemGroup("engmod_food_tab")
	{
		private int counter=-1;
		
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ENGItems.BARLEY_BREAD.get());
		}
		
		@Override
		public ItemStack getIconItem()
		{
			counter++;
			if(counter>=ENGTabs.DELAY*2) counter=0;
			if(counter>=0&&counter<ENGTabs.DELAY) return new ItemStack(ENGItems.BARLEY_BREAD.get());
			else if(counter>=ENGTabs.DELAY&&counter<ENGTabs.DELAY*2) return new ItemStack(ENGItems.BEER.get());
			return this.makeIcon();
		}

		@Override
		public void fillItemList(NonNullList<ItemStack> items)
		{
			super.fillItemList(items);
			Collections.sort(items, (itemStack1, itemStack2)->Integer.compare(itemStack2.getItem().getFoodProperties().getNutrition(), itemStack1.getItem().getFoodProperties().getNutrition()));
		}
	};
}