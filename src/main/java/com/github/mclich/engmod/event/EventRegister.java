package com.github.mclich.engmod.event;

import com.github.mclich.engmod.ElderNorseGods;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public abstract class EventRegister
{
	
}