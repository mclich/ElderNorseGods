package com.github.mclich.engmod.network.packet;

import java.util.function.Supplier;
import com.github.mclich.engmod.data.capability.ManaCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class ManaDataPacket
{
	private float value;
	private boolean status;
	
	public ManaDataPacket(float value, boolean status)
	{
		this.value=value;
		this.status=status;
	}
	
	public static void encode(ManaDataPacket packet, PacketBuffer buffer)
	{
		buffer.writeFloat(packet.value);
		buffer.writeBoolean(packet.status);
	}
	
	public static ManaDataPacket decode(PacketBuffer buffer)
	{
		return new ManaDataPacket(buffer.readFloat(), buffer.readBoolean());
	}
	
	public static void handle(ManaDataPacket packet, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(()->DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->PacketHandler.handlePacket(packet, ctx)));
		ctx.get().setPacketHandled(true);
	}
	
	private static class PacketHandler
	{
		private static void handlePacket(ManaDataPacket packet, @SuppressWarnings("unused") Supplier<NetworkEvent.Context> ctx)
		{
			Minecraft mc=Minecraft.getInstance();
			mc.player.getCapability(ManaCapability.CAP_INSTANCE).ifPresent
			(
				mana->
				{
					mana.setMana(packet.value);
					mana.setStatus(packet.status);
				}
			);
		}
	}
}