package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.effect.DrunkennessEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ENGEffects
{
	public static final DeferredRegister<Effect> EFFECTS=DeferredRegister.create(ForgeRegistries.POTIONS, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<Effect> DRUNKENNESS=ENGEffects.EFFECTS.register(DrunkennessEffect.ID, DrunkennessEffect::new);
}