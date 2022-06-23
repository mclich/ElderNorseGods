package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.block.BreweryBlock;
import com.github.mclich.engmod.entity.ValkyrieEntity;
import com.github.mclich.engmod.item.*;
import com.github.mclich.engmod.item.staff.HealingStaffItem;
import com.github.mclich.engmod.item.staff.RegenerationStaffItem;
import com.github.mclich.engmod.item.staff.StaffItem;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public abstract class ENGItems
{
	public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(ForgeRegistries.ITEMS, ElderNorseGods.MOD_ID);

	public static final RegistryObject<Item> BARLEY=ENGItems.ITEMS.register("barley", ()->new Item(new Item.Properties().tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> BARLEY_SEEDS=ENGItems.ITEMS.register("barley_seeds", ()->new ItemNameBlockItem(ENGBlocks.BARLEY_CROP.get(), new Item.Properties().tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> HOP=ENGItems.ITEMS.register("hop", ()->new ItemNameBlockItem(ENGBlocks.HOP_BUSH.get(), new Item.Properties().tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> MALT=ENGItems.ITEMS.register("malt", ()->new Item(new Item.Properties().tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> BEER=ENGItems.ITEMS.register(BeerItem.ID, BeerItem::new);
	public static final RegistryObject<Item> FIRE_TINCTURE=ENGItems.ITEMS.register("fire_extract", ()->new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON).tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> FROST_TINCTURE=ENGItems.ITEMS.register("frost_extract", ()->new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON).tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> MANA_MIXTURE=ENGItems.ITEMS.register(ManaMixtureItem.ID, ManaMixtureItem::new);
	public static final RegistryObject<Item> BARLEY_BREAD=ENGItems.ITEMS.register("barley_bread", ()->new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build()).tab(ENGTabs.FOOD)));
	public static final RegistryObject<Item> TOTEM_OF_ABYSS=ENGItems.ITEMS.register(TotemOfAbyssItem.ID, TotemOfAbyssItem::new);
	public static final RegistryObject<Item> CUSTOM_SWORD=ENGItems.ITEMS.register("custom_sword", ()->new SwordItem(ENGItemTiers.CUSTOM_SWORD_TIER, 6, -2.4F, new Item.Properties().tab(ENGTabs.COMBAT)));
	public static final RegistryObject<Item> HEALING_STAFF=ENGItems.ITEMS.register(HealingStaffItem.ID, HealingStaffItem::new);
	public static final RegistryObject<Item> REGENERATION_STAFF=ENGItems.ITEMS.register(RegenerationStaffItem.ID, RegenerationStaffItem::new);

	public static final RegistryObject<Item> VALKYRIE_SPAWN_EGG=ENGItems.ITEMS.register(ValkyrieEntity.SPAWN_EGG_ID, ()->new ForgeSpawnEggItem(ENGEntities.VALKYRIE, 0x5BBCF4, 0xF8DD72, new Item.Properties().tab(ENGTabs.MISC)));

	public static final RegistryObject<Item> BREWERY_ITEM=ENGItems.ITEMS.register(BreweryBlock.ID, ()->new ItemNameBlockItem(ENGBlocks.BREWERY.get(), new Item.Properties().tab(ENGTabs.BLOCKS)));
	public static final RegistryObject<Item> BARLEY_HAY_BLOCK_ITEM=ENGItems.ITEMS.register("barley_hay_block", ()->new ItemNameBlockItem(ENGBlocks.BARLEY_HAY.get(), new Item.Properties().tab(ENGTabs.BLOCKS)));
	public static final RegistryObject<Item> FIRE_LILY_FLOWER_ITEM=ENGItems.ITEMS.register("fire_lily", ()->new ItemNameBlockItem(ENGBlocks.FIRE_LILY_FLOWER.get(), new Item.Properties().rarity(Rarity.UNCOMMON).tab(ENGTabs.MISC)));
	public static final RegistryObject<Item> FROST_HYACINTH_FLOWER_ITEM=ENGItems.ITEMS.register("frost_hyacinth", ()->new ItemNameBlockItem(ENGBlocks.FROST_HYACINTH_FLOWER.get(), new Item.Properties().rarity(Rarity.UNCOMMON).tab(ENGTabs.MISC)));

	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
	public static abstract class PropsHelper
	{
		public static final ResourceLocation CASTING=new ResourceLocation(ElderNorseGods.MOD_ID, "casting");
		public static final ClampedItemPropertyFunction CASTING_PROP=(itemStack, world, entity, seed)->entity instanceof Player&&entity.isUsingItem()&&entity.getUseItem()==itemStack&&itemStack.getItem() instanceof StaffItem?1F:0F;
		
		@SubscribeEvent
		public static void registerProperties(FMLClientSetupEvent event)
		{
			event.enqueueWork(()->
			{
				ItemProperties.register(ENGItems.HEALING_STAFF.get(), PropsHelper.CASTING, PropsHelper.CASTING_PROP);
				ItemProperties.register(ENGItems.REGENERATION_STAFF.get(), PropsHelper.CASTING, PropsHelper.CASTING_PROP);
				ElderNorseGods.LOGGER.info("Registering item properties completed");
			});
		}
	}
}