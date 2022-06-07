package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.client.gui.screen.BreweryScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
public abstract class ENGGui
{
	@SubscribeEvent
	public static void registerScreens(FMLClientSetupEvent event)
	{
		event.enqueueWork(()->ScreenManager.register(ENGContainers.BREWERY_CONTAINER.get(), BreweryScreen::new));
	}
}