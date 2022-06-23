package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.client.gui.bar.ManaBar;
import com.github.mclich.engmod.client.gui.screen.BreweryScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
public abstract class ENGScreens
{
	@SubscribeEvent
	public static void registerOverlays(FMLClientSetupEvent event)
	{
		OverlayRegistry.registerOverlayTop("Mana Level", ManaBar.MANA_LEVEL_ELEMENT);
		ElderNorseGods.LOGGER.info("Registering in-game overlays completed");
	}

	@SubscribeEvent
	public static void registerScreens(FMLClientSetupEvent event)
	{
		event.enqueueWork(()->
		{
			MenuScreens.register(ENGContainers.BREWERY_CONTAINER.get(), BreweryScreen::new);
			ElderNorseGods.LOGGER.info("Registering screens completed");
		});
	}
}