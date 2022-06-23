package com.github.mclich.engmod.data.provider;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.data.capability.IManaStorage;
import com.github.mclich.engmod.data.capability.ManaStorage;
import com.github.mclich.engmod.register.ENGCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class ManaProvider implements ICapabilitySerializable<CompoundTag>
{
	private final IManaStorage mana=new ManaStorage();
	private final LazyOptional<IManaStorage> lazyMana=LazyOptional.of(()->this.mana);

	@Override
	public CompoundTag serializeNBT()
	{
		CompoundTag tag=new CompoundTag();
		tag.putFloat("ManaAmount", this.mana.getMana());
		tag.putBoolean("ManaStatus", this.mana.getStatus());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag)
	{
		this.mana.setMana(tag.getFloat("ManaAmount"));
		this.mana.setStatus(tag.getBoolean("ManaStatus"));
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return ENGCapabilities.MANA.orEmpty(cap, this.lazyMana);
	}
	
	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
	private static abstract class Synchronizer
	{
		private static void update(ServerPlayer player)
		{
			player.getCapability(ENGCapabilities.MANA).ifPresent(mana->mana.update(player));
		}

		@SubscribeEvent
		public static void loginUpdate(PlayerLoggedInEvent event)
		{
			if(!event.getPlayer().getLevel().isClientSide()) Synchronizer.update((ServerPlayer)event.getPlayer());
		}

		@SubscribeEvent
		public static void dimensionUpdate(PlayerChangedDimensionEvent event)
		{
			if(!event.getPlayer().getLevel().isClientSide()) Synchronizer.update((ServerPlayer)event.getPlayer());
		}

		@SubscribeEvent
		public static void respawnUpdate(PlayerRespawnEvent event)
		{
			if(!event.getPlayer().getLevel().isClientSide()) Synchronizer.update((ServerPlayer)event.getPlayer());
		}

		@SubscribeEvent
		public static void cloneUpdate(PlayerEvent.Clone event)
		{
			if(!event.isWasDeath()) return;
			event.getOriginal().reviveCaps();
			IManaStorage oldMana=event.getOriginal().getCapability(ENGCapabilities.MANA).orElse(null);
			IManaStorage newMana=event.getPlayer().getCapability(ENGCapabilities.MANA).orElse(null);
			if(oldMana!=null&&newMana!=null)
			{
				if(oldMana.getStatus()) newMana.setManaAndStatus(20F, true);
				else newMana.setManaAndStatus(0F, false);
			}
			event.getOriginal().invalidateCaps();
		}
	}
}