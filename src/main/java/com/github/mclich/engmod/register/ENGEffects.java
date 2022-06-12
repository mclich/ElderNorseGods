package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.effect.*;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ENGEffects
{
	public static final DeferredRegister<Effect> EFFECTS=DeferredRegister.create(ForgeRegistries.POTIONS, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<Effect> ANEMIA=ENGEffects.EFFECTS.register(AnemiaEffect.ID, AnemiaEffect::new);
	public static final RegistryObject<Effect> LOSS=ENGEffects.EFFECTS.register(LossEffect.ID, LossEffect::new);
	public static final RegistryObject<Effect> DRUNKENNESS=ENGEffects.EFFECTS.register(DrunkennessEffect.ID, DrunkennessEffect::new);
	public static final RegistryObject<Effect> REPLENISHMENT=ENGEffects.EFFECTS.register(ReplenishmentEffect.ID, ReplenishmentEffect::new);
}