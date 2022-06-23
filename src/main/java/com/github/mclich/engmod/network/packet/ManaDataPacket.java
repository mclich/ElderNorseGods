package com.github.mclich.engmod.network.packet;

import com.github.mclich.engmod.register.ENGCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class ManaDataPacket
{
	private float amount;
	private boolean status;
	
	public ManaDataPacket(float amount, boolean status)
	{
		this.amount=amount;
		this.status=status;
	}
	
	public static void encode(ManaDataPacket packet, FriendlyByteBuf buffer)
	{
		buffer.writeFloat(packet.amount);
		buffer.writeBoolean(packet.status);
	}
	
	public static ManaDataPacket decode(FriendlyByteBuf buffer)
	{
		return new ManaDataPacket(buffer.readFloat(), buffer.readBoolean());
	}
	
	public static void handle(ManaDataPacket packet, Supplier<NetworkEvent.Context> context)
	{
		context.get().enqueueWork(()->DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->PacketHandler.handlePacket(packet, context)));
		context.get().setPacketHandled(true);
	}
	
	private static class PacketHandler
	{
		private static void handlePacket(ManaDataPacket packet, @SuppressWarnings("unused") Supplier<NetworkEvent.Context> context)
		{
			Minecraft mc=Minecraft.getInstance();
			mc.player.getCapability(ENGCapabilities.MANA).ifPresent
			(
				mana->
				{
					mana.setMana(packet.amount);
					mana.setStatus(packet.status);
				}
			);
		}
	}
}