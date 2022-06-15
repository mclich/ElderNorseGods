package com.github.mclich.engmod.data.capability;

import net.minecraft.server.level.ServerPlayer;

public interface IManaStorage
{
	float getMana();
	
	boolean getStatus();
	
	void setMana(float amount);
	
	void setStatus(boolean status);
	
	void update(ServerPlayer player);
	
	default void setAndUpdateMana(ServerPlayer player, float amount)
	{
		this.setMana(amount);
		this.update(player);
	}

	@SuppressWarnings("unused")
	default void setAndUpdateStatus(ServerPlayer player, boolean status)
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