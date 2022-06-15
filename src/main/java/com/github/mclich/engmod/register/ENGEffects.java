package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.effect.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ENGEffects
{
	public static final DeferredRegister<MobEffect> EFFECTS=DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<MobEffect> ANEMIA=ENGEffects.EFFECTS.register(AnemiaEffect.ID, AnemiaEffect::new);
	public static final RegistryObject<MobEffect> LOSS=ENGEffects.EFFECTS.register(LossEffect.ID, LossEffect::new);
	public static final RegistryObject<MobEffect> DRUNKENNESS=ENGEffects.EFFECTS.register(DrunkennessEffect.ID, DrunkennessEffect::new);
	public static final RegistryObject<MobEffect> REPLENISHMENT=ENGEffects.EFFECTS.register(ReplenishmentEffect.ID, ReplenishmentEffect::new);
}