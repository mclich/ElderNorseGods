package com.github.mclich.engmod.network;

import com.github.mclich.engmod.ElderNorseGods;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public abstract class NetworkHandler
{
	private static final String PROTOCOL_VERSION="1";
	private static final SimpleChannel CHANNEL=NetworkRegistry.newSimpleChannel
	(
		new ResourceLocation(ElderNorseGods.MOD_ID, "network"),
		()->NetworkHandler.PROTOCOL_VERSION,
		NetworkHandler.PROTOCOL_VERSION::equals,
		NetworkHandler.PROTOCOL_VERSION::equals
	);
	
	public static <MSG> void sendToServer(MSG msg)
	{
		NetworkHandler.CHANNEL.sendToServer(msg);
	}
	
	public static <MSG> void sendToPlayer(ServerPlayerEntity player, MSG msg)
	{
		NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(()->player), msg);
	}
	
	public static <MSG> void sendToTrackingEntity(Entity entity, MSG msg)
	{
		NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(()->entity), msg);
	}
	
	public static <MSG> void sendToTrackingChunk(Chunk chunk, MSG msg)
	{
		NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(()->chunk), msg);
	}

	public static <MSG> void sendToAllPlayers(MSG msg)
	{
		NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), msg);
	}
	
	public static SimpleChannel getChannel()
	{
		return NetworkHandler.CHANNEL;
	}
}