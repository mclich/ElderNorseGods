package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.data.capability.IManaStorage;
import com.github.mclich.engmod.data.provider.ManaProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
public abstract class ENGCapabilities
{
	public static final Capability<IManaStorage> MANA=CapabilityManager.get(new CapabilityToken<>(){});

	private static final ResourceLocation MANA_LOCATION=new ResourceLocation(ElderNorseGods.MOD_ID, "mana");

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event)
	{
		event.register(IManaStorage.class);
	}

	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
	private static abstract class AttachCapabilities
	{
		@SubscribeEvent
		public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
		{
			if(!(event.getObject() instanceof Player)) return;
			event.addCapability(ENGCapabilities.MANA_LOCATION, new ManaProvider());
		}
	}
}