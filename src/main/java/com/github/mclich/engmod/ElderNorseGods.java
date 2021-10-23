package com.github.mclich.engmod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.mclich.engmod.register.*;

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
		ENGEffects.EFFECTS.register(eventBus);
		ENGEnchantments.ENCHANTMENTS.register(eventBus);
		ENGItems.ITEMS.register(eventBus);
		ENGBlocks.BLOCKS.register(eventBus);
		ENGTileEntities.TILE_ENTITIES.register(eventBus);
		ENGContainers.CONTAINERS.register(eventBus);
		ENGSerializers.SERIALIZERS.register(eventBus);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public static SimpleChannel getChannel()
	{
		return ElderNorseGods.CHANNEL;
	}
}