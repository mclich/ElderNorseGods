package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.HayBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.ToolType;
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
	public static final RegistryObject<Block> FIRE_LILY_FLOWER=ENGBlocks.BLOCKS.register(FireLilyFlowerBlock.ID, FireLilyFlowerBlock::new);
	public static final RegistryObject<Block> FROST_HYACINTH_FLOWER=ENGBlocks.BLOCKS.register(FrostHyacinthFlowerBlock.ID, FrostHyacinthFlowerBlock::new);
	public static final RegistryObject<Block> POTTED_FIRE_LILY_FLOWER=ENGBlocks.BLOCKS.register(FireLilyFlowerBlock.POTTED_ID, ()->new FlowerPotBlock(((FlowerPotBlock)Blocks.FLOWER_POT)::getEmptyPot, ENGBlocks.FIRE_LILY_FLOWER::get, Block.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
	public static final RegistryObject<Block> POTTED_FROST_HYACINTH_FLOWER=ENGBlocks.BLOCKS.register(FrostHyacinthFlowerBlock.POTTED_ID, ()->new FlowerPotBlock(((FlowerPotBlock)Blocks.FLOWER_POT)::getEmptyPot, ENGBlocks.FROST_HYACINTH_FLOWER::get, Block.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
	public static final RegistryObject<Block> BREWERY=ENGBlocks.BLOCKS.register(BreweryBlock.ID, BreweryBlock::new);
	public static final RegistryObject<Block> BARLEY_HAY_BLOCK=ENGBlocks.BLOCKS.register("barley_hay_block", ()->new HayBlock(Block.Properties.of(Material.GRASS, MaterialColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> CUSTOM_BLOCK=ENGBlocks.BLOCKS.register("custom_block", ()->new Block(Block.Properties.of(Material.STONE).strength(5.0F, 6.0F).harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK)));
	
	@SubscribeEvent
    public static void registerBlockRenderers(FMLClientSetupEvent event)
    {
		RenderTypeLookup.setRenderLayer(ENGBlocks.BARLEY_CROP.get(), RenderType.cutoutMipped());
		RenderTypeLookup.setRenderLayer(ENGBlocks.HOP_BUSH.get(), RenderType.cutoutMipped());
		RenderTypeLookup.setRenderLayer(ENGBlocks.FIRE_LILY_FLOWER.get(), RenderType.cutoutMipped());
		RenderTypeLookup.setRenderLayer(ENGBlocks.FROST_HYACINTH_FLOWER.get(), RenderType.cutoutMipped());
		RenderTypeLookup.setRenderLayer(ENGBlocks.POTTED_FIRE_LILY_FLOWER.get(), RenderType.cutoutMipped());
		RenderTypeLookup.setRenderLayer(ENGBlocks.POTTED_FROST_HYACINTH_FLOWER.get(), RenderType.cutoutMipped());
    }
}