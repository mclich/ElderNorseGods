package com.github.mclich.engmod.block;

import com.github.mclich.engmod.register.ENGItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

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
	public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState)
	{
		return new ItemStack(ENGItems.HOP.get());
	}
	
	@Override
	public void entityInside(BlockState blockState, Level world, BlockPos blockPos, Entity entity)
	{
		if(entity instanceof LivingEntity&&entity.getType()!=EntityType.BEE) entity.makeStuckInBlock(blockState, new Vec3(0.9D, 0.9D, 0.9D));
	}
	
	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context)
	{
		if(blockState.getValue(HopBushBlock.AGE)==0) return HopBushBlock.SAPLING_SHAPE;
		else return HopBushBlock.MID_GROWTH_SHAPE;
	}

	@Override
	public InteractionResult use(BlockState blockState, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult)
	{
		int i=blockState.getValue(HopBushBlock.AGE);
		boolean flag=i==3;
		if(!flag&&player.getItemInHand(hand).getItem()==Items.BONE_MEAL) return InteractionResult.PASS;
		else if(i>1)
		{
			int j=1+world.random.nextInt(2);
			Block.popResource(world, blockPos, new ItemStack(ENGItems.HOP.get(), j+(flag?1:0)));
			world.playSound((Player)null, blockPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F+world.random.nextFloat()*0.4F);
			world.setBlock(blockPos, blockState.setValue(HopBushBlock.AGE, 1), 2);
			return InteractionResult.sidedSuccess(world.isClientSide());
		}
		else return super.use(blockState, world, blockPos, player, hand, blockHitResult);
	}
}