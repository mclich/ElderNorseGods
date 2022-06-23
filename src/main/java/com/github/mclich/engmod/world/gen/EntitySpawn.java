package com.github.mclich.engmod.world.gen;

import com.github.mclich.engmod.ElderNorseGods;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
public abstract class EntitySpawn
{
	/*
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void registerSpawns(BiomeLoadingEvent event)
	{
		//event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(ENGModEntities.VALKYRIE_ENTITY.get(), 200, 1, 4));
		if(event.getCategory()==Biome.BiomeCategory.PLAINS)
		{
			event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ENGEntities.VALKYRIE.get(), 100, 1, 4));
		}
	}
	*/
	
	@SubscribeEvent
	public static void disableItemGravity(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof ItemEntity item)
		{
			if(item.getItem().getItem()==Items.APPLE)
			{
				item.setNoGravity(true);
				//item.setDeltaMovement(item.getDeltaMovement().add(0D, 1D, 0D));
			}
		}
	}
}