package com.github.mclich.engmod.entity.block;

import com.github.mclich.engmod.block.BreweryBlock;
import com.github.mclich.engmod.block.container.BreweryContainer;
import com.github.mclich.engmod.recipe.BrewingRecipe;
import com.github.mclich.engmod.register.ENGBlockEntities;
import com.github.mclich.engmod.register.ENGRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import java.util.Arrays;

public class BreweryBlockEntity extends BaseContainerBlockEntity
{
	public static final int TYPE_BREWERY=15;
	
	private final NonNullList<ItemStack> items=NonNullList.withSize(6, ItemStack.EMPTY);

	private boolean[] brewData;
	private boolean brewFlag;
	private float experience;
	private int totalBrewTime;
	private int brewTime;
	private int fuelBar;
	
	public BreweryBlockEntity(BlockPos blockPos, BlockState blockState)
	{
		super(ENGBlockEntities.BREWERY_ENTITY.get(), blockPos, blockState);
		this.brewData=new boolean[3];
		this.brewFlag=true;
		this.experience=0F;
		this.totalBrewTime=0;
		this.brewTime=0;
		this.fuelBar=0;
	}

	public static <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState blockState, T blockEntity)
	{
		if(blockEntity instanceof BreweryBlockEntity breweryEntity)
		{
			ItemStack fuel=breweryEntity.getItem(3).copy();
			if(breweryEntity.fuelBar<64&&!fuel.isEmpty())
			{
				int itemCount=0,
					burnCount=0,
					currentMaxBurnCount=12800-breweryEntity.fuelBar*200,
					fuelBurnTime=ForgeHooks.getBurnTime(fuel, ENGRecipeTypes.getBrewingType());
				while(burnCount<currentMaxBurnCount&&itemCount<fuel.getCount())
				{
					burnCount+=fuelBurnTime;
					itemCount++;
				}
				if(burnCount>=200)
				{
					if(burnCount>currentMaxBurnCount) burnCount=currentMaxBurnCount;
					int extraFuel=burnCount/200;
					breweryEntity.fuelBar+=extraFuel;
					if(fuelBurnTime<200) itemCount=extraFuel*Math.round(200F/fuelBurnTime);
					breweryEntity.getItem(3).shrink(itemCount);
					if(fuel.hasContainerItem()&&breweryEntity.getItem(3).isEmpty()) breweryEntity.setItem(3, fuel.getContainerItem());
					breweryEntity.setChanged();
				}
			}
			if(breweryEntity.brewFlag)
			{
				for(int i=0; i<breweryEntity.brewData.length; i++) breweryEntity.brewData[i]=!breweryEntity.getItem(i).isEmpty();
				breweryEntity.brewFlag=false;
				breweryEntity.setChanged();
			}
			if(breweryEntity.canBrew())
			{
				BrewingRecipe recipe=world.getRecipeManager().getRecipeFor(ENGRecipeTypes.getBrewingType(), breweryEntity, world).orElse(null);
				breweryEntity.totalBrewTime=recipe.getBrewTime();
				breweryEntity.brewTime++;
				if(breweryEntity.brewTime>=recipe.getBrewTime())
				{
					breweryEntity.experience+=recipe.getExperience();
					breweryEntity.brewTime=0;
					breweryEntity.brewFlag=true;
					int count=0;
					for(int i=0; i<3; i++) if(!breweryEntity.getItem(i).isEmpty()) count++;
					if(breweryEntity.fuelBar==2&&count>2) count=2;
					if(breweryEntity.fuelBar==1&&count>1) count=1;
					breweryEntity.fuelBar-=count;
					ItemStack result=recipe.assemble(breweryEntity);
					result.setCount(!breweryEntity.getItem(5).isEmpty()?count+breweryEntity.getItem(5).getCount():count);
					for(int i=0; i<3&&count>0; i++)
					{
						if(!breweryEntity.getItem(i).isEmpty())
						{
							breweryEntity.getItem(i).shrink(1);
							count--;
						}
					}
					breweryEntity.getItem(4).shrink(1);
					breweryEntity.setItem(5, result);
					breweryEntity.setChanged();
				}
				breweryEntity.setChanged();
			}
			else
			{
				if(breweryEntity.totalBrewTime!=0) breweryEntity.totalBrewTime=0;
				breweryEntity.brewTime=0;
				breweryEntity.brewFlag=true;
				breweryEntity.setChanged();
			}
			if(!world.isClientSide())
			{
				if(!(blockState.getBlock() instanceof BreweryBlock)) return;
				if(breweryEntity.fuelBar>0) blockState=blockState.setValue(BreweryBlock.HAS_FUEL, true);
				else blockState=blockState.setValue(BreweryBlock.HAS_FUEL, false);
				world.setBlock(blockPos, blockState, 2);
				blockState=world.getBlockState(blockPos);
				if(breweryEntity.brewTime>0) blockState=blockState.setValue(BreweryBlock.LIT, true);
				else blockState=blockState.setValue(BreweryBlock.LIT, false);
				world.setBlock(blockPos, blockState, 2);
			}
		}
	}

	private boolean canBrew()
	{
		if(!Arrays.equals(this.brewData, new boolean[]{!this.getItem(0).isEmpty(), !this.getItem(1).isEmpty(), !this.getItem(2).isEmpty()})) return false;
		BrewingRecipe recipe=this.level.getRecipeManager().getRecipeFor(ENGRecipeTypes.getBrewingType(), this, this.level).orElse(null);
		boolean hasRecipe=recipe!=null,
				hasFuel=this.fuelBar>0,
				isResultValid=hasRecipe&&(this.getItem(5).isEmpty()||this.getItem(5).getItem()==recipe.getResultItem().getItem()),
				isNotFull=this.getItem(0).getCount()+this.getItem(1).getCount()+this.getItem(2).getCount()+this.getItem(5).getCount()<64,
				isBrewable=hasRecipe&&hasFuel&&isResultValid&&isNotFull;
		for(int i=0; i<3; i++)
		{
			if(!this.getItem(i).isEmpty())
			{
				int secondSlot=Integer.max(1-i, 0);
				int thirdSlot=-Integer.max(i-3, -2);
				if(!this.getItem(secondSlot).isEmpty())
				{
					if(!this.getItem(thirdSlot).isEmpty()) return isBrewable&&this.getItem(i).getItem()==this.getItem(secondSlot).getItem()&&this.getItem(i).getItem()==this.getItem(thirdSlot).getItem();
					else return isBrewable&&this.getItem(i).getItem()==this.getItem(secondSlot).getItem();
				}
				else
				{
					if(!this.getItem(thirdSlot).isEmpty()) return isBrewable&&this.getItem(i).getItem()==this.getItem(thirdSlot).getItem();
					else return isBrewable;
				}
			}
		}
		return false;
	}

	public void awardExperience(Level world, Vec3 pos)
	{
		while(this.experience>0F)
		{
			int award=ExperienceOrb.getExperienceValue((int)this.experience);
			world.addFreshEntity(new ExperienceOrb(world, pos.x, pos.y, pos.z, award));
			this.experience-=award;
		}
		if(this.experience!=0F) this.experience=0F;
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
		this.brewTime=tag.getInt("BrewTime");
		this.totalBrewTime=tag.getInt("TotalBrewTime");
		this.experience=tag.getFloat("Experience");
		this.fuelBar=tag.getInt("FuelAmount");
		ContainerHelper.loadAllItems(tag, this.items);
	}

	@Override
	public CompoundTag save(CompoundTag tag)
	{
		super.save(tag);
		tag.putInt("BrewTime", this.brewTime);
		tag.putInt("TotalBrewTime", this.totalBrewTime);
		tag.putFloat("Experience", this.experience);
		tag.putInt("FuelAmount", this.fuelBar);
		ContainerHelper.saveAllItems(tag, this.items);
		return tag;
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		return this.save(new CompoundTag());
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return new ClientboundBlockEntityDataPacket(this.worldPosition, BreweryBlockEntity.TYPE_BREWERY, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet)
	{
		if(packet.getType()==BreweryBlockEntity.TYPE_BREWERY) this.handleUpdateTag(packet.getTag());
	}
	
	public int getBrewTime()
	{
		return this.brewTime;
	}

	public int getTotalBrewTime()
	{
		return this.totalBrewTime;
	}

	public float getExperience()
	{
		return this.experience;
	}

	public int getFuelBar()
	{
		return this.fuelBar;
	}

	@Override
	public int getContainerSize()
	{
		return this.items.size();
	}

	@Override
	public boolean isEmpty()
	{
		return this.items.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getItem(int slot)
	{
		return this.items.get(slot);
	}

	@Override
	public void setItem(int slot, ItemStack itemStack)
	{
		this.items.set(slot, itemStack);
	}

	@Override
	public ItemStack removeItem(int slot, int count)
	{
		return ContainerHelper.removeItem(this.items, slot, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot)
	{
		return ContainerHelper.takeItem(this.items, slot);
	}

	@Override
	public void clearContent()
	{
		this.items.clear();
	}

	@Override
	public boolean stillValid(Player player)
	{
		if(this.level.getBlockEntity(this.worldPosition)!=this) return false;
		else return !(player.distanceToSqr(this.worldPosition.getX()+0.5D, this.worldPosition.getY()+0.5D, this.worldPosition.getZ()+0.5D)>64.0D);
	}

	@Override
	public Component getDefaultName()
	{
		return new TranslatableComponent("container.engmod.brewery");
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory)
	{
		return new BreweryContainer(id, this, inventory);
	}
}