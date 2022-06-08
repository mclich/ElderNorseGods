package com.github.mclich.engmod.network.packet;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class ItemActivationPacket
{
	private ItemStack toActivate;
	
	public ItemActivationPacket(ItemStack itemStack)
	{
		this.toActivate=itemStack;
	}
	
	public static void encode(ItemActivationPacket packet, PacketBuffer buffer)
	{
		buffer.writeItem(packet.toActivate);
	}
	
	public static ItemActivationPacket decode(PacketBuffer buffer)
	{
		return new ItemActivationPacket(buffer.readItem());
	}
	
	public static void handle(ItemActivationPacket packet, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(()->DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->PacketHandler.handlePacket(packet, ctx)));
		ctx.get().setPacketHandled(true);
	}
	
	private static class PacketHandler
	{
		private static void handlePacket(ItemActivationPacket packet, @SuppressWarnings("unused") Supplier<NetworkEvent.Context> ctx)
		{
			Minecraft.getInstance().gameRenderer.displayItemActivation(packet.toActivate);
		}
	}
}