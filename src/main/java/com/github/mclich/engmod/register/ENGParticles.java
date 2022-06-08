package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.particle.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
public abstract class ENGParticles
{
	public static final DeferredRegister<ParticleType<?>> PARTICLES=DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<StaffParticleType> STAFF_PARTICLE=ENGParticles.PARTICLES.register(StaffParticle.ID, StaffParticleType::new);
	
	@SubscribeEvent
	public static void registerParticles(ParticleFactoryRegisterEvent event)
	{
		ParticleManager particleManager=Minecraft.getInstance().particleEngine;
		particleManager.register(ENGParticles.STAFF_PARTICLE.get(), StaffParticle.Factory::new);
	}
}