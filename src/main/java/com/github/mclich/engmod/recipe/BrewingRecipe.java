package com.github.mclich.engmod.recipe;

import com.github.mclich.engmod.entity.block.BreweryBlockEntity;
import com.github.mclich.engmod.register.ENGBlocks;
import com.github.mclich.engmod.register.ENGRecipes;
import com.github.mclich.engmod.register.ENGSerializers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class BrewingRecipe implements Recipe<BreweryBlockEntity>
{
	public static final String ID="brewing";
	
	private final ResourceLocation location;
	private final ItemStack result;
	private final Ingredient bottle;
	private final Ingredient ingredient;
	private final float experience;
	private final int brewTime;
	
	public BrewingRecipe(ResourceLocation location, Ingredient bottle, Ingredient ingredient, ItemStack result, float experience, int brewTime)
	{
		this.location=location;
		this.result=result;
		this.bottle=bottle;
		this.ingredient=ingredient;
		this.experience=experience;
		this.brewTime=brewTime;
	}
	
	@Override
	public ItemStack getToastSymbol()
	{
		return new ItemStack(ENGBlocks.BREWERY.get());
	}
	
	@Override
	public boolean matches(BreweryBlockEntity breweryEntity, Level world)
	{
		return (this.bottle.test(breweryEntity.getItem(0))||
			    this.bottle.test(breweryEntity.getItem(1))||
			    this.bottle.test(breweryEntity.getItem(2)))&&
			    this.ingredient.test(breweryEntity.getItem(4));
	}

	@Override
	public ItemStack assemble(BreweryBlockEntity breweryEntity)
	{
		return this.result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return true;
	}
	
	@Override
	public ResourceLocation getId()
	{
		return this.location;
	}
	
	@Override
	public ItemStack getResultItem()
	{
		return this.result;
	}

	public Ingredient getBottleIngredient()
	{
		return this.bottle;
	}

	public Ingredient getCookIngredient()
	{
		return this.ingredient;
	}

	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		NonNullList<Ingredient> result=NonNullList.create();
		result.add(this.bottle);
		result.add(this.ingredient);
		return result;
	}
	
	public float getExperience()
	{
		return this.experience;
	}

	public int getBrewTime()
	{
		return this.brewTime;
	}

	@Override
	public RecipeType<?> getType()
	{
		return ENGRecipes.BREWING.get();
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return ENGSerializers.BREWING_SERIALIZER.get();
	}
}
