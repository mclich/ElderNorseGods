package com.github.mclich.engmod.recipe.serializer;

import com.github.mclich.engmod.recipe.BrewingRecipe;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class BrewingRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BrewingRecipe>
{
	private final Supplier<BrewingRecipe> supplier;
	private final float defaultExperience;
	private final int defaultBrewingTime;
	
	public BrewingRecipeSerializer(Supplier<BrewingRecipe> supplier, float defaultExperience, int defaultBrewingTime)
	{
		this.supplier=supplier;
		this.defaultExperience=defaultExperience;
		this.defaultBrewingTime=defaultBrewingTime;
	}

	@SuppressWarnings("unused")
	public BrewingRecipeSerializer(Supplier<BrewingRecipe> supplier)
	{
		this(supplier, 0.7F, 200);
	}
	
	public BrewingRecipeSerializer()
	{
		this(BrewingRecipe::new, 0.7F, 200);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BrewingRecipe fromJson(ResourceLocation location, JsonObject jsonFile)
	{
		Ingredient bottle=Ingredient.fromJson(JSONUtils.isArrayNode(jsonFile, "bottle")?JSONUtils.getAsJsonArray(jsonFile, "bottle"):JSONUtils.getAsJsonObject(jsonFile, "bottle"));
		Ingredient ingredient=Ingredient.fromJson(JSONUtils.isArrayNode(jsonFile, "ingredient")?JSONUtils.getAsJsonArray(jsonFile, "ingredient"):JSONUtils.getAsJsonObject(jsonFile, "ingredient"));
		if(!jsonFile.has("result")) throw new JsonSyntaxException("Missing result, expected to find a string or object");
		@SuppressWarnings("all") ItemStack itemstack=ItemStack.EMPTY;
		if(jsonFile.get("result").isJsonObject()) itemstack=ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(jsonFile, "result"));
		else
		{
			String result=JSONUtils.getAsString(jsonFile, "result");
			ResourceLocation rl=new ResourceLocation(result);
			itemstack=new ItemStack(Registry.ITEM.getOptional(rl).orElseThrow(()->new IllegalStateException("Item: "+result+" does not exist")));
		}
		float xp=JSONUtils.getAsFloat(jsonFile, "experience", this.defaultExperience);
		int brewTime=JSONUtils.getAsInt(jsonFile, "brew_time", this.defaultBrewingTime);
		return this.supplier.create(location, itemstack, bottle, ingredient, xp, brewTime);
	}

	@Override
	public BrewingRecipe fromNetwork(ResourceLocation location, PacketBuffer buffer)
	{
		return null;
	}

	@Override
	public void toNetwork(PacketBuffer buffer, BrewingRecipe recipe)
	{
		
	}
	
	private interface Supplier<R extends BrewingRecipe>
	{
		R create(ResourceLocation location, ItemStack result, Ingredient bottle, Ingredient ingredient, float experience, int brewingTime);
	}
}