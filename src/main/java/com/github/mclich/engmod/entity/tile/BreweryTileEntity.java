package com.github.mclich.engmod.entity.tile;

import java.util.Arrays;
import com.github.mclich.engmod.block.BreweryBlock;
import com.github.mclich.engmod.block.container.BreweryContainer;
import com.github.mclich.engmod.recipe.BrewingRecipe;
import com.github.mclich.engmod.register.ENGRecipeTypes;
import com.github.mclich.engmod.register.ENGTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class BreweryTileEntity extends LockableTileEntity implements ITickableTileEntity
{
	public static final int SIZE=6;
	
	private ItemStackHandler items=new ItemStackHandler(BreweryTileEntity.SIZE);
	private LazyOptional<IItemHandler> itemsHandler=LazyOptional.of(()->this.items);
	private boolean[] brewData;
	private boolean brewFlag;
	private float experience;
	private int totalBrewTime;
	private int brewTime;
	private int fuelBar;
	
	public BreweryTileEntity()
	{
		super(ENGTileEntities.BREWERY_TILE.get());
		this.brewData=new boolean[3];
		this.brewFlag=true;
		this.experience=0F;
		this.totalBrewTime=0;
		this.brewTime=0;
		this.fuelBar=0;
	}
	
	@Override
	public void load(BlockState blockState, CompoundNBT tag)
	{
		this.items.deserializeNBT(tag.getCompound("ItemHandler"));
		this.experience=tag.getFloat("Experience");
		this.totalBrewTime=tag.getInt("TotalBrewTime");
		this.brewTime=tag.getInt("BrewTime");
		this.fuelBar=tag.getInt("FuelBar");
		super.load(blockState, tag);
	}

	@Override
	public CompoundNBT save(CompoundNBT tag)
	{
		tag.put("ItemHandler", this.items.serializeNBT());
		tag.putFloat("Experience", this.experience);
		tag.putInt("TotalBrewTime", this.totalBrewTime);
		tag.putInt("BrewTime", this.brewTime);
		tag.putInt("FuelBar", this.fuelBar);
		return super.save(tag);
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.level.isClientSide()?super.getUpdateTag():this.save(new CompoundNBT());
	}
	
	private boolean canBrew()
	{
		if(!Arrays.equals(this.brewData, new boolean[]{!this.getItem(0).isEmpty(), !this.getItem(1).isEmpty(), !this.getItem(2).isEmpty()})) return false;
		BrewingRecipe recipe=this.level.getRecipeManager().getRecipeFor(ENGRecipeTypes.getBrewingType(), this, this.level).orElse(null);
		boolean hasRecipe=recipe!=null;
		boolean hasFuel=this.fuelBar>0;
		boolean isResultValid=hasRecipe?this.getItem(5).isEmpty()||this.getItem(5).getItem()==recipe.getResultItem().getItem():false;
		boolean isBrewable=hasRecipe&&hasFuel&&isResultValid;
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
	
	public void awardExperience(World world, Vector3d pos)
	{
		while(this.experience>0F)
		{
			int award=ExperienceOrbEntity.getExperienceValue((int)this.experience);
			world.addFreshEntity(new ExperienceOrbEntity(world, pos.x, pos.y, pos.z, award));
			this.experience-=award;
		}
		if(this.experience!=0F) this.experience=0F;
	}

	public float getExperience()
	{
		return this.experience;
	}

	public int getTotalBrewTime()
	{
		return this.totalBrewTime;
	}
	
	public int getBrewTime()
	{
		return this.brewTime;
	}

	public int getFuelBar()
	{
		return this.fuelBar;
	}

	@Override
	public int getContainerSize()
	{
		return BreweryTileEntity.SIZE;
	}

	@Override
	public boolean isEmpty()
	{
		for(int i=0; i<this.items.getSlots(); i++)
		{
			if(!this.items.getStackInSlot(i).isEmpty()) return false;
		}
		return true;
	}

	@Override
	public ItemStack getItem(int slot)
	{
		return this.items.getStackInSlot(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int count)
	{
		return this.items.extractItem(slot, count, false);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot)
	{
		return this.items.extractItem(slot, 64, true);
	}

	@Override
	public void setItem(int slot, ItemStack itemStack)
	{
		this.items.setStackInSlot(slot, itemStack);
	}

	@Override
	public boolean stillValid(PlayerEntity player)
	{
		if(this.level.getBlockEntity(this.worldPosition)!=this) return false;
		else return !(player.distanceToSqr(this.worldPosition.getX()+0.5D, this.worldPosition.getY()+0.5D, this.worldPosition.getZ()+0.5D)>64.0D);
	}

	@Override
	public void clearContent()
	{
		for(int i=0; i<this.items.getSlots(); i++)
		{
			this.items.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	@Override
	protected ITextComponent getDefaultName()
	{
		return new TranslationTextComponent("container.engmod.brewery");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory playerInventory)
	{
		return this.createMenu(id, playerInventory, playerInventory.player);
	}
	
	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player)
	{
		return new BreweryContainer(id, this, playerInventory, player);
	}

	@Override
	public void tick()
	{
		ItemStack fuel=this.getItem(3).copy();
		if(this.fuelBar<64)
		{
			int itemCount=0;
			int burnCount=0;
			while(burnCount<12800&&itemCount<fuel.getCount())
			{
				burnCount+=ForgeHooks.getBurnTime(fuel, ENGRecipeTypes.getBrewingType());
				itemCount++;
			}
			if(burnCount>=200)
			{
				if(burnCount>12800) burnCount=12800;
				this.fuelBar+=burnCount/200;
				this.getItem(3).shrink(itemCount);
				if(fuel.hasContainerItem()&&this.getItem(3).isEmpty()) this.setItem(3, fuel.getContainerItem());
				this.setChanged();
			}
		}
		if(this.brewFlag)
		{
			for(int i=0; i<this.brewData.length; i++) this.brewData[i]=!this.getItem(i).isEmpty();
			this.brewFlag=false;
			this.setChanged();
		}
		if(this.canBrew())
		{
			BrewingRecipe recipe=this.level.getRecipeManager().getRecipeFor(ENGRecipeTypes.getBrewingType(), this, this.level).orElse(null);
			this.totalBrewTime=recipe.getBrewTime();
			this.brewTime++;
			if(this.brewTime>=recipe.getBrewTime())
			{
				this.experience+=recipe.getExperience();
				this.brewTime=0;
				this.brewFlag=true;
				int count=0;
				for(int i=0; i<3; i++) if(!this.getItem(i).isEmpty()) count++;
				if(this.fuelBar==2&&count>2) count=2;
				if(this.fuelBar==1&&count>1) count=1;
				this.fuelBar-=count;
				ItemStack result=recipe.assemble(this);
				result.setCount(!this.getItem(5).isEmpty()?count+this.getItem(5).getCount():count);
				for(int i=0; i<3&&count>0; i++)
				{
					if(!this.getItem(i).isEmpty())
					{
						this.getItem(i).shrink(1);
						count--;
					}
				}
				this.getItem(4).shrink(1);
				this.setItem(5, result);
				this.setChanged();
			}
			this.setChanged();
		}
		else
		{
			if(this.totalBrewTime!=0) this.totalBrewTime=0;
			this.brewTime=0;
			this.brewFlag=true;
			this.setChanged();
		}
		if(!this.level.isClientSide())
		{
			BlockState blockState=this.level.getBlockState(this.getBlockPos());
			if(!(blockState.getBlock() instanceof BreweryBlock)) return;
			if(this.fuelBar>0) blockState=blockState.setValue(BreweryBlock.HAS_FUEL, true);
			else blockState=blockState.setValue(BreweryBlock.HAS_FUEL, false);
			this.level.setBlock(this.worldPosition, blockState, 2);
			blockState=this.level.getBlockState(this.getBlockPos());
			if(this.brewTime>0) blockState=blockState.setValue(BreweryBlock.LIT, true);
			else blockState=blockState.setValue(BreweryBlock.LIT, false);
			this.level.setBlock(this.worldPosition, blockState, 2);
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction facing)
	{
		if(cap==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return this.itemsHandler.cast();
		return super.getCapability(cap, facing);
	}
}