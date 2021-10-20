package com.github.mclich.engmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.mclich.engmod.util.*;

@Mod(ElderNorseGods.MOD_ID)
public class ElderNorseGods
{
	public static final String MOD_ID="engmod";
    public static final Logger LOGGER=LogManager.getLogger(ElderNorseGods.class);
    public ElderNorseGods()
    {
    	IEventBus eventBus=FMLJavaModLoadingContext.get().getModEventBus();
    	ENGItems.ITEMS.register(eventBus);
    	ENGBlocks.BLOCKS.register(eventBus);
    	ENGTileEntities.TILE_ENTITIES.register(eventBus);
    	ENGContainers.CONTAINERS.register(eventBus);
    	ENGEffects.EFFECTS.register(eventBus);
    	ENGSerializers.SERIALIZERS.register(eventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}