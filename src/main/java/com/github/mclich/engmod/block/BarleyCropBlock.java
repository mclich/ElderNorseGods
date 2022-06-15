package com.github.mclich.engmod.block;

import com.github.mclich.engmod.register.ENGItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public class BarleyCropBlock extends CropBlock
{
	private static final IntegerProperty AGE=BlockStateProperties.AGE_3;
	private static final VoxelShape[] SHAPES=new VoxelShape[]{Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D)};
	
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
	protected ItemLike getBaseSeedId()
	{
		return ENGItems.BARLEY_SEEDS.get();
	}
	
	@Override
	protected int getBonemealAgeIncrease(Level world)
	{
		return Mth.nextInt(world.getRandom(), 1, 2);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder)
	{
		builder.add(BarleyCropBlock.AGE);
	}
	
	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context)
	{
		return BarleyCropBlock.SHAPES[blockState.getValue(this.getAgeProperty())];
	}
}