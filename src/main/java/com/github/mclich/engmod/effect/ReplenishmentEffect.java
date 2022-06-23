package com.github.mclich.engmod.effect;

import com.github.mclich.engmod.register.ENGCapabilities;
import com.github.mclich.engmod.register.ENGMobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;

public class ReplenishmentEffect extends MobEffect
{
	public static final String ID="replenishment";

	public ReplenishmentEffect()
	{
		super(MobEffectCategory.BENEFICIAL, 0x007DFB);
	}
	
	public static MobEffectInstance getInstance()
	{
		return new MobEffectInstance(ENGMobEffects.REPLENISHMENT.get(), 400, 0);
	}
	
	public static MobEffectInstance getActivationInstance()
	{
		return new MobEffectInstance(ENGMobEffects.REPLENISHMENT.get(), 100, 1);
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return duration%(amplifier>0?5:40)==0;
	}
	
	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		if(!entity.getLevel().isClientSide()&&entity instanceof ServerPlayer)
		{
			entity.getCapability(ENGCapabilities.MANA).ifPresent
			(
				mana->
				{
					if(mana.getStatus()) mana.setAndUpdateMana((ServerPlayer)entity, mana.getMana()+1);
				}
			);
		}
	}
}