package com.github.mclich.engmod.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class ItemActivationPacket
{
	private ItemStack toActivate;
	
	public ItemActivationPacket(ItemStack itemStack)
	{
		this.toActivate=itemStack;
	}
	
	public static void encode(ItemActivationPacket packet, FriendlyByteBuf buffer)
	{
		buffer.writeItem(packet.toActivate);
	}
	
	public static ItemActivationPacket decode(FriendlyByteBuf buffer)
	{
		return new ItemActivationPacket(buffer.readItem());
	}
	
	public static void handle(ItemActivationPacket packet, Supplier<NetworkEvent.Context> context)
	{
		context.get().enqueueWork(()->DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->PacketHandler.handlePacket(packet, context)));
		context.get().setPacketHandled(true);
	}
	
	private static class PacketHandler
	{
		private static void handlePacket(ItemActivationPacket packet, @SuppressWarnings("unused") Supplier<NetworkEvent.Context> context)
		{
			Minecraft.getInstance().gameRenderer.displayItemActivation(packet.toActivate);
		}
	}
}