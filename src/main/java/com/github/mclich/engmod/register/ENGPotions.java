package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.effect.AnemiaEffect;
import com.github.mclich.engmod.effect.LossEffect;
import com.github.mclich.engmod.effect.ReplenishmentEffect;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionBrewing.Mix;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.List;

@SuppressWarnings("unused")
@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
public abstract class ENGPotions
{
	public static final DeferredRegister<Potion> POTIONS=DeferredRegister.create(ForgeRegistries.POTIONS, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<Potion> ANEMIA=ENGPotions.POTIONS.register(AnemiaEffect.ID, ()->new Potion(AnemiaEffect.getInstance()));
	public static final RegistryObject<Potion> LOSING=ENGPotions.POTIONS.register(LossEffect.POTION_ID, ()->new Potion(LossEffect.getInstance()));
	public static final RegistryObject<Potion> REPLENISHMENT=ENGPotions.POTIONS.register(ReplenishmentEffect.ID, ()->new Potion(ReplenishmentEffect.getInstance()));
	
	@SubscribeEvent
	public static void registerRecipes(FMLCommonSetupEvent event)
	{
		event.enqueueWork(()->
		{
			List<Mix<Potion>> potionMixes=ObfuscationReflectionHelper.getPrivateValue(PotionBrewing.class, null, "POTION_MIXES");
			potionMixes.add(new Mix<>(ForgeRegistries.POTIONS, Potions.AWKWARD, Ingredient.of(ENGItems.FROST_HYACINTH_FLOWER_ITEM.get()), ENGPotions.REPLENISHMENT.get()));
			ElderNorseGods.LOGGER.info("Registering potion recipes completed");
		});
	}
}