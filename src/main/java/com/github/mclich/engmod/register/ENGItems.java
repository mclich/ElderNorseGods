package com.github.mclich.engmod.register;

import java.util.Map;
import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.block.BreweryBlock;
import com.github.mclich.engmod.entity.ValkyrieEntity;
import com.github.mclich.engmod.item.*;
import com.github.mclich.engmod.item.staff.*;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ENGItems
{
	public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(ForgeRegistries.ITEMS, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<Item> CUSTOM_MATERIAL=ENGItems.ITEMS.register("custom_material", ()->new Item(new Item.Properties().tab(ENGTabs.MATERIALS)));
	public static final RegistryObject<Item> BARLEY=ENGItems.ITEMS.register("barley", ()->new Item(new Item.Properties().tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> BARLEY_SEEDS=ENGItems.ITEMS.register("barley_seeds", ()->new BlockNamedItem(ENGBlocks.BARLEY_CROP.get(), new Item.Properties().tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> HOP=ENGItems.ITEMS.register("hop", ()->new BlockNamedItem(ENGBlocks.HOP_BUSH.get(), new Item.Properties().tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> MALT=ENGItems.ITEMS.register("malt", ()->new Item(new Item.Properties().tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> BEER=ENGItems.ITEMS.register(BeerItem.ID, BeerItem::new);
	public static final RegistryObject<Item> FIRE_TINCTURE=ENGItems.ITEMS.register("fire_extract", ()->new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON).tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> FROST_TINCTURE=ENGItems.ITEMS.register("frost_extract", ()->new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON).tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> MANA_MIXTURE=ENGItems.ITEMS.register(ManaMixtureItem.ID, ManaMixtureItem::new);
	public static final RegistryObject<Item> BARLEY_BREAD=ENGItems.ITEMS.register("barley_bread", ()->new Item(new Item.Properties().food(new Food.Builder().nutrition(5).saturationMod(0.6F).build()).tab(ENGTabs.FOOD)));
	public static final RegistryObject<Item> TOTEM_OF_ABYSS=ENGItems.ITEMS.register(TotemOfAbyssItem.ID, TotemOfAbyssItem::new);
	public static final RegistryObject<Item> CUSTOM_SWORD=ENGItems.ITEMS.register(CustomSwordItem.ID, CustomSwordItem::new);
	public static final RegistryObject<Item> HEALING_STAFF=ENGItems.ITEMS.register(HealingStaffItem.ID, HealingStaffItem::new);
	public static final RegistryObject<Item> REGENERATION_STAFF=ENGItems.ITEMS.register(RegenerationStaffItem.ID, RegenerationStaffItem::new);
	
	public static final RegistryObject<Item> BREWERY_ITEM=ENGItems.ITEMS.register(BreweryBlock.ID, ()->new BlockNamedItem(ENGBlocks.BREWERY.get(), new Item.Properties().tab(ENGTabs.BLOCKS)));
	public static final RegistryObject<Item> BARLEY_HAY_BLOCK_ITEM=ENGItems.ITEMS.register("barley_hay_block", ()->new BlockNamedItem(ENGBlocks.BARLEY_HAY_BLOCK.get(), new Item.Properties().tab(ENGTabs.BLOCKS)));
	public static final RegistryObject<Item> FIRE_LILY_FLOWER_ITEM=ENGItems.ITEMS.register("fire_lily", ()->new BlockNamedItem(ENGBlocks.FIRE_LILY_FLOWER.get(), new Item.Properties().rarity(Rarity.UNCOMMON).tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> FROST_HYACINTH_FLOWER_ITEM=ENGItems.ITEMS.register("frost_hyacinth", ()->new BlockNamedItem(ENGBlocks.FROST_HYACINTH_FLOWER.get(), new Item.Properties().rarity(Rarity.UNCOMMON).tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> CUSTOM_BLOCK_ITEM=ENGItems.ITEMS.register("custom_block", ()->new BlockNamedItem(ENGBlocks.CUSTOM_BLOCK.get(), new Item.Properties().tab(ENGTabs.BLOCKS)));
	
	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
	public static abstract class PropsHelper
	{
		public static final ResourceLocation CASTING=new ResourceLocation(ElderNorseGods.MOD_ID, "casting");
		public static final IItemPropertyGetter CASTTING_PROP=(itemStack, world, entity)->entity instanceof PlayerEntity&&entity.isUsingItem()&&entity.getUseItem()==itemStack&&itemStack.getItem() instanceof StaffItem?1F:0F;
		
		@SubscribeEvent
		public static void registerProperties(FMLClientSetupEvent event)
		{
			event.enqueueWork(()->
			{
				ItemModelsProperties.register(ENGItems.HEALING_STAFF.get(), PropsHelper.CASTING, PropsHelper.CASTTING_PROP);
				ItemModelsProperties.register(ENGItems.REGENERATION_STAFF.get(), PropsHelper.CASTING, PropsHelper.CASTTING_PROP);
			});
		}
	}
	
	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
	public static abstract class EggHelper
	{
		private static final Map<EntityType<?>, SpawnEggItem> VANILLA_EGGS=ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, null, "BY_ID");
		private static final DefaultDispenseItemBehavior DDIB=new DefaultDispenseItemBehavior()
		{
			@Override
			public ItemStack execute(IBlockSource source, ItemStack stack)
			{
				Direction dir=source.getBlockState().getValue(DispenserBlock.FACING);
                EntityType<?> type=((SpawnEggItem)stack.getItem()).getType(stack.getTag());
                type.spawn(source.getLevel(), stack, null, source.getPos().relative(dir), SpawnReason.DISPENSER, dir!=Direction.DOWN, false);
                stack.shrink(1);
                return stack;
			}
        };
		
		public static final SpawnEggItem VALKYRIE_SPAWN_EGG=new SpawnEggItem(ENGEntities.VALKYRIE, 0x5bbcf4, 0xf8dd72, new Item.Properties().tab(ENGTabs.MISC));
		
		private static SpawnEggItem setupEgg(SpawnEggItem egg)
		{
			EggHelper.VANILLA_EGGS.put(egg.getType(null), egg);
			DispenserBlock.registerBehavior(egg, EggHelper.DDIB);
			return egg;
		}
		
		@SubscribeEvent
	    public static void registerSpawnEggs(Register<Item> event)
	    {
			event.getRegistry().register(EggHelper.setupEgg(EggHelper.VALKYRIE_SPAWN_EGG).setRegistryName(new ResourceLocation(ElderNorseGods.MOD_ID, ValkyrieEntity.SPAWN_EGG_ID)));
	    }
	}
}