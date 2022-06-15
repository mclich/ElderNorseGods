package com.github.mclich.engmod.client.gui.bar;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.data.capability.IManaStorage;
import com.github.mclich.engmod.register.ENGCapabilities;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import java.lang.reflect.Method;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
public abstract class ManaBar
{
	private static final ResourceLocation ICONS=new ResourceLocation(ElderNorseGods.MOD_ID, "textures/gui/bar/mana.png");

	public static final IIngameOverlay MANA_LEVEL_ELEMENT=(gui, poseStack, ticks, width, height)->
	{
		Minecraft mc=Minecraft.getInstance();
		boolean isMounted=mc.player.getVehicle() instanceof LivingEntity;
		IManaStorage handler=mc.player.getCapability(ENGCapabilities.MANA).orElse(null);
		if(!mc.gameMode.getPlayerMode().isSurvival()||handler==null||!handler.getStatus()) return;
		mc.getProfiler().push("mana");
		RenderSystem.enableBlend();
		int x=width/2+10;
		int y=height-(isMounted?49:39);
		RenderSystem.setShaderTexture(0, ManaBar.ICONS);
		for(int i=0; i<10; i++)
		{
			Gui.blit(poseStack, x+i*8, y, 0F, 0F, 9, 9, 64, 16);
		}
		int mana=(int)handler.getMana();
		for(int i=0; i<=mana; i+=2)
		{
			if(i==0) continue;
			Gui.blit(poseStack, x+81-i*4, y+1, 19F, 1F, 7, 7, 64, 16);
		}
		if(mana%2!=0) Gui.blit(poseStack, x+77-mana*4, y+1, 37F, 1F, 7, 7, 64, 16);
		RenderSystem.disableBlend();
		mc.getProfiler().pop();
	};
	
	@SubscribeEvent
	public static void renderFood(RenderGameOverlayEvent.PreLayer event)
	{
		Minecraft mc=Minecraft.getInstance();
		boolean isHidden=mc.options.hideGui,
				isNotSurvival=!mc.gameMode.canHurtPlayer(),
				isNotPlayer=!(mc.getCameraEntity() instanceof Player),
				isMounted=mc.player.getVehicle() instanceof LivingEntity,
				isNotCorrectOverlay=event.getOverlay()!=ForgeIngameGui.FOOD_LEVEL_ELEMENT;
		IManaStorage handler=mc.player.getCapability(ENGCapabilities.MANA).orElse(null);
		if(isHidden||isNotSurvival||isNotPlayer||isMounted||isNotCorrectOverlay||handler==null||!handler.getStatus()) return;
		event.setCanceled(true);
		ForgeIngameGui gui=new ForgeIngameGui(mc);
		ObfuscationReflectionHelper.setPrivateValue(Gui.class, gui, mc.gui.getGuiTicks(), "tickCount");
		gui.renderFood(mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight()-10, event.getMatrixStack());
	}
	
	@SubscribeEvent
	public static void renderAir(RenderGameOverlayEvent.PreLayer event) throws ReflectiveOperationException
	{
		Minecraft mc=Minecraft.getInstance();
		boolean isHidden=mc.options.hideGui,
				isNotSurvival=!mc.gameMode.canHurtPlayer(),
				isNotPlayer=!(mc.getCameraEntity() instanceof Player),
				isNotCorrectOverlay=event.getOverlay()!=ForgeIngameGui.AIR_LEVEL_ELEMENT;
		IManaStorage handler=mc.player.getCapability(ENGCapabilities.MANA).orElse(null);
		if(isHidden||isNotSurvival||isNotPlayer||isNotCorrectOverlay||handler==null||!handler.getStatus()) return;
		event.setCanceled(true);
		Method renderAir=ObfuscationReflectionHelper.findMethod(ForgeIngameGui.class, "renderAir", int.class, int.class, PoseStack.class);
		renderAir.invoke(new ForgeIngameGui(mc), mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight()-20, event.getMatrixStack());
	}
}