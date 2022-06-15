package com.github.mclich.engmod.block.container;

import com.github.mclich.engmod.entity.block.BreweryBlockEntity;
import com.github.mclich.engmod.recipe.BrewingRecipe;
import com.github.mclich.engmod.register.ENGContainers;
import com.github.mclich.engmod.register.ENGRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import java.util.List;
import java.util.stream.Stream;

public class BreweryContainer extends AbstractContainerMenu
{
	private BreweryBlockEntity breweryEntity;
	private List<BrewingRecipe> recipes;
	
    public BreweryContainer(int window, Inventory inventory, FriendlyByteBuf buffer)
    {
    	this(window, inventory.player.getCommandSenderWorld().getBlockEntity(buffer.readBlockPos()), inventory);
    }

    public BreweryContainer(int window, BlockEntity blockEntity, Inventory inventory)
	{
		super(ENGContainers.BREWERY_CONTAINER.get(), window);
		if(blockEntity instanceof BreweryBlockEntity) this.breweryEntity=(BreweryBlockEntity)blockEntity;
		this.recipes=this.breweryEntity.getLevel().getRecipeManager().getAllRecipesFor(ENGRecipeTypes.getBrewingType());
        if(this.breweryEntity!=null)
        {
        	this.breweryEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent
        	(
        		itemHandler->
        		{
        			this.addSlot(new BottleSlot(this.breweryEntity, 0, 57, 17));
                	this.addSlot(new BottleSlot(this.breweryEntity, 1, 80, 17));
                	this.addSlot(new BottleSlot(this.breweryEntity, 2, 103, 17));
                	this.addSlot(new FuelSlot(this.breweryEntity, 3, 80, 53));
                	this.addSlot(new IngredientSlot(this.breweryEntity, 4, 38, 53));
                	this.addSlot(new ResultSlot(this.breweryEntity, 5, 122, 53));
        		}
        	);
        }
        for(int i=0; i<3; i++)
        {
        	for(int j=0; j<9; j++)
        	{
        		this.addSlot(new Slot(inventory, j+i*9+9, 8+j*18, 93+i*18));
        	}
        }
        for(int i=0; i<9; i++)
        {
        	this.addSlot(new Slot(inventory, i, 8+i*18, 151));
        }
	}

    private static boolean onResultTake(Player player, Slot slot, ItemStack itemStack)
    {
    	if(slot instanceof ResultSlot) slot.onTake(player, itemStack);
    	return true;
    }

	protected boolean isValidBottle(ItemStack itemStack)
	{
		return this.recipes.stream().anyMatch(r->Stream.of(r.getBottleIngredient().getItems()).anyMatch(i->i.getItem()==itemStack.getItem()&&i.getOrCreateTag().equals(itemStack.getOrCreateTag())));
	}

	protected boolean isFuel(ItemStack itemStack)
	{
		return ForgeHooks.getBurnTime(itemStack, ENGRecipeTypes.getBrewingType())>0;
	}

	protected boolean isIngredient(ItemStack itemStack)
	{
		return this.recipes.stream().anyMatch(r->Stream.of(r.getCookIngredient().getItems()).anyMatch(i->i.getItem()==itemStack.getItem()));
	}

	protected boolean isResultItem(ItemStack itemStack)
	{
		return this.recipes.stream().anyMatch(r->r.getResultItem().getItem()==itemStack.getItem());
	}

	@SuppressWarnings("unused")
	public float getExperience()
	{
		return this.breweryEntity.getExperience();
	}
	
	public int getTotalBrewTime()
	{
		return this.breweryEntity.getTotalBrewTime();
	}
	
	public int getBrewTime()
	{
		return this.breweryEntity.getBrewTime();
	}
	
	public int getFuelBar()
	{
		return this.breweryEntity.getFuelBar();
	}
	
	@Override
	public boolean stillValid(Player player)
	{
		return this.breweryEntity!=null&&this.breweryEntity.stillValid(player);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex)
	{
		ItemStack itemStack;
		Slot slot=this.slots.get(slotIndex);
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

	private class BottleSlot extends Slot
	{
		public BottleSlot(Container container, int index, int xPixel, int yPixel)
		{
			super(container, index, xPixel, yPixel);
		}

		@Override
		public int getMaxStackSize()
		{
			return 1;
		}

		@Override
		public int getMaxStackSize(ItemStack itemStack)
		{
			return this.getMaxStackSize();
		}

		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return BreweryContainer.this.isValidBottle(itemStack);
		}
	}

	private class FuelSlot extends Slot
	{
		public FuelSlot(Container container, int index, int xPixel, int yPixel)
		{
			super(container, index, xPixel, yPixel);
		}

		@Override
		public int getMaxStackSize()
		{
			return 64;
		}

		@Override
		public int getMaxStackSize(ItemStack itemStack)
		{
			return this.getMaxStackSize();
		}

		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return BreweryContainer.this.isFuel(itemStack);
		}
	}

	private class IngredientSlot extends Slot
	{
		public IngredientSlot(Container container, int index, int xPixel, int yPixel)
		{
			super(container, index, xPixel, yPixel);
		}
		
		@Override
		public int getMaxStackSize()
		{
			return 64;
		}

		@Override
		public int getMaxStackSize(ItemStack itemStack)
		{
			return this.getMaxStackSize();
		}

		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return BreweryContainer.this.isIngredient(itemStack);
		}
	}
	
	private class ResultSlot extends Slot
	{
		public ResultSlot(Container container, int index, int xPixel, int yPixel)
		{
			super(container, index, xPixel, yPixel);
		}
		
		@Override
		public int getMaxStackSize()
		{
			return 64;
		}

		@Override
		public int getMaxStackSize(ItemStack itemStack)
		{
			return this.getMaxStackSize();
		}
		
		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return false;
		}
		
		@Override
		public void onTake(Player player, ItemStack itemStack)
		{
			BreweryContainer.this.breweryEntity.awardExperience(player.level, player.position());
			super.onTake(player, itemStack);
		}
	}
}