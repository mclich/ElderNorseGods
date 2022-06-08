package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.network.NetworkHandler;
import com.github.mclich.engmod.network.packet.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
public abstract class ENGPackets
{
	@SubscribeEvent
    public static void registerPackets(FMLCommonSetupEvent event)
    {
		event.enqueueWork(()->
		{
			int id=0;
			NetworkHandler.getChannel().registerMessage(id++, ItemActivationPacket.class, ItemActivationPacket::encode, ItemActivationPacket::decode, ItemActivationPacket::handle);
			NetworkHandler.getChannel().registerMessage(id++, SpawnParticlesPacket.class, SpawnParticlesPacket::encode, SpawnParticlesPacket::decode, SpawnParticlesPacket::handle);
			NetworkHandler.getChannel().registerMessage(id, ManaDataPacket.class, ManaDataPacket::encode, ManaDataPacket::decode, ManaDataPacket::handle);
		});
    }
}