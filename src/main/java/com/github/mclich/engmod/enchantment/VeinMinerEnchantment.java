package com.github.mclich.engmod.enchantment;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGEnchantments;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags.Blocks;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.FORGE)
public class VeinMinerEnchantment extends Enchantment
{
	public static final String ID="vein_miner";
	
	public VeinMinerEnchantment()
	{
		super(Rarity.RARE, EnchantmentType.create("pickaxe", item->item instanceof PickaxeItem), new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
	}
	
	@SubscribeEvent
	public static void onBlockDestroy(BreakEvent event)
	{
		if(!event.getWorld().isClientSide()&&EnchantmentHelper.getItemEnchantmentLevel(ENGEnchantments.VEIN_MINER.get(), event.getPlayer().getMainHandItem())>0&&event.getState().is(Blocks.ORES))
		{
			VeinMinerEnchantment.mineVein((ServerPlayerEntity)event.getPlayer(), (World)event.getWorld(), event.getPos(), event.getState(), event.getState().getBlock());
		}
	}
	
	private static void mineVein(ServerPlayerEntity player, World world, BlockPos startPos, BlockState blockState, Block ore)
	{
		ElderNorseGods.LOGGER.info("mineVein");
		if(player.gameMode.isSurvival())
		{
			blockState.getBlock().playerDestroy(world, player, startPos, blockState, null, player.getMainHandItem());
		}
		world.destroyBlock(startPos, false, player);
		if(world.getBlockState(startPos.above()).getBlock()==ore) VeinMinerEnchantment.mineVein(player, world, startPos.above(), world.getBlockState(startPos.above()), ore);
		if(world.getBlockState(startPos.below()).getBlock()==ore) VeinMinerEnchantment.mineVein(player, world, startPos.below(), world.getBlockState(startPos.below()), ore);
		if(world.getBlockState(startPos.north()).getBlock()==ore) VeinMinerEnchantment.mineVein(player, world, startPos.north(), world.getBlockState(startPos.north()), ore);
		if(world.getBlockState(startPos.south()).getBlock()==ore) VeinMinerEnchantment.mineVein(player, world, startPos.south(), world.getBlockState(startPos.south()), ore);
		if(world.getBlockState(startPos.east()).getBlock()==ore) VeinMinerEnchantment.mineVein(player, world, startPos.east(), world.getBlockState(startPos.east()), ore);
		if(world.getBlockState(startPos.west()).getBlock()==ore) VeinMinerEnchantment.mineVein(player, world, startPos.west(), world.getBlockState(startPos.west()), ore);
	}
	
	@Override
	public int getMaxLevel()
	{
		return 1;
	}

	@Override
	public int getMinCost(int amount)
	{
		return amount*15;
	}
	
	@Override
	public int getMaxCost(int amount)
	{
		return this.getMinCost(amount)+45;
	}
	
	@Override
	public boolean isTreasureOnly()
	{
		return true;
	}
}