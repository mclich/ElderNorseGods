package com.github.mclich.engmod.util;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.block.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.MOD)
public abstract class ENGBlocks
{
	public static final DeferredRegister<Block> BLOCKS=DeferredRegister.create(ForgeRegistries.BLOCKS, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<Block> BARLEY_CROP=ENGBlocks.BLOCKS.register(BarleyCropBlock.ID, BarleyCropBlock::new);
	public static final RegistryObject<Block> HOP_BUSH=ENGBlocks.BLOCKS.register(HopBushBlock.ID, HopBushBlock::new);
	public static final RegistryObject<Block> BREWERY=ENGBlocks.BLOCKS.register(BreweryBlock.ID, BreweryBlock::new);
	public static final RegistryObject<Block> BARLEY_HAY_BLOCK=ENGBlocks.BLOCKS.register(BarleyHayBlock.ID, BarleyHayBlock::new);
	public static final RegistryObject<Block> CUSTOM_BLOCK=ENGBlocks.BLOCKS.register(CustomBlock.ID, CustomBlock::new);
	
	@SubscribeEvent
    public static void registerBlockRenderers(FMLClientSetupEvent event)
    {
		RenderTypeLookup.setRenderLayer(ENGBlocks.BARLEY_CROP.get(), RenderType.cutoutMipped());
		RenderTypeLookup.setRenderLayer(ENGBlocks.HOP_BUSH.get(), RenderType.cutoutMipped());
    }
}