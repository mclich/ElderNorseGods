package com.github.mclich.engmod.world.gen;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGEntities;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Items;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
public abstract class EntitySpawn
{
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void registerSpawns(BiomeLoadingEvent event)
	{
		//event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(ENGModEntities.VALKYRIE_ENTITY.get(), 200, 1, 4));
		if(event.getCategory()==Biome.Category.PLAINS)
		{
			event.getSpawns().addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ENGEntities.VALKYRIE, 100, 1, 4));
		}
	}
	
	@SubscribeEvent
	public static void disableItemGravity(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof ItemEntity)
		{
			ItemEntity item=(ItemEntity)event.getEntity();
			if(item.getItem().getItem()==Items.APPLE)
			{
				item.setNoGravity(true);
				//item.setDeltaMovement(item.getDeltaMovement().add(0D, 1D, 0D));
			}
		}
	}
}