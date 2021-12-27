package com.github.mclich.engmod.recipe;

import com.github.mclich.engmod.entity.tile.BreweryTileEntity;
import com.github.mclich.engmod.register.ENGBlocks;
import com.github.mclich.engmod.register.ENGRecipeTypes;
import com.github.mclich.engmod.register.ENGSerializers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BrewingRecipe implements IRecipe<BreweryTileEntity>
{
	public static final String ID="brewing";
	
	private final ResourceLocation location;
	private final ItemStack result;
	private final Ingredient bottle;
	private final Ingredient ingredient;
	private final float experience;
	private final int brewTime;
	
	public BrewingRecipe(ResourceLocation location, ItemStack result, Ingredient bottle, Ingredient ingredient, float experience, int brewTime)
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
	public boolean matches(BreweryTileEntity tileEntity, World world)
	{
		return (this.bottle.test(tileEntity.getItem(0))||
			    this.bottle.test(tileEntity.getItem(1))||
			    this.bottle.test(tileEntity.getItem(2)))&&
			    this.ingredient.test(tileEntity.getItem(4));
	}

	@Override
	public ItemStack assemble(BreweryTileEntity tileEntity)
	{
		return this.result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int ignore, int ignore_)
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
	
	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		return NonNullList.of(bottle, bottle, ingredient);
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
	public IRecipeType<?> getType()
	{
		return ENGRecipeTypes.getBrewingType();
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return ENGSerializers.BREWERY_SERIALIZER.get();
	}
}
