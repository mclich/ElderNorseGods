package com.github.mclich.engmod.data.handler;

import com.github.mclich.engmod.network.NetworkHandler;
import com.github.mclich.engmod.network.packet.ManaDataPacket;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ManaHandler implements IManaHandler
{
	private float mana;
	private boolean active;
	
	public ManaHandler()
	{
		this.mana=0F;
		this.active=false;
	}
	@Override
	public float getMana()
	{
		return this.mana;
	}
	
	@Override
	public boolean getStatus()
	{
		return this.active;
	}
	
	@Override
	public void setMana(float amount)
	{
		if(amount<0F) this.mana=0F;
		else this.mana=Math.min(amount, 20F);
	}

	@Override
	public void setStatus(boolean status)
	{
		this.active=status;
	}

	@Override
	public void update(ServerPlayerEntity player)
	{
		NetworkHandler.sendToPlayer(player, new ManaDataPacket(this.getMana(), this.getStatus()));
	}
}