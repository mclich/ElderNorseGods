package com.github.mclich.engmod.world.gen;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGEntities;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.FORGE)
public abstract class EntitySpawn
{
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void onBiomeLoading(BiomeLoadingEvent event)
	{
		//event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(ENGModEntities.VALKYRIE_ENTITY.get(), 200, 1, 4));
		if(event.getCategory()==Biome.Category.PLAINS)
		{
			event.getSpawns().addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ENGEntities.VALKYRIE_ENTITY, 100, 1, 4));
		}
	}
}