package com.github.mclich.engmod.block;

import com.github.mclich.engmod.register.ENGItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class HopBushBlock extends SweetBerryBushBlock
{
	private static final VoxelShape SAPLING_SHAPE=Block.box(3D, 0D, 3D, 13D, 8D, 13D);
	private static final VoxelShape MID_GROWTH_SHAPE=Block.box(1D, 0D, 1D, 15D, 15D, 15D);
	
	public static final String ID="hop_bush";
	
	public HopBushBlock()
	{
		super(Block.Properties.of(Material.PLANT).randomTicks().noOcclusion().noCollission().sound(SoundType.SWEET_BERRY_BUSH));
	}
	
	@Override
	public ItemStack getCloneItemStack(IBlockReader ibReader, BlockPos blockPos, BlockState blockState)
	{
		return new ItemStack(ENGItems.HOP.get());
	}
	
	@Override
	public void entityInside(BlockState blockState, World world, BlockPos blockPos, Entity entity)
	{
		if(entity instanceof LivingEntity&&entity.getType()!=EntityType.BEE) entity.makeStuckInBlock(blockState, new Vector3d(0.9D, 0.9D, 0.9D));
	}
	
	@Override
	public VoxelShape getShape(BlockState blockState, IBlockReader ibReader, BlockPos blockPos, ISelectionContext context)
	{
		if(blockState.getValue(HopBushBlock.AGE)==0) return HopBushBlock.SAPLING_SHAPE;
		else return HopBushBlock.MID_GROWTH_SHAPE;
	}

	@Override
	public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockHitResult)
	{
		int i=blockState.getValue(HopBushBlock.AGE);
		boolean flag=i==3;
		if(!flag&&playerEntity.getItemInHand(hand).getItem()==Items.BONE_MEAL) return ActionResultType.PASS;
		else if(i>1)
		{
			int j=1+world.random.nextInt(2);
			Block.popResource(world, blockPos, new ItemStack(ENGItems.HOP.get(), j+(flag?1:0)));
			world.playSound((PlayerEntity)null, blockPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F+world.random.nextFloat()*0.4F);
			world.setBlock(blockPos, blockState.setValue(HopBushBlock.AGE, Integer.valueOf(1)), 2);
			return ActionResultType.sidedSuccess(world.isClientSide());
		}
		else return super.use(blockState, world, blockPos, playerEntity, hand, blockHitResult);
	}
}