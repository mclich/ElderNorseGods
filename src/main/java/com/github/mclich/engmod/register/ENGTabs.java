package com.github.mclich.engmod.register;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import com.github.mclich.engmod.register.ENGItems.EggHelper;
import net.minecraftforge.fml.RegistryObject;

@SuppressWarnings("unused")
public abstract class ENGTabs
{
	private static final Comparator<ItemStack> NAME_COMPARATOR=Comparator.comparing(is->is.getItem().getDescriptionId());
	private static final Comparator<ItemStack> FOOD_COMPARATOR=(is1, is2)->Integer.compare(is2.getItem().getFoodProperties().getNutrition(), is1.getItem().getFoodProperties().getNutrition());
	private static final int DELAY=3*20;
	
	public static final ItemGroup COMBAT=new ItemGroup("engmod.combat")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ENGItems.CUSTOM_SWORD.get());
		}
	};
	
	public static final ItemGroup TOOLS=new ItemGroup("engmod.tools")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(Items.ENCHANTED_BOOK);
		}
		
		@Override
		public boolean hasEnchantmentCategory(EnchantmentType type)
		{
			return super.hasEnchantmentCategory(type)||type==ENGEnchantments.Types.PICKAXE_ONLY||type==ENGEnchantments.Types.DAMAGING;
		}

		@Override
		public EnchantmentType[] getEnchantmentCategories()
		{
			EnchantmentType[] current=super.getEnchantmentCategories();
			EnchantmentType[] result=new EnchantmentType[current.length+1];
			System.arraycopy(current, 0, result, 0, current.length);
			result[current.length]=ENGEnchantments.Types.PICKAXE_ONLY;
			return result;
		}
	};
	
	public static final ItemGroup MATERIALS=new ItemGroup("engmod.materials")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ENGItems.CUSTOM_MATERIAL.get());
		}
	};
	
	public static final ItemGroup BLOCKS=new ItemGroup("engmod.blocks")
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
			this.counter++;
			List<Item> tabItems=ENGItems.ITEMS.getEntries().stream().filter(r->r.get().getCreativeTabs().contains(this)).map(RegistryObject::get).collect(Collectors.toList());
			if(this.counter>=tabItems.size()*ENGTabs.DELAY) this.counter=0;
			return ENGTabs.currentIcon(this, tabItems, this.counter);
		}
		
		@Override
		public void fillItemList(NonNullList<ItemStack> items)
		{
			super.fillItemList(items);
			items.sort(ENGTabs.NAME_COMPARATOR);
			//Collections.sort(items, (itemStack1, itemStack2)->Float.compare(((BlockItem)itemStack1.getItem()).getBlock().getStateForPlacement(null).getDestroySpeed(null, null), ((BlockItem)itemStack2.getItem()).getBlock().getStateForPlacement(null).getDestroySpeed(null, null)));
		}
	};
	
	public static final ItemGroup MISC=new ItemGroup("engmod.misc")
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
			this.counter++;
			List<Item> tabItems=ENGItems.ITEMS.getEntries().stream().filter(r->r.get().getCreativeTabs().contains(this)).map(RegistryObject::get).collect(Collectors.toList());
			tabItems.add(EggHelper.VALKYRIE_SPAWN_EGG);
			if(this.counter>=tabItems.size()*ENGTabs.DELAY) this.counter=0;
			return ENGTabs.currentIcon(this, tabItems, this.counter);
		}
	};
	
	public static final ItemGroup FOOD=new ItemGroup("engmod.food")
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
			this.counter++;
			List<Item> tabItems=ENGItems.ITEMS.getEntries().stream().filter(r->r.get().getCreativeTabs().contains(this)).map(RegistryObject::get).collect(Collectors.toList());
			if(this.counter>=tabItems.size()*ENGTabs.DELAY) this.counter=0;
			return ENGTabs.currentIcon(this, tabItems, this.counter);
		}

		@Override
		public void fillItemList(NonNullList<ItemStack> items)
		{
			super.fillItemList(items);
			items.sort(ENGTabs.FOOD_COMPARATOR);
		}
	};
	
	private static ItemStack currentIcon(ItemGroup tab, List<Item> tabItems, int counter)
	{
		for(int i=0; i<tabItems.size(); i++)
		{
			Item item=tabItems.get(i);
			if(item.getCreativeTabs().contains(tab))
			{
				if(counter>=ENGTabs.DELAY*i&&counter<ENGTabs.DELAY*(i+1)) return new ItemStack(item);
			}
		}
		return tab.makeIcon();
	}
}