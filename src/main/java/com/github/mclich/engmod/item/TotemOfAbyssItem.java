package com.github.mclich.engmod.item;

import java.util.Optional;
import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.network.NetworkHandler;
import com.github.mclich.engmod.network.packet.ItemActivationPacket;
import com.github.mclich.engmod.network.packet.SpawnParticlesPacket;
import com.github.mclich.engmod.register.ENGItems;
import com.github.mclich.engmod.register.ENGTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class TotemOfAbyssItem extends Item
{
	public static final String ID="totem_of_abyss";
	
	public TotemOfAbyssItem()
	{
		super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).tab(ENGTabs.COMBAT));
	}
	
	private float getResRot(BlockPos blockSpawnPos, Vec3 playerSpawnPos)
	{
		Vec3 vector=Vec3.atBottomCenterOf(blockSpawnPos).subtract(playerSpawnPos).normalize();
        return (float)Mth.wrapDegrees(Mth.atan2(vector.z, vector.x)*(double)(180F/(float)Math.PI)-90D);
	}
	
	private void activateTotem(ServerLevel world, ServerPlayer player, ItemStack itemStack)
	{
		player.fallDistance=0F;
		ItemStack totem=itemStack.copy();
		if(player.gameMode.isSurvival()) itemStack.shrink(1);
		if(world.dimension()==Level.END) player.moveTo(100.5F, 49F, 0.5F);
		else
		{
			ServerLevel spawnDimension=world.getServer().getLevel(player.getRespawnDimension());
			Optional<Vec3> o=Player.findRespawnPositionAndUseSpawnBlock(spawnDimension, player.getRespawnPosition(), player.getRespawnAngle(), player.isRespawnForced(), false);
			if(o.isPresent())
			{
				Vec3 playerSpawn=o.get();
				if(world==spawnDimension) player.moveTo(playerSpawn);
				else
				{
					world=spawnDimension;
					player.teleportTo(world, playerSpawn.x, playerSpawn.y, playerSpawn.z, this.getResRot(player.getRespawnPosition(), playerSpawn), 0F);
				}
				if(world.getBlockState(player.getRespawnPosition()).is(Blocks.RESPAWN_ANCHOR))
				{
					world.playSound(null, player.getRespawnPosition(), SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundSource.BLOCKS, 1F, 1F);
				}
			}
			else
			{
				LevelData worldSpawn=world.getLevelData();
				if(world.dimension()==Level.OVERWORLD) player.moveTo(worldSpawn.getXSpawn(), worldSpawn.getYSpawn(), worldSpawn.getZSpawn());
				else
				{
					world=world.getServer().getLevel(Level.OVERWORLD);
					player.teleportTo(world, worldSpawn.getXSpawn(), worldSpawn.getYSpawn(), worldSpawn.getZSpawn(), player.getRespawnAngle(), 0F);
				}
				player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0F));
			}
		}
		NetworkHandler.sendToPlayer(player, new ItemActivationPacket(totem));
		NetworkHandler.sendToTrackingEntity(player, new SpawnParticlesPacket(ParticleTypes.PORTAL, player.getUUID(), 30));
		world.playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1F, 1F);
		//player.playSound(SoundEvents.TOTEM_USE, 1F, 1F);
	}
	
	@Override
	public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int tick, boolean isSelected)
	{
		if(!world.isClientSide()&&entity instanceof Player&&entity.getY()<0&&((Player)entity).getLastDamageSource()==DamageSource.OUT_OF_WORLD)
		{
			ServerPlayer player=(ServerPlayer)entity;
			if(player.getMainHandItem().getItem()==this) this.activateTotem((ServerLevel)world, player, player.getMainHandItem());
			else if(player.getOffhandItem().getItem()==this) this.activateTotem((ServerLevel)world, player, player.getOffhandItem());
		}
	}
	
	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
	private static abstract class EventHandler
	{
		@SubscribeEvent
		public static void onVoidDamage(LivingDamageEvent event)
		{
			if(event.getEntityLiving() instanceof Player player)
			{
				Item totem=ENGItems.TOTEM_OF_ABYSS.get();
				if((player.getMainHandItem().getItem()==totem||player.getOffhandItem().getItem()==totem)&&event.getSource()==DamageSource.OUT_OF_WORLD)
				{
					event.setAmount(0F);
				}
			}
		}
	}
}