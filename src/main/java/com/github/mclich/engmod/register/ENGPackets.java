package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.network.server.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.MOD)
public abstract class ENGPackets
{
	@SubscribeEvent
    public static void registerPackets(FMLCommonSetupEvent event)
    {
		int id=0;
		ElderNorseGods.getChannel().registerMessage(id++, ItemActivationPacket.class, ItemActivationPacket::encode, ItemActivationPacket::decode, ItemActivationPacket::handle);
		ElderNorseGods.getChannel().registerMessage(id++, SpawnParticlesPacket.class, SpawnParticlesPacket::encode, SpawnParticlesPacket::decode, SpawnParticlesPacket::handle);
    }
}