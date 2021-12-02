package com.github.mclich.engmod.data.capability;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.data.handler.IManaHandler;
import com.github.mclich.engmod.data.handler.ManaHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ManaCapability
{
	public static final ResourceLocation LOCATION=new ResourceLocation(ElderNorseGods.MOD_ID, "mana");
	
	@CapabilityInject(IManaHandler.class)
	public static Capability<IManaHandler> CAP_INSTANCE=null;
	
	public static class ManaStorage implements IStorage<IManaHandler>
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
	
	public static class ManaProvider implements ICapabilitySerializable<CompoundNBT>
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
}