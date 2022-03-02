package com.github.mclich.engmod.data.capability;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.data.handler.IManaHandler;
import com.github.mclich.engmod.data.handler.ManaHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ManaCapability
{
	public static final ResourceLocation LOCATION=new ResourceLocation(ElderNorseGods.MOD_ID, "mana");
	
	@CapabilityInject(IManaHandler.class)
	public static Capability<IManaHandler> CAP_INSTANCE=null;
	
	public static class Storage implements IStorage<IManaHandler>
	{
		@Override
		public INBT writeNBT(Capability<IManaHandler> cap, IManaHandler manaHandler, Direction side)
		{
			CompoundNBT tag=new CompoundNBT();
			tag.putFloat("Mana", manaHandler.getMana());
			tag.putBoolean("Status", manaHandler.getStatus());
			return tag;
		}

		@Override
		public void readNBT(Capability<IManaHandler> cap, IManaHandler manaHandler, Direction side, INBT nbt)
		{
			manaHandler.setMana(((CompoundNBT)nbt).getFloat("Mana"));
			manaHandler.setStatus(((CompoundNBT)nbt).getBoolean("Status"));
		}
	}
	
	public static class Provider implements ICapabilitySerializable<CompoundNBT>
	{
		private final ManaHandler mana=new ManaHandler();
		private final LazyOptional<IManaHandler> manaOptional=LazyOptional.of(()->this.mana);
		
	    public void invalidate()
	    {
	    	this.manaOptional.invalidate();
	    }
	    
		@Override
		public CompoundNBT serializeNBT()
		{
			if(ManaCapability.CAP_INSTANCE==null) return new CompoundNBT();
			else return (CompoundNBT)ManaCapability.CAP_INSTANCE.writeNBT(this.mana, null);
		}

		@Override
		public void deserializeNBT(CompoundNBT nbt)
		{
			if(ManaCapability.CAP_INSTANCE!=null) ManaCapability.CAP_INSTANCE.readNBT(this.mana, null, nbt);
		}
		
		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
		{
			return cap==ManaCapability.CAP_INSTANCE?this.manaOptional.cast():LazyOptional.empty();
		}
	}
	
	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
	private static abstract class Helper
	{
		private static void sendUpdates(ServerPlayerEntity player)
		{
			player.getCapability(ManaCapability.CAP_INSTANCE).ifPresent(mana->mana.update(player));
		}
		
		@SubscribeEvent
		public static void dimensionUpdate(PlayerChangedDimensionEvent event)
		{
			if(!event.getPlayer().getCommandSenderWorld().isClientSide()) Helper.sendUpdates((ServerPlayerEntity)event.getPlayer());
		}
		
		@SubscribeEvent
		public static void respawnUpdate(PlayerRespawnEvent event)
		{
			if(!event.getPlayer().getCommandSenderWorld().isClientSide()) Helper.sendUpdates((ServerPlayerEntity)event.getPlayer());
		}
		
		@SubscribeEvent
		public static void loginUpdate(PlayerLoggedInEvent event)
		{
			if(!event.getPlayer().getCommandSenderWorld().isClientSide()) Helper.sendUpdates((ServerPlayerEntity)event.getPlayer());
		}
		
		@SubscribeEvent
		public static void cloneUpdate(PlayerEvent.Clone event)
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
}