package com.github.mclich.engmod.block;

import java.util.Random;
import com.github.mclich.engmod.entity.tile.BreweryTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

public class BreweryBlock extends HorizontalBlock
{
	public static final String ID="brewery";
	public static final BooleanProperty HAS_FUEL=BooleanProperty.create("has_fuel");
	public static final BooleanProperty LIT=BlockStateProperties.LIT;
	
	private static final VoxelShape SHAPE_TOP=Block.box(2.0D, 14.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	private static final VoxelShape SHAPE_BOX=Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);
	
	public BreweryBlock()
	{
		super(Block.Properties.of(Material.STONE).strength(3.5F).harvestLevel(0).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().lightLevel(bs->bs.getValue(BreweryBlock.HAS_FUEL)?bs.getValue(BreweryBlock.LIT)?13:6:0).sound(SoundType.STONE));
		this.registerDefaultState(this.stateDefinition.any().setValue(BreweryBlock.FACING, Direction.NORTH).setValue(BreweryBlock.HAS_FUEL, false).setValue(BreweryBlock.LIT, false));
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new BreweryTileEntity();
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder)
	{
		builder.add(BreweryBlock.FACING, BreweryBlock.HAS_FUEL, BreweryBlock.LIT);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		if(context==null) return this.defaultBlockState();
		return this.defaultBlockState().setValue(BreweryBlock.FACING, context.getHorizontalDirection().getOpposite());
	}
	
	@Override
	public VoxelShape getShape(BlockState blockState, IBlockReader ibReader, BlockPos blockPos, ISelectionContext context)
	{
		return VoxelShapes.or(BreweryBlock.SHAPE_TOP, BreweryBlock.SHAPE_BOX);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState blockState, World world, BlockPos blockPos, Random random)
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
				if(random.nextDouble()<0.2D) world.playLocalSound(d0, d1, d2, SoundEvents.SMOKER_SMOKE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
				world.addParticle(ParticleTypes.SMOKE, d0, d1+1D, d2, 0D, 0D, 0D);
			}
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState blockState1, World world, BlockPos blockPos, BlockState blockState2, boolean flag)
	{
		if(!blockState1.is(blockState2.getBlock()))
		{
			TileEntity tileEntity=world.getBlockEntity(blockPos);
			if(tileEntity instanceof BreweryTileEntity)
			{
				BreweryTileEntity bTileEntity=(BreweryTileEntity)tileEntity;
				InventoryHelper.dropContents(world, blockPos, bTileEntity);
				bTileEntity.awardExperience(world, Vector3d.atCenterOf(blockPos));
				world.updateNeighbourForOutputSignal(blockPos, this);
			}
			super.onRemove(blockState1, world, blockPos, blockState2, flag);
		}
	}

	@Override
	public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTrace)
	{
		if(world.isClientSide()) return ActionResultType.SUCCESS;
		else
		{
			TileEntity tileEntity=world.getBlockEntity(blockPos);
			if(tileEntity instanceof BreweryTileEntity)
			{
				NetworkHooks.openGui((ServerPlayerEntity)player, (BreweryTileEntity)tileEntity, tileEntity.getBlockPos());
			}
			return ActionResultType.CONSUME;
		}
	}
}