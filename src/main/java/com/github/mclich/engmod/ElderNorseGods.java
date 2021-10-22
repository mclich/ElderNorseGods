package com.github.mclich.engmod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.mclich.engmod.network.server.*;
import com.github.mclich.engmod.util.*;

@Mod(ElderNorseGods.MOD_ID)
public class ElderNorseGods
{
	public static final String MOD_ID="engmod";
	public static final Logger LOGGER=LogManager.getLogger(ElderNorseGods.class);
	
	private static final String PROTOCOL_VERSION="1";
	private static final SimpleChannel CHANNEL=NetworkRegistry.newSimpleChannel
	(
		new ResourceLocation(ElderNorseGods.MOD_ID, "network"),
		()->ElderNorseGods.PROTOCOL_VERSION,
		ElderNorseGods.PROTOCOL_VERSION::equals,
		ElderNorseGods.PROTOCOL_VERSION::equals
	);
	
	public ElderNorseGods()
	{
		IEventBus eventBus=FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(ElderNorseGods::registerPackets);
		ENGItems.ITEMS.register(eventBus);
		ENGBlocks.BLOCKS.register(eventBus);
		ENGTileEntities.TILE_ENTITIES.register(eventBus);
		ENGContainers.CONTAINERS.register(eventBus);
		ENGEffects.EFFECTS.register(eventBus);
		ENGSerializers.SERIALIZERS.register(eventBus);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private static void registerPackets(final FMLCommonSetupEvent event)
	{
		int id=0;
		ElderNorseGods.CHANNEL.registerMessage(id++, ItemActivationPacket.class, ItemActivationPacket::encode, ItemActivationPacket::decode, ItemActivationPacket::handle);
		ElderNorseGods.CHANNEL.registerMessage(id++, SpawnParticlesPacket.class, SpawnParticlesPacket::encode, SpawnParticlesPacket::decode, SpawnParticlesPacket::handle);
	}

	public static SimpleChannel getChannel()
	{
		return ElderNorseGods.CHANNEL;
	}
}