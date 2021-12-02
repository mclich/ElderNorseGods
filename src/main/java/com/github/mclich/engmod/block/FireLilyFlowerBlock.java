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
public class FireLilyFlowerBlock extends FlowerBlock
{
	public static final String ID="fire_lily";
	public static final String POTTED_ID="potted_fire_lily";
	
	public FireLilyFlowerBlock()
	{
		super(Effects.FIRE_RESISTANCE, 10, Block.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS));
	}
	
	@SubscribeEvent
    public static void registerFlowerBehavior(FMLCommonSetupEvent event)
    {
		event.enqueueWork(()->((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(ENGBlocks.FIRE_LILY_FLOWER.getId(), ENGBlocks.POTTED_FIRE_LILY_FLOWER::get));
    }
}