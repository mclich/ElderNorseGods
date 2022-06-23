package com.github.mclich.engmod.particle;

import java.util.Locale;
import com.github.mclich.engmod.register.ENGParticles;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class StaffParticleOptions implements ParticleOptions
{
	private final int color;
	
	public StaffParticleOptions(int color)
	{
		this.color=color;
	}
	
	@Override
	public void writeToNetwork(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.color);
	}
	
	@Override
	public String writeToString()
	{
		ResourceLocation loc=ForgeRegistries.PARTICLE_TYPES.getKey(this.getType());
		return String.format(Locale.ROOT, "%s %d %d %d", loc, this.color>>16&255, this.color>>8&255, this.color&255);
	}
	
	public int getColor()
	{
		return this.color;
	}
	
	@Override
	public StaffParticleType getType()
	{
		return ENGParticles.STAFF_PARTICLE.get();
	}
}