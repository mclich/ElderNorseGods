package com.github.mclich.engmod.particle;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.multiplayer.ClientLevel;

public class StaffParticle extends TextureSheetParticle
{
	public static final String ID="staff_particle";
	
	private final SpriteSet sprites;
	private int angle;
	
	public StaffParticle(ClientLevel world, SpriteSet sprites, int color, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		this.angle=0;
		this.lifetime=20;
		this.sprites=sprites;
		this.pickSprite(this.sprites);
		this.setColor((color>>16&255)/255F, (color>>8&255)/255F, (color&255)/255F);
	}
	
	@Override
	public void tick()
	{
		this.xo=this.x;
		this.yo=this.y;
		this.zo=this.z;
		if(this.age++>=this.lifetime) this.remove();
		else
		{
			this.xd=this.angle*Math.cos(Math.PI*this.angle/180)*0.0008;
			this.yd=this.age*0.01;
			this.zd=this.angle*Math.sin(Math.PI*this.angle/180)*0.0008;
			this.angle+=80;
			this.move(this.xd, this.yd, this.zd);
			if(this.age%10==0) this.pickSprite(this.sprites);
			this.xd*=0.98;
			this.yd*=0.98;
			this.zd*=0.98;
			if(this.onGround)
			{
				this.xd*=0.7;
				this.zd*=0.7;
			}
		}
	}
	
	@Override
	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}
	
	public static class Factory implements ParticleProvider<StaffParticleOptions>
	{
		private final SpriteSet sprite;
		
		public Factory(SpriteSet sprite)
		{
			this.sprite=sprite;
		}

		@Override
		public Particle createParticle(StaffParticleOptions options, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
		{
			return new StaffParticle(world, this.sprite, options.getColor(), x, y, z, xSpeed, ySpeed, zSpeed);
		}
	}
}