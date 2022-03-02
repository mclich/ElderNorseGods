package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.recipe.BrewingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
public abstract class ENGRecipeTypes
{
	private static IRecipeType<BrewingRecipe> BREWING_TYPE;
	
	public static IRecipeType<BrewingRecipe> getBrewingType()
	{
		return ENGRecipeTypes.BREWING_TYPE;
	}
	
	@SubscribeEvent
    public static void registerRecipeTypes(FMLCommonSetupEvent event)
    {
		event.enqueueWork(()->ENGRecipeTypes.BREWING_TYPE=IRecipeType.register(ElderNorseGods.MOD_ID+":"+BrewingRecipe.ID));
    }
}