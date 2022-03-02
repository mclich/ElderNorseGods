package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.data.capability.ManaCapability;
import com.github.mclich.engmod.data.handler.IManaHandler;
import com.github.mclich.engmod.data.handler.ManaHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
public abstract class ENGCapabilities
{
	@SubscribeEvent
	public static void registerCapabilities(FMLCommonSetupEvent event)
	{
		event.enqueueWork(()->CapabilityManager.INSTANCE.register(IManaHandler.class, new ManaCapability.Storage(), ManaHandler::new));
	}

	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
	private static abstract class AttachCapabilities
	{
		@SubscribeEvent
		public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
		{
			if(!(event.getObject() instanceof PlayerEntity)) return;
			ManaCapability.Provider provider=new ManaCapability.Provider();
			event.addCapability(ManaCapability.LOCATION, provider);
			event.addListener(provider::invalidate);
		}
	}
}