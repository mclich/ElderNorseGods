package com.github.mclich.engmod.register;

import java.lang.reflect.Method;
import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.effect.AnemiaEffect;
import com.github.mclich.engmod.effect.ReplenishmentEffect;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.Potions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
public abstract class ENGPotions
{
	public static final DeferredRegister<Potion> POTIONS=DeferredRegister.create(ForgeRegistries.POTION_TYPES, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<Potion> ANEMIA=ENGPotions.POTIONS.register(AnemiaEffect.ID, ()->new Potion(AnemiaEffect.getInstance()));
	public static final RegistryObject<Potion> REPLENISHMENT=ENGPotions.POTIONS.register(ReplenishmentEffect.ID, ()->new Potion(ReplenishmentEffect.getInstance()));
	
	@SubscribeEvent
	public static void registerRecipes(FMLCommonSetupEvent event)
	{
		event.enqueueWork(()->
		{
			Method addMixMethod=ObfuscationReflectionHelper.findMethod(PotionBrewing.class, "addMix", Potion.class, Item.class, Potion.class);
			try
			{
				addMixMethod.invoke(null, Potions.AWKWARD, ENGItems.FROST_HYACINTH_FLOWER_ITEM.get(), ENGPotions.REPLENISHMENT.get());
			}
			catch(ReflectiveOperationException exc)
			{
				ElderNorseGods.LOGGER.fatal(exc.getMessage(), exc);
			}
		});
	}
}