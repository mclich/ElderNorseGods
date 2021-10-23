package com.github.mclich.engmod.block;

import com.github.mclich.engmod.register.ENGItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BarleyCropBlock extends CropsBlock
{
	private static final IntegerProperty AGE=BlockStateProperties.AGE_3;
	private static final VoxelShape[] SHAPES=new VoxelShape[]{Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D)};
	
	public static final String ID="barley_crop";
	
	public BarleyCropBlock()
	{
		super(Block.Properties.of(Material.PLANT).randomTicks().noCollission().instabreak().sound(SoundType.CROP));
	}
	
	@Override
	public IntegerProperty getAgeProperty()
	{
		return BarleyCropBlock.AGE;
	}
	
	@Override
	public int getMaxAge()
	{
		return 3;
	}
	
	@Override
	protected IItemProvider getBaseSeedId()
	{
		return ENGItems.BARLEY_SEEDS.get();
	}
	
	@Override
	protected int getBonemealAgeIncrease(World world)
	{
		return MathHelper.nextInt(world.random, 1, 3);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder)
	{
		builder.add(BarleyCropBlock.AGE);
	}
	
	@Override
	public VoxelShape getShape(BlockState blockState, IBlockReader ibReader, BlockPos blockPos, ISelectionContext context)
	{
		return BarleyCropBlock.SHAPES[blockState.getValue(this.getAgeProperty())];
	}
}