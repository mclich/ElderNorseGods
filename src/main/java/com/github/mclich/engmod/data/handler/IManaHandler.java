package com.github.mclich.engmod.data.handler;

import net.minecraft.entity.player.ServerPlayerEntity;

public interface IManaHandler
{
	float getMana();
	
	boolean getStatus();
	
	void setMana(float amount);
	
	void setStatus(boolean status);
	
	void update(ServerPlayerEntity player);
	
	default void setAndUpdateMana(ServerPlayerEntity player, float amount)
	{
		this.setMana(amount);
		this.update(player);
	}

	@SuppressWarnings("unused")
	default void setAndUpdateStatus(ServerPlayerEntity player, boolean status)
	{
		this.setStatus(status);
		this.update(player);
	}
	
	default void setManaAndStatus(float amount, boolean status)
	{
		this.setMana(amount);
		this.setStatus(status);
	}
}