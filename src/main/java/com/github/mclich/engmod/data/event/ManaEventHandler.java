package com.github.mclich.engmod.data.event;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.data.capability.ManaCapability;
import com.github.mclich.engmod.data.handler.IManaHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.FORGE)
public abstract class ManaEventHandler
{
	private static void sendUpdates(ServerPlayerEntity player)
	{
		player.getCapability(ManaCapability.CAP_INSTANCE).ifPresent(mana->mana.update(player));
	}
	
	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerChangedDimensionEvent event)
	{
		if(!event.getPlayer().getCommandSenderWorld().isClientSide()) ManaEventHandler.sendUpdates((ServerPlayerEntity)event.getPlayer());
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent event)
	{
		if(!event.getPlayer().getCommandSenderWorld().isClientSide()) ManaEventHandler.sendUpdates((ServerPlayerEntity)event.getPlayer());
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerLoggedInEvent event)
	{
		if(!event.getPlayer().getCommandSenderWorld().isClientSide()) ManaEventHandler.sendUpdates((ServerPlayerEntity)event.getPlayer());
	}
	
	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event)
	{
		if(!event.isWasDeath()) return;
		IManaHandler oldMana=event.getOriginal().getCapability(ManaCapability.CAP_INSTANCE).orElse(null);
		IManaHandler newMana=event.getPlayer().getCapability(ManaCapability.CAP_INSTANCE).orElse(null);
		if(oldMana!=null&&newMana!=null)
		{
			if(oldMana.getStatus()) newMana.setManaAndStatus(20F, true);
			else newMana.setManaAndStatus(0F, false);
		}
	}
}