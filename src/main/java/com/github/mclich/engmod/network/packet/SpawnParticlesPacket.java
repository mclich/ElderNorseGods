package com.github.mclich.engmod.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.UUID;
import java.util.function.Supplier;

public class SpawnParticlesPacket
{
	private ParticleOptions particle;
	private UUID player;
	private int lifeTime;
	
	public SpawnParticlesPacket(ParticleOptions particle, UUID player, int lifeTime)
	{
		this.particle=particle;
		this.player=player;
		this.lifeTime=lifeTime;
	}
	
	public static void encode(SpawnParticlesPacket packet, FriendlyByteBuf buffer)
	{
		buffer.writeRegistryId(ForgeRegistries.PARTICLE_TYPES, packet.particle.getType());
		buffer.writeUUID(packet.player);
		buffer.writeInt(packet.lifeTime);
	}
	
	public static SpawnParticlesPacket decode(FriendlyByteBuf buffer)
	{
		return new SpawnParticlesPacket(buffer.readRegistryId(), buffer.readUUID(), buffer.readInt());
	}
	
	public static void handle(SpawnParticlesPacket packet, Supplier<NetworkEvent.Context> context)
	{
		context.get().enqueueWork(()->DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->PacketHandler.handlePacket(packet, context)));
		context.get().setPacketHandled(true);
	}
	
	private static class PacketHandler
	{
		private static void handlePacket(SpawnParticlesPacket packet, @SuppressWarnings("unused") Supplier<NetworkEvent.Context> context)
		{
			Minecraft mc=Minecraft.getInstance();
			mc.particleEngine.createTrackingEmitter(mc.level.getPlayerByUUID(packet.player), packet.particle, packet.lifeTime);
		}
	}
}