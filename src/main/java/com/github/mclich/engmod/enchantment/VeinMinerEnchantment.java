package com.github.mclich.engmod.enchantment;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGEnchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags.Blocks;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class VeinMinerEnchantment extends Enchantment
{
	public static final String ID="vein_miner";
	
	public VeinMinerEnchantment()
	{
		super(Rarity.RARE, ENGEnchantments.Types.PICKAXE_ONLY, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	}
	
	@Override
	public int getMaxLevel()
	{
		return 1;
	}

	@Override
	public int getMinCost(int level)
	{
		return level*15;
	}
	
	@Override
	public int getMaxCost(int level)
	{
		return this.getMinCost(level)+45;
	}
	
	@Override
	public boolean isTreasureOnly()
	{
		return true;
	}
	
	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
	private static abstract class EventHandler
	{
		private static void mineVein(ServerPlayer player, Level world, BlockPos startPos, BlockState blockState, Block ore)
		{
			if(player.gameMode.isSurvival())
			{
				blockState.getBlock().playerDestroy(world, player, startPos, blockState, null, player.getMainHandItem());
			}
			world.destroyBlock(startPos, false, player);
			if(world.getBlockState(startPos.above()).getBlock()==ore) EventHandler.mineVein(player, world, startPos.above(), world.getBlockState(startPos.above()), ore);
			if(world.getBlockState(startPos.below()).getBlock()==ore) EventHandler.mineVein(player, world, startPos.below(), world.getBlockState(startPos.below()), ore);
			if(world.getBlockState(startPos.north()).getBlock()==ore) EventHandler.mineVein(player, world, startPos.north(), world.getBlockState(startPos.north()), ore);
			if(world.getBlockState(startPos.south()).getBlock()==ore) EventHandler.mineVein(player, world, startPos.south(), world.getBlockState(startPos.south()), ore);
			if(world.getBlockState(startPos.east()).getBlock()==ore) EventHandler.mineVein(player, world, startPos.east(), world.getBlockState(startPos.east()), ore);
			if(world.getBlockState(startPos.west()).getBlock()==ore) EventHandler.mineVein(player, world, startPos.west(), world.getBlockState(startPos.west()), ore);
		}
		
		@SubscribeEvent
		public static void blockDestroy(BreakEvent event)
		{
			if(!event.getWorld().isClientSide()&&EnchantmentHelper.getItemEnchantmentLevel(ENGEnchantments.VEIN_MINER.get(), event.getPlayer().getMainHandItem())>0&&event.getState().is(Blocks.ORES))
			{
				EventHandler.mineVein((ServerPlayer)event.getPlayer(), (Level)event.getWorld(), event.getPos(), event.getState(), event.getState().getBlock());
			}
		}
	}
}