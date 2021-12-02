package com.github.mclich.engmod.block;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.MOD)
public class FrostHyacinthFlowerBlock extends FlowerBlock
{
	public static final String ID="frost_hyacinth";
	public static final String POTTED_ID="potted_frost_hyacinth";
	
	public FrostHyacinthFlowerBlock()
	{
		super(Effects.NIGHT_VISION, 10, Block.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS));
	}
	
	@SubscribeEvent
    public static void registerFlowerBehavior(FMLCommonSetupEvent event)
    {
		event.enqueueWork(()->((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(ENGBlocks.FROST_HYACINTH_FLOWER.getId(), ENGBlocks.POTTED_FROST_HYACINTH_FLOWER::get));
    }
}