package com.github.mclich.engmod.network.packet;

import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class SpawnParticlesPacket
{
	private IParticleData particle;
	private UUID player;
	private int lifetime;
	
	public SpawnParticlesPacket(IParticleData particle, UUID player, int lifetime)
	{
		this.particle=particle;
		this.player=player;
		this.lifetime=lifetime;
	}
	
	public static void encode(SpawnParticlesPacket packet, PacketBuffer buffer)
	{
		buffer.writeRegistryId(packet.particle.getType());
		buffer.writeUUID(packet.player);
		buffer.writeInt(packet.lifetime);
	}
	
	public static SpawnParticlesPacket decode(PacketBuffer buffer)
	{
		return new SpawnParticlesPacket(buffer.readRegistryId(), buffer.readUUID(), buffer.readInt());
	}
	
	public static void handle(SpawnParticlesPacket packet, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(()->DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->PacketHandler.handlePacket(packet, ctx)));
		ctx.get().setPacketHandled(true);
	}
	
	private static class PacketHandler
	{
		private static void handlePacket(SpawnParticlesPacket packet, Supplier<NetworkEvent.Context> ctx)
		{
			Minecraft mc=Minecraft.getInstance();
			mc.particleEngine.createTrackingEmitter(mc.level.getPlayerByUUID(packet.player), packet.particle, packet.lifetime);
		}
	}
}