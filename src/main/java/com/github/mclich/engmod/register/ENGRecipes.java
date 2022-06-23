package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.recipe.BrewingRecipe;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.RecipeBookRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
public abstract class ENGRecipes
{
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES=DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ElderNorseGods.MOD_ID);

	public static final RegistryObject<RecipeType<BrewingRecipe>> BREWING=ENGRecipes.RECIPE_TYPES.register(BrewingRecipe.ID, ()->new RecipeType<>(){});

	@SubscribeEvent
	public static void registerCategories(FMLClientSetupEvent event)
	{
		event.enqueueWork(()->
		{
			RecipeBookCategories brewing=RecipeBookCategories.create("brewing", new ItemStack(ENGItems.HOP.get()));
			RecipeBookRegistry.addCategoriesFinder(ENGRecipes.BREWING.get(), r->brewing);
			RecipeBookRegistry.addAggregateCategories(brewing, ImmutableList.of(brewing));
			ElderNorseGods.LOGGER.info("Registering recipe categories completed");
		});
	}
}