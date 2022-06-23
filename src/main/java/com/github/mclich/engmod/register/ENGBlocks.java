package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.block.BarleyCropBlock;
import com.github.mclich.engmod.block.BreweryBlock;
import com.github.mclich.engmod.block.HopBushBlock;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
public abstract class ENGBlocks
{
	public static final DeferredRegister<Block> BLOCKS=DeferredRegister.create(ForgeRegistries.BLOCKS, ElderNorseGods.MOD_ID);

	public static final RegistryObject<Block> BREWERY=ENGBlocks.BLOCKS.register(BreweryBlock.ID, BreweryBlock::new);
	public static final RegistryObject<Block> BARLEY_CROP=ENGBlocks.BLOCKS.register(BarleyCropBlock.ID, BarleyCropBlock::new);
	public static final RegistryObject<Block> HOP_BUSH=ENGBlocks.BLOCKS.register(HopBushBlock.ID, HopBushBlock::new);
	public static final RegistryObject<Block> BARLEY_HAY=ENGBlocks.BLOCKS.register("barley_hay_block", ()->new HayBlock(Block.Properties.of(Material.GRASS, MaterialColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> FIRE_LILY_FLOWER=ENGBlocks.BLOCKS.register("fire_lily", ()->new FlowerBlock(MobEffects.FIRE_RESISTANCE, 10, Block.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)));
	public static final RegistryObject<Block> FROST_HYACINTH_FLOWER=ENGBlocks.BLOCKS.register("frost_hyacinth", ()->new FlowerBlock(MobEffects.NIGHT_VISION, 10, Block.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)));
	public static final RegistryObject<Block> POTTED_FIRE_LILY_FLOWER=ENGBlocks.BLOCKS.register("potted_fire_lily", ()->new FlowerPotBlock(((FlowerPotBlock)Blocks.FLOWER_POT)::getEmptyPot, ENGBlocks.FIRE_LILY_FLOWER, Block.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
	public static final RegistryObject<Block> POTTED_FROST_HYACINTH_FLOWER=ENGBlocks.BLOCKS.register("potted_frost_hyacinth", ()->new FlowerPotBlock(((FlowerPotBlock)Blocks.FLOWER_POT)::getEmptyPot, ENGBlocks.FROST_HYACINTH_FLOWER, Block.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
	
	@SubscribeEvent
    public static void registerBlockRenderers(FMLClientSetupEvent event)
    {
		event.enqueueWork(()->
		{
			ItemBlockRenderTypes.setRenderLayer(ENGBlocks.BARLEY_CROP.get(), RenderType.cutoutMipped());
			ItemBlockRenderTypes.setRenderLayer(ENGBlocks.HOP_BUSH.get(), RenderType.cutoutMipped());
			ItemBlockRenderTypes.setRenderLayer(ENGBlocks.FIRE_LILY_FLOWER.get(), RenderType.cutoutMipped());
			ItemBlockRenderTypes.setRenderLayer(ENGBlocks.FROST_HYACINTH_FLOWER.get(), RenderType.cutoutMipped());
			ItemBlockRenderTypes.setRenderLayer(ENGBlocks.POTTED_FIRE_LILY_FLOWER.get(), RenderType.cutoutMipped());
			ItemBlockRenderTypes.setRenderLayer(ENGBlocks.POTTED_FROST_HYACINTH_FLOWER.get(), RenderType.cutoutMipped());
			ElderNorseGods.LOGGER.info("Registering block renderers completed");
		});
	}
	
	@SubscribeEvent
    public static void registerFlowerBehaviour(FMLCommonSetupEvent event)
    {
		event.enqueueWork(()->
		{
			((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(ENGBlocks.FIRE_LILY_FLOWER.getId(), ENGBlocks.POTTED_FIRE_LILY_FLOWER);
			((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(ENGBlocks.FROST_HYACINTH_FLOWER.getId(), ENGBlocks.POTTED_FROST_HYACINTH_FLOWER);
			ElderNorseGods.LOGGER.info("Registering flowers behaviour completed");
		});
    }
}