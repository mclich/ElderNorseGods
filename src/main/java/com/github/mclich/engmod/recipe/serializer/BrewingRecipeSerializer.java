package com.github.mclich.engmod.recipe.serializer;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.recipe.BrewingRecipe;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class BrewingRecipeSerializer implements RecipeSerializer<BrewingRecipe>
{
	private final BrewingSupplier<BrewingRecipe> supplier;
	private final float defaultExperience;
	private final int defaultBrewTime;
	
	public BrewingRecipeSerializer(BrewingSupplier<BrewingRecipe> brewingSupplier, float defaultExperience, int defaultBrewTime)
	{
		this.supplier=brewingSupplier;
		this.defaultExperience=defaultExperience;
		this.defaultBrewTime=defaultBrewTime;
	}

	public BrewingRecipeSerializer(BrewingSupplier<BrewingRecipe> brewingSupplier)
	{
		this(brewingSupplier, 0.7F, 200);
	}
	
	public BrewingRecipeSerializer()
	{
		this(BrewingRecipe::new);
	}

	private static void requiresValidJson(JsonObject json)
	{
		if(!json.has("bottle")) throw new JsonSyntaxException("Missing 'bottle', expected to find an object");
		if(!json.has("ingredient")) throw new JsonSyntaxException("Missing 'ingredient', expected to find an object");
		if(!json.has("result")) throw new JsonSyntaxException("Missing 'result', expected to find a string");
	}

	private static Ingredient getIngredientFromJson(JsonObject json, String memberName)
	{
		return Ingredient.fromJson(GsonHelper.isArrayNode(json, memberName)?GsonHelper.getAsJsonArray(json, memberName):GsonHelper.getAsJsonObject(json, memberName));
	}

	private static ItemStack getResultItemFromJson(JsonObject json)
	{
		String location=GsonHelper.getAsString(json, "result");
		Item resultItem=ForgeRegistries.ITEMS.getValue(new ResourceLocation(location));
		if(resultItem==null)
		{
			ElderNorseGods.LOGGER.error("Cannot found result item from brewing recipe by key '{}'", location);
			return ItemStack.EMPTY;
		}
		return new ItemStack(resultItem);
	}

	@Override
	public BrewingRecipe fromJson(ResourceLocation location, JsonObject json)
	{
		BrewingRecipeSerializer.requiresValidJson(json);
		Ingredient bottle=BrewingRecipeSerializer.getIngredientFromJson(json, "bottle");
		Ingredient ingredient=BrewingRecipeSerializer.getIngredientFromJson(json, "ingredient");
		ItemStack result=BrewingRecipeSerializer.getResultItemFromJson(json);
		float experience=GsonHelper.getAsFloat(json, "experience", this.defaultExperience);
		int brewTime=GsonHelper.getAsInt(json, "brew_time", this.defaultBrewTime);
		return this.supplier.create(location, bottle, ingredient, result, experience, brewTime);
	}

	@Override
	public BrewingRecipe fromNetwork(ResourceLocation location, FriendlyByteBuf buffer)
	{
		return this.supplier.create
		(
			location,
			Ingredient.fromNetwork(buffer),
			Ingredient.fromNetwork(buffer),
			buffer.readItem(),
			buffer.readFloat(),
			buffer.readInt()
		);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, BrewingRecipe recipe)
	{
		recipe.getBottleIngredient().toNetwork(buffer);
		recipe.getCookIngredient().toNetwork(buffer);
		buffer.writeItem(recipe.getResultItem());
		buffer.writeFloat(recipe.getExperience());
		buffer.writeInt(recipe.getBrewTime());
	}
	
	private interface BrewingSupplier<R extends BrewingRecipe>
	{
		R create(ResourceLocation location, Ingredient bottle, Ingredient ingredient, ItemStack result, float experience, int brewingTime);
	}
}