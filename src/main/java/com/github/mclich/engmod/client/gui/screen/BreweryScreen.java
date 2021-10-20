package com.github.mclich.engmod.client.gui.screen;

import com.github.mclich.engmod.block.container.BreweryContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BreweryScreen extends ContainerScreen<BreweryContainer>
{
	private static final ResourceLocation TEXTURE=new ResourceLocation("engmod:textures/gui/container/brewery.png");
	private static final int[] BUBBLES_CORDS=new int[]{17, 14, 11, 8, 5, 2, 0};
	
	public BreweryScreen(BreweryContainer container, PlayerInventory inventory, ITextComponent name)
	{
		super(container, inventory, name);
	}
	
	@Override
	protected void init()
	{
		super.init();
		this.imageHeight=175;
		this.inventoryLabelY=this.imageHeight-94;
		this.topPos=(this.height-this.imageHeight)/2;
		this.titleLabelX=(this.imageWidth-this.font.width(this.title))/2;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(BreweryScreen.TEXTURE);
		int x=(this.width-this.imageWidth)/2;
		int y=(this.height-this.imageHeight)/2;
		this.blit(matrixStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
		int fuelBarLength=this.menu.getFuelBar();
		if(fuelBarLength>0) this.blit(matrixStack, x+37, y+74, 0, 175, 51*fuelBarLength/32, 4);
		int brewBarHeight=this.menu.getBrewTime();
		if(brewBarHeight>0)
		{
			this.blit(matrixStack, x+123, y+23, 188, 17, 11, 27*brewBarHeight/this.menu.getTotalBrewTime());
			int bubblesCords=BreweryScreen.BUBBLES_CORDS[brewBarHeight/2%BreweryScreen.BUBBLES_CORDS.length];
			if(bubblesCords>=0) this.blit(matrixStack, x+67, y+35+bubblesCords, 176, bubblesCords, 42, 17-bubblesCords);
		}
		this.font.draw(matrixStack, Integer.toString(fuelBarLength), x+150, y+72, 0x404040);
	}
}