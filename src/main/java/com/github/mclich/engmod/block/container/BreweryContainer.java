package com.github.mclich.engmod.block.container;

import java.util.List;
import java.util.stream.Stream;
import com.github.mclich.engmod.entity.tile.BreweryTileEntity;
import com.github.mclich.engmod.recipe.BrewingRecipe;
import com.github.mclich.engmod.register.ENGContainers;
import com.github.mclich.engmod.register.ENGRecipeTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BreweryContainer extends Container
{
	private BreweryTileEntity tileEntity;
	private List<BrewingRecipe> recipes;
	
    public BreweryContainer(int window, PlayerInventory pInventory, PacketBuffer data)
    {
    	this(window, pInventory.player.getCommandSenderWorld().getBlockEntity(data.readBlockPos()), pInventory, pInventory.player);
    }
    
    public BreweryContainer(int window, TileEntity tileEntity, PlayerInventory pInventory, PlayerEntity player)
	{
		super(ENGContainers.BREWERY_CONTAINER.get(), window);
		if(tileEntity!=null&&tileEntity instanceof BreweryTileEntity) this.tileEntity=(BreweryTileEntity)tileEntity;
		this.recipes=this.tileEntity.getLevel().getRecipeManager().getAllRecipesFor(ENGRecipeTypes.getBrewingType());
        if(this.tileEntity!=null)
        {
        	this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent
        	(
        		itemHandler->
        		{
        			this.addSlot(new BottleSlot(itemHandler, 0, 57, 17));
                	this.addSlot(new BottleSlot(itemHandler, 1, 80, 17));
                	this.addSlot(new BottleSlot(itemHandler, 2, 103, 17));
                	this.addSlot(new FuelSlot(itemHandler, 3, 80, 53));
                	this.addSlot(new IngredientSlot(itemHandler, 4, 38, 53));
                	this.addSlot(new ResultSlot(itemHandler, 5, 122, 53));
        		}
        	);
        }
        for(int i=0; i<3; ++i)
        {
        	for(int j=0; j<9; ++j)
        	{
        		this.addSlot(new Slot(pInventory, j+i*9+9, 8+j*18, 93+i*18));
        	}
        }
        for(int k=0; k<9; ++k)
        {
        	this.addSlot(new Slot(pInventory, k, 8+k*18, 151));
        }
	}

    private static boolean onResultTake(PlayerEntity player, Slot slot, ItemStack itemStack)
    {
    	if(slot instanceof ResultSlot) slot.onTake(player, itemStack);
    	return true;
    }
    
	public boolean isValidBottle(ItemStack itemStack)
	{
		return this.recipes.stream().anyMatch(r->Stream.of(r.getIngredients().get(0).getItems()).anyMatch(i->i.getItem()==itemStack.getItem()&&i.getOrCreateTag().equals(itemStack.getOrCreateTag())));
	}
	
	public boolean isFuel(ItemStack itemStack)
	{
		return ForgeHooks.getBurnTime(itemStack, ENGRecipeTypes.getBrewingType())>0;
	}
	
	public boolean isIngredient(ItemStack itemStack)
	{
		return this.recipes.stream().anyMatch(r->Stream.of(r.getIngredients().get(1).getItems()).anyMatch(i->i.getItem()==itemStack.getItem()));
	}
	
	public boolean isResultItem(ItemStack itemStack)
	{
		return this.recipes.stream().anyMatch(r->r.getResultItem().getItem()==itemStack.getItem());
	}
	
	public float getExperience()
	{
		return this.tileEntity.getExperience();
	}
	
	public int getTotalBrewTime()
	{
		return this.tileEntity.getTotalBrewTime();
	}
	
	public int getBrewTime()
	{
		return this.tileEntity.getBrewTime();
	}
	
	public int getFuelBar()
	{
		return this.tileEntity.getFuelBar();
	}
	
	@Override
	public boolean stillValid(PlayerEntity player)
	{
		return this.tileEntity!=null?this.tileEntity.stillValid(player):false;
	}
	
	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int slotIndex)
	{
		Slot slot=this.slots.get(slotIndex);
		ItemStack itemStack=ItemStack.EMPTY;
		if(slot!=null&&slot.hasItem())
		{
			itemStack=slot.getItem();
			if(this.isValidBottle(itemStack))
			{
				if((slotIndex==0||slotIndex==1||slotIndex==2)&&!this.moveItemStackTo(itemStack, 6, 42, false)) return ItemStack.EMPTY;
				else if(slotIndex==5&&!this.moveItemStackTo(itemStack, 6, 42, true)) return ItemStack.EMPTY;
				else if(BreweryContainer.onResultTake(player, slot, itemStack)&&!this.moveItemStackTo(itemStack, 0, 3, false)) return ItemStack.EMPTY;
			}
			else if(this.isFuel(itemStack))
			{
				if(slotIndex==3&&!this.moveItemStackTo(itemStack, 6, 42, false)) return ItemStack.EMPTY;
				else if(slotIndex==5&&!this.moveItemStackTo(itemStack, 6, 42, true)) return ItemStack.EMPTY;
				else if(BreweryContainer.onResultTake(player, slot, itemStack)&&!this.moveItemStackTo(itemStack, 3, 4, false)) return ItemStack.EMPTY;
			}
			else if(this.isIngredient(itemStack))
			{
				if(slotIndex==4&&!this.moveItemStackTo(itemStack, 6, 42, false)) return ItemStack.EMPTY;
				else if(slotIndex==5&&!this.moveItemStackTo(itemStack, 6, 42, true)) return ItemStack.EMPTY;
				else if(BreweryContainer.onResultTake(player, slot, itemStack)&&!this.moveItemStackTo(itemStack, 4, 5, false)) return ItemStack.EMPTY;
			}
			else if(this.isResultItem(itemStack)&&slotIndex==5&&!this.moveItemStackTo(itemStack, 6, 42, true)) return ItemStack.EMPTY;
			else if(BreweryContainer.onResultTake(player, slot, itemStack)&&slotIndex>5&&slotIndex<33&&!this.moveItemStackTo(itemStack, 33, 42, false)) return ItemStack.EMPTY;
			else if(!this.moveItemStackTo(itemStack, 6, 33, false)) return ItemStack.EMPTY;
			if(itemStack.isEmpty()) slot.set(ItemStack.EMPTY);
			slot.setChanged();
		}
		return ItemStack.EMPTY;
	}

	public class BottleSlot extends SlotItemHandler
	{
		public BottleSlot(IItemHandler itemHandler, int index, int xPixel, int yPixel)
		{
			super(itemHandler, index, xPixel, yPixel);
		}
		
		@Override
		public int getMaxStackSize()
		{
			return 1;
		}

		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return BreweryContainer.this.isValidBottle(itemStack);
		}
	}
	
	public class FuelSlot extends SlotItemHandler
	{
		public FuelSlot(IItemHandler itemHandler, int index, int xPixel, int yPixel)
		{
			super(itemHandler, index, xPixel, yPixel);
		}
		
		@Override
		public int getMaxStackSize(ItemStack itemStack)
		{
			return 64;
		}

		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return BreweryContainer.this.isFuel(itemStack);
		}
	}
	
	public class IngredientSlot extends SlotItemHandler
	{
		public IngredientSlot(IItemHandler itemHandler, int index, int xPixel, int yPixel)
		{
			super(itemHandler, index, xPixel, yPixel);
		}
		
		@Override
		public int getMaxStackSize()
		{
			return 64;
		}

		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return BreweryContainer.this.isIngredient(itemStack);
		}
	}
	
	public class ResultSlot extends SlotItemHandler
	{
		public ResultSlot(IItemHandler itemHandler, int index, int xPixel, int yPixel)
		{
			super(itemHandler, index, xPixel, yPixel);
		}
		
		@Override
		public int getMaxStackSize()
		{
			return 64;
		}
		
		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return false;
		}
		
		@Override
		public ItemStack onTake(PlayerEntity player, ItemStack itemStack)
		{
			BreweryContainer.this.tileEntity.awardExperience(player.level, player.position());
			return super.onTake(player, itemStack);
		}
	}
}