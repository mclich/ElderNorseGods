package com.github.mclich.engmod.client.gui.bar;

import java.util.Random;
import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.data.capability.ManaCapability;
import com.github.mclich.engmod.data.handler.IManaHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
public abstract class ManaBar
{
	private static final ResourceLocation ICONS=new ResourceLocation(ElderNorseGods.MOD_ID, "textures/gui/bar/mana.png");
	private static final int START_POS=39;
	
	@SubscribeEvent
	public static void renderMana(RenderGameOverlayEvent.Pre event)
	{
		Minecraft mc=Minecraft.getInstance();
		IManaHandler handler=mc.player.getCapability(ManaCapability.CAP_INSTANCE).orElse(null);
		if(!mc.gameMode.getPlayerMode().isSurvival()||event.getType()!=ElementType.ALL||handler==null||!handler.getStatus()) return;
		mc.getProfiler().push("mana");
		RenderSystem.enableBlend();
		int x=mc.getWindow().getGuiScaledWidth()/2+10;
		int y=mc.getWindow().getGuiScaledHeight()-ManaBar.START_POS;
        mc.getTextureManager().bind(ManaBar.ICONS);
        for(int i=0; i<10; i++)
        {
        	IngameGui.blit(event.getMatrixStack(), x+i*8, y, 0F, 0F, 9, 9, 64, 16);
        }
        int mana=(int)handler.getMana();
        for(int i=0; i<=mana; i+=2)
        {
        	if(i==0) continue;
        	IngameGui.blit(event.getMatrixStack(), x+81-i*4, y+1, 19F, 1F, 7, 7, 64, 16);
        }
        if(mana%2!=0) IngameGui.blit(event.getMatrixStack(), x+77-mana*4, y+1, 37F, 1F, 7, 7, 64, 16);
        RenderSystem.disableBlend();
        mc.getProfiler().pop();
	}
	
	@SubscribeEvent
	public static void renderFood(RenderGameOverlayEvent.Pre event)
	{
		Minecraft mc=Minecraft.getInstance();
		IManaHandler handler=mc.player.getCapability(ManaCapability.CAP_INSTANCE).orElse(null);
		if(event.getType()!=ElementType.FOOD||handler==null||!handler.getStatus()) return;
		event.setCanceled(true);
        mc.getProfiler().push("food");
        RenderSystem.enableBlend();
        int x=mc.getWindow().getGuiScaledWidth()/2+91;
        int y=mc.getWindow().getGuiScaledHeight()-ManaBar.START_POS-10;
        FoodStats stats=mc.player.getFoodData();
        int level=stats.getFoodLevel();
        for(int i=0; i<10; i++)
        {
            int idx=i*2+1;
            int left=x-i*8-9;
            int top=y;
            int icon=16;
            byte background=0;
            if(mc.player.hasEffect(Effects.HUNGER))
            {
                icon+=36;
                background=13;
            }
            if(((PlayerEntity)mc.getCameraEntity()).getFoodData().getSaturationLevel()<=0F&&mc.gui.getGuiTicks()%(level*3+1)==0)
            {
                top=y+(new Random().nextInt(3)-1);
            }
            IngameGui.blit(event.getMatrixStack(), left, top, mc.gui.getBlitOffset(), 16F+background*9F, 27F, 9, 9, 256, 256);
            if(idx<level) IngameGui.blit(event.getMatrixStack(), left, top, mc.gui.getBlitOffset(), icon+36F, 27F, 9, 9, 256, 256);
            else if(idx==level) IngameGui.blit(event.getMatrixStack(), left, top, mc.gui.getBlitOffset(), icon+45F, 27F, 9, 9, 256, 256);
        }
        RenderSystem.disableBlend();
        mc.getProfiler().pop();
	}
	
	@SubscribeEvent
	public static void renderAir(RenderGameOverlayEvent.Pre event)
	{
		Minecraft mc=Minecraft.getInstance();
		IManaHandler handler=mc.player.getCapability(ManaCapability.CAP_INSTANCE).orElse(null);
		if(event.getType()!=ElementType.AIR||handler==null||!handler.getStatus()) return;
		event.setCanceled(true);
        mc.getProfiler().push("air");
        RenderSystem.enableBlend();
        int x=mc.getWindow().getGuiScaledWidth()/2+91;
        int y=mc.getWindow().getGuiScaledHeight()-ManaBar.START_POS-20;
        int air=mc.player.getAirSupply();
        if(mc.player.isEyeInFluid(FluidTags.WATER)||air<300)
        {
            int full=MathHelper.ceil((double)(air-2)*10D/300D);
            int partial=MathHelper.ceil((double)air*10D/300D)-full;
            for(int i=0; i<full+partial; i++)
            {
            	IngameGui.blit(event.getMatrixStack(), x-i*8-9, y, mc.gui.getBlitOffset(), (i<full?16F:25F), 18F, 9, 9, 256, 256);
            }
        }
        RenderSystem.disableBlend();
        mc.getProfiler().pop();
	}
}