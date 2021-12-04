package com.github.mclich.engmod.effect;

import com.github.mclich.engmod.data.capability.ManaCapability;
import com.github.mclich.engmod.register.ENGEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

public class ReplenishmentEffect extends Effect
{
	public static final String ID="replenishment";

	public ReplenishmentEffect()
	{
		super(EffectType.BENEFICIAL, 0x007dfb);
	}
	
	public static EffectInstance getInstance()
	{
		return new EffectInstance(ENGEffects.REPLENISHMENT.get(), 400, 0);
	}
	
	public static EffectInstance getActivationInstance()
	{
		return new EffectInstance(ENGEffects.REPLENISHMENT.get(), 100, 1);
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return duration%(amplifier>0?5:40)==0;
	}
	
	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		if(!entity.getCommandSenderWorld().isClientSide()&&entity instanceof ServerPlayerEntity)
		{
			entity.getCapability(ManaCapability.CAP_INSTANCE).ifPresent
			(
				mana->
				{
					if(mana.getStatus()) mana.setAndUpdateMana((ServerPlayerEntity)entity, mana.getMana()+1);
				}
			);
		}
	}
}