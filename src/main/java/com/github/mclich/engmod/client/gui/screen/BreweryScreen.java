package com.github.mclich.engmod.client.gui.screen;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.block.BreweryBlock;
import com.github.mclich.engmod.block.container.BreweryContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BreweryScreen extends AbstractContainerScreen<BreweryContainer>
{
	private static final ResourceLocation TEXTURE=new ResourceLocation(ElderNorseGods.MOD_ID, "textures/gui/container/"+BreweryBlock.ID+".png");
	private static final int[] BUBBLES_CORDS=new int[]{17, 14, 11, 8, 5, 2, 0};
	
	public BreweryScreen(BreweryContainer container, Inventory inventory, Component component)
	{
		super(container, inventory, component);
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
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(poseStack, mouseX, mouseY);
	}

	@Override
	public void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		RenderSystem.setShaderTexture(0, BreweryScreen.TEXTURE);
		int x=(this.width-this.imageWidth)/2;
		int y=(this.height-this.imageHeight)/2;
		this.blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
		int fuelBarLength=this.menu.getFuelBar();
		if(fuelBarLength>0) this.blit(poseStack, x+37, y+74, 0, 175, 51*fuelBarLength/32, 4);
		int brewBarHeight=this.menu.getBrewTime();
		if(brewBarHeight>0)
		{
			this.blit(poseStack, x+123, y+23, 188, 17, 11, 27*brewBarHeight/this.menu.getTotalBrewTime());
			int bubblesCords=BreweryScreen.BUBBLES_CORDS[brewBarHeight/2%BreweryScreen.BUBBLES_CORDS.length];
			if(bubblesCords>=0) this.blit(poseStack, x+67, y+35+bubblesCords, 176, bubblesCords, 42, 17-bubblesCords);
		}
		this.font.draw(poseStack, Integer.toString(fuelBarLength), x+150, y+72, 0x404040);
	}
}