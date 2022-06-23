package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.effect.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ENGMobEffects
{
	public static final DeferredRegister<MobEffect> MOB_EFFECTS=DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<MobEffect> ANEMIA=ENGMobEffects.MOB_EFFECTS.register(AnemiaEffect.ID, AnemiaEffect::new);
	public static final RegistryObject<MobEffect> LOSS=ENGMobEffects.MOB_EFFECTS.register(LossEffect.ID, LossEffect::new);
	public static final RegistryObject<MobEffect> DRUNKENNESS=ENGMobEffects.MOB_EFFECTS.register(DrunkennessEffect.ID, DrunkennessEffect::new);
	public static final RegistryObject<MobEffect> REPLENISHMENT=ENGMobEffects.MOB_EFFECTS.register(ReplenishmentEffect.ID, ReplenishmentEffect::new);
}