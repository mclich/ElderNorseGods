package com.github.mclich.engmod.network;

import com.github.mclich.engmod.ElderNorseGods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

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

	@SuppressWarnings("unused")
	public static <M> void sendToServer(M message)
	{
		NetworkHandler.CHANNEL.sendToServer(message);
	}
	
	public static <M> void sendToPlayer(ServerPlayer player, M message)
	{
		NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(()->player), message);
	}
	
	public static <M> void sendToTrackingEntity(Entity entity, M message)
	{
		NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(()->entity), message);
	}

	@SuppressWarnings("unused")
	public static <M> void sendToTrackingChunk(LevelChunk chunk, M message)
	{
		NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(()->chunk), message);
	}

	@SuppressWarnings("unused")
	public static <M> void sendToAllPlayers(M message)
	{
		NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), message);
	}
	
	public static SimpleChannel getChannel()
	{
		return NetworkHandler.CHANNEL;
	}
}