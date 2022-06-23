package com.github.mclich.engmod.block;

import com.github.mclich.engmod.entity.block.BreweryBlockEntity;
import com.github.mclich.engmod.register.ENGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import javax.annotation.Nullable;

public class BreweryBlock extends HorizontalDirectionalBlock implements EntityBlock
{
	public static final String ID="brewery";
	public static final BooleanProperty HAS_FUEL=BooleanProperty.create("has_fuel");
	public static final BooleanProperty LIT=BlockStateProperties.LIT;
	
	private static final VoxelShape SHAPE_TOP=Block.box(2.0D, 14.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	private static final VoxelShape SHAPE_BOX=Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);
	
	public BreweryBlock()
	{
		super(Block.Properties.of(Material.STONE).strength(3.5F).lightLevel(bs->bs.getValue(BreweryBlock.HAS_FUEL)?bs.getValue(BreweryBlock.LIT)?13:6:0).sound(SoundType.STONE));
		this.registerDefaultState(this.stateDefinition.any().setValue(BreweryBlock.FACING, Direction.NORTH).setValue(BreweryBlock.HAS_FUEL, false).setValue(BreweryBlock.LIT, false));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
	{
		return new BreweryBlockEntity(blockPos, blockState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState blockState, BlockEntityType<T> blockEntityType)
	{
		return blockEntityType==ENGBlockEntities.BREWERY_ENTITY.get()?BreweryBlockEntity::tick:null;
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder)
	{
		builder.add(BreweryBlock.FACING, BreweryBlock.HAS_FUEL, BreweryBlock.LIT);
	}
	
	@Override
	public BlockState getStateForPlacement(@Nullable BlockPlaceContext context)
	{
		if(context==null) return this.defaultBlockState();
		return this.defaultBlockState().setValue(BreweryBlock.FACING, context.getHorizontalDirection().getOpposite());
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context)
	{
		return Shapes.or(BreweryBlock.SHAPE_TOP, BreweryBlock.SHAPE_BOX);
	}

	@Override
	public void animateTick(BlockState blockState, Level world, BlockPos blockPos, RandomSource random)
	{
		if(blockState.getValue(BreweryBlock.HAS_FUEL))
		{
			double d0=blockPos.getX()+0.5D;
			double d1=blockPos.getY();
			double d2=blockPos.getZ()+0.5D;
			Direction dir=blockState.getValue(BreweryBlock.FACING);
			double d4=random.nextDouble()*0.6D-0.3D;
			if(random.nextDouble()<0.2D&&(dir==Direction.NORTH||dir==Direction.SOUTH))
			{
				world.addParticle(ParticleTypes.DRIPPING_LAVA, d0-0.47D, d1+0.2D, d2+d4, 0D, 0D, 0D);
				world.addParticle(ParticleTypes.DRIPPING_LAVA, d0+0.47D, d1+0.2D, d2+d4, 0D, 0D, 0D);
			}
			else if(random.nextDouble()<0.2D&&(dir==Direction.EAST||dir==Direction.WEST))
			{
				world.addParticle(ParticleTypes.DRIPPING_LAVA, d0+d4, d1+0.2D, d2-0.47D, 0D, 0D, 0D);
				world.addParticle(ParticleTypes.DRIPPING_LAVA, d0+d4, d1+0.2D, d2+0.47D, 0D, 0D, 0D);
			}
			if(blockState.getValue(BreweryBlock.LIT))
			{
				if(random.nextDouble()<0.2D) world.playLocalSound(d0, d1, d2, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
				world.addParticle(ParticleTypes.SMOKE, d0, d1+1D, d2, 0D, 0D, 0D);
			}
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState oldBlockState, Level world, BlockPos blockPos, BlockState newBlockState, boolean isMoving)
	{
		if(!oldBlockState.is(newBlockState.getBlock()))
		{
			BlockEntity blockEntity=world.getBlockEntity(blockPos);
			if(blockEntity instanceof BreweryBlockEntity breweryEntity)
			{
				Containers.dropContents(world, blockPos, breweryEntity);
				breweryEntity.awardExperience(world, Vec3.atCenterOf(blockPos));
				world.updateNeighbourForOutputSignal(blockPos, this);
			}
			super.onRemove(oldBlockState, world, blockPos, newBlockState, isMoving);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState blockState, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult)
	{
		if(world.isClientSide()) return InteractionResult.SUCCESS;
		else
		{
			BlockEntity blockEntity=world.getBlockEntity(blockPos);
			if(blockEntity instanceof BreweryBlockEntity)
			{
				NetworkHooks.openGui((ServerPlayer)player, (BreweryBlockEntity)blockEntity, blockEntity.getBlockPos());
			}
			return InteractionResult.CONSUME;
		}
	}
}