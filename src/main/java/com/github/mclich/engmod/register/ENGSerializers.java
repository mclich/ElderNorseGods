package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.recipe.BrewingRecipe;
import com.github.mclich.engmod.recipe.serializer.BrewingRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ENGSerializers
{
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS=DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<RecipeSerializer<?>> BREWING_SERIALIZER=ENGSerializers.SERIALIZERS.register(BrewingRecipe.ID, BrewingRecipeSerializer::new);
}