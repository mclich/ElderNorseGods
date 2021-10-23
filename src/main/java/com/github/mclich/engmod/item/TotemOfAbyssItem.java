package com.github.mclich.engmod.item;

import java.util.Optional;
import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.network.server.ItemActivationPacket;
import com.github.mclich.engmod.network.server.SpawnParticlesPacket;
import com.github.mclich.engmod.register.ENGItems;
import com.github.mclich.engmod.register.ENGTabs;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.FORGE)
public class TotemOfAbyssItem extends Item
{
	public static final String ID="totem_of_abyss";
	
	public TotemOfAbyssItem()
	{
		super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).tab(ENGTabs.COMBAT));
	}

	@SubscribeEvent
	public static void onVoidDamage(LivingDamageEvent event)
	{
		if(event.getEntityLiving() instanceof PlayerEntity)
		{
			Item totem=ENGItems.TOTEM_OF_ABYSS.get();
			PlayerEntity player=(PlayerEntity)event.getEntityLiving();
			if((player.getMainHandItem().getItem()==totem||player.getOffhandItem().getItem()==totem)&&event.getSource()==DamageSource.OUT_OF_WORLD)
			{
				event.setAmount(0F);
			}
		}
	}
	
	@Override
	public void inventoryTick(ItemStack itemStack, World world, Entity entity, int tick, boolean flag)
	{
		if(!world.isClientSide()&&entity instanceof PlayerEntity&&entity.getY()<0&&((PlayerEntity)entity).getLastDamageSource()==DamageSource.OUT_OF_WORLD)
		{
			ServerPlayerEntity player=(ServerPlayerEntity)entity;
			if(player.getMainHandItem().getItem()==this) TotemOfAbyssItem.activateTotem((ServerWorld)world, player, player.getMainHandItem());
			else if(player.getOffhandItem().getItem()==this) TotemOfAbyssItem.activateTotem((ServerWorld)world, player, player.getOffhandItem());
		}
	}
	
	private static void activateTotem(ServerWorld world, ServerPlayerEntity player, ItemStack itemStack)
	{
		player.fallDistance=0F;
		ItemStack totem=itemStack.copy();
		if(player.gameMode.isSurvival()) itemStack.shrink(1);
		if(world.dimension()==World.END) player.moveTo(100.5F, 49F, 0.5F);
		else
		{
			ServerWorld spawnDimension=world.getServer().getLevel(player.getRespawnDimension());
			Optional<Vector3d> o=PlayerEntity.findRespawnPositionAndUseSpawnBlock(spawnDimension, player.getRespawnPosition(), player.getRespawnAngle(), player.isRespawnForced(), false);
			if(o.isPresent())
			{
				Vector3d playerSpawn=o.get();
				if(world==spawnDimension) player.moveTo(playerSpawn);
				else
				{
					world=spawnDimension;
					player.teleportTo(world, playerSpawn.x, playerSpawn.y, playerSpawn.z, TotemOfAbyssItem.getResRot(player.getRespawnPosition(), playerSpawn), 0F);
				}
				if(world.getBlockState(player.getRespawnPosition()).is(Blocks.RESPAWN_ANCHOR))
				{
					world.playSound(null, player.getRespawnPosition(), SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundCategory.BLOCKS, 1F, 1F);
				}
			}
			else
			{
				IWorldInfo worldSpawn=world.getLevelData();
				if(world.dimension()==World.OVERWORLD) player.moveTo(worldSpawn.getXSpawn(), worldSpawn.getYSpawn(), worldSpawn.getZSpawn());
				else
				{
					world=world.getServer().getLevel(World.OVERWORLD);
					player.teleportTo(world, worldSpawn.getXSpawn(), worldSpawn.getYSpawn(), worldSpawn.getZSpawn(), player.getRespawnAngle(), 0F);
				}
				player.connection.send(new SChangeGameStatePacket(SChangeGameStatePacket.NO_RESPAWN_BLOCK_AVAILABLE, 0F));
			}
		}
		final ServerWorld tmp=world;
		ElderNorseGods.getChannel().send(PacketDistributor.PLAYER.with(()->player), new ItemActivationPacket(totem));
		ElderNorseGods.getChannel().send(PacketDistributor.TRACKING_CHUNK.with(()->tmp.getChunkAt(player.blockPosition())), new SpawnParticlesPacket(ParticleTypes.PORTAL, player.getUUID(), 30));
		world.playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundCategory.PLAYERS, 1F, 1F);
		//player.playSound(SoundEvents.TOTEM_USE, 1F, 1F);
	}
	
	private static float getResRot(BlockPos blockSpawnPos, Vector3d playerSpawnPos)
	{
		Vector3d vector=Vector3d.atBottomCenterOf(blockSpawnPos).subtract(playerSpawnPos).normalize();
        return (float)MathHelper.wrapDegrees(MathHelper.atan2(vector.z, vector.x)*(double)(180F/(float)Math.PI)-90D);
	}
}