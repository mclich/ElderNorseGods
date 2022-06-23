package com.github.mclich.engmod;

import com.github.mclich.engmod.register.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ElderNorseGods.MOD_ID)
public class ElderNorseGods
{
	public static final String MOD_ID="engmod";
	public static final Logger LOGGER=LogManager.getLogger(ElderNorseGods.class.getSimpleName());

	//todo: add javadocs
	public ElderNorseGods()
	{
		IEventBus eventBus=FMLJavaModLoadingContext.get().getModEventBus();
		ENGParticles.PARTICLES.register(eventBus);
		ENGMobEffects.MOB_EFFECTS.register(eventBus);
		ENGPotions.POTIONS.register(eventBus);
		ENGEnchantments.ENCHANTMENTS.register(eventBus);
		ENGItems.ITEMS.register(eventBus);
		ENGBlocks.BLOCKS.register(eventBus);
		ENGBlockEntities.BLOCK_ENTITIES.register(eventBus);
		ENGContainers.CONTAINERS.register(eventBus);
		ENGEntities.ENTITIES.register(eventBus);
		ENGRecipes.RECIPE_TYPES.register(eventBus);
		ENGSerializers.SERIALIZERS.register(eventBus);
		MinecraftForge.EVENT_BUS.register(this);
		ElderNorseGods.LOGGER.info("Mod initialization completed");
	}
}