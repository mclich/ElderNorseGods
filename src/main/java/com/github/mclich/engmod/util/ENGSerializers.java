package com.github.mclich.engmod.util;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.recipe.BrewingRecipe;
import com.github.mclich.engmod.recipe.serializer.BrewingRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ENGSerializers
{
	public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS=DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<BrewingRecipeSerializer> BREWING_SERIALIZER=ENGSerializers.SERIALIZERS.register(BrewingRecipe.ID, ()->new BrewingRecipeSerializer());
}