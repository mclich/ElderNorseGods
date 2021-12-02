package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.data.capability.ManaCapability;
import com.github.mclich.engmod.data.capability.ManaCapability.ManaProvider;
import com.github.mclich.engmod.data.capability.ManaCapability.ManaStorage;
import com.github.mclich.engmod.data.handler.IManaHandler;
import com.github.mclich.engmod.data.handler.ManaHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.MOD)
public abstract class ENGCapabilities
{
	@SubscribeEvent
	public static void registerCapabilities(FMLCommonSetupEvent event)
	{
		CapabilityManager.INSTANCE.register(IManaHandler.class, new ManaStorage(), ManaHandler::new);
	}

	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.FORGE)
	public static abstract class AttachCapabilities
	{
		@SubscribeEvent
		public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
		{
			if(!(event.getObject() instanceof PlayerEntity)) return;
			ManaProvider provider=new ManaProvider();
			event.addCapability(ManaCapability.LOCATION, provider);
			event.addListener(provider::invalidate);
		}
	}
}