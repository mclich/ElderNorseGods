package com.github.mclich.engmod.effect;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGMobEffects;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent.MouseClickedEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.opengl.GL15;

public class AnemiaEffect extends MobEffect
{
	//dizziness, stun, vertigo
	public static final String ID="anemia";

	public AnemiaEffect()
	{
		super(MobEffectCategory.HARMFUL, 0xED7E7E);
	}

	public static MobEffectInstance getInstance()
	{
		return new MobEffectInstance(ENGMobEffects.ANEMIA.get(), 400, 0);
	}

	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
	private static abstract class EventHandler
	{
		private static void enableDepthFunc()
		{
			RenderSystem.enableDepthTest();
			RenderSystem.depthFunc(GL15.GL_GREATER);
		}

		private static void disableDepthFunc()
		{
			RenderSystem.depthFunc(GL15.GL_LEQUAL);
			RenderSystem.disableDepthTest();
		}

		private static void enableDepthFuncOrBlend(boolean flag)
		{
			if(flag) EventHandler.enableDepthFunc();
			else RenderSystem.enableBlend();
		}

		private static void disableDepthFuncOrBlend(boolean flag)
		{
			if(flag) EventHandler.disableDepthFunc();
			else RenderSystem.disableBlend();
		}

		private static void drawSlots(AbstractContainerScreen<?> screen, PoseStack poseStack, Fill fill)
		{
			boolean flag=fill==Fill.ITEM;
			Player player=screen.getMinecraft().player;
			boolean isNotSurvival=!screen.getMinecraft().gameMode.getPlayerMode().isSurvival();
			if(!player.hasEffect(ENGMobEffects.ANEMIA.get())||isNotSurvival) return;
			int i=(flag?0:1)*screen.getGuiLeft();
			int j=(flag?0:1)*screen.getGuiTop();
			for(Slot slot:screen.getMenu().slots)
			{
				if(flag) EventHandler.enableDepthFunc();
				Gui.fill(poseStack, slot.x+i, slot.y+j, slot.x+i+16, slot.y+j+16, fill.color);
				if(flag) EventHandler.disableDepthFunc();
			}
		}

		private static void drawHotbarSlots(IIngameOverlay overlay, PoseStack poseStack, Fill fill)
		{
			boolean flag=fill==Fill.ITEM;
			Minecraft mc=Minecraft.getInstance();
			boolean isNotSurvival=!mc.gameMode.getPlayerMode().isSurvival();
			if(overlay!=ForgeIngameGui.HOTBAR_ELEMENT||!mc.player.hasEffect(ENGMobEffects.ANEMIA.get())||isNotSurvival) return;
			Window window=mc.getWindow();
			int i=window.getGuiScaledWidth()/2-72;
			int j=window.getGuiScaledHeight()-3;
			int color=flag?fill.color:fill.color|0x45000000;
			if(mc.player.hasItemInSlot(EquipmentSlot.OFFHAND))
			{
				EventHandler.enableDepthFuncOrBlend(flag);
				Gui.fill(poseStack, i-29, j, i-45, j-16, color);
				EventHandler.disableDepthFuncOrBlend(flag);
			}
			for(int k=0; k<9; k++)
			{
				EventHandler.enableDepthFuncOrBlend(flag);
				Gui.fill(poseStack, i+k*20, j, i+k*20-16, j-16, color);
				EventHandler.disableDepthFuncOrBlend(flag);
			}
		}

		@SubscribeEvent
		public static void drawBgSlots(ContainerScreenEvent.DrawBackground event)
		{
			EventHandler.drawSlots(event.getContainerScreen(), event.getPoseStack(), Fill.SLOT);
		}

		@SubscribeEvent
		public static void drawFgSlots(ContainerScreenEvent.DrawForeground event)
		{
			EventHandler.drawSlots(event.getContainerScreen(), event.getPoseStack(), Fill.ITEM);
		}

		@SubscribeEvent
		public static void drawBgHotbarSlots(RenderGameOverlayEvent.PreLayer event)
		{
			EventHandler.drawHotbarSlots(event.getOverlay(), event.getPoseStack(), Fill.SLOT);
		}

		@SubscribeEvent
		public static void drawFgHotbarSlots(RenderGameOverlayEvent.PostLayer event)
		{
			EventHandler.drawHotbarSlots(event.getOverlay(), event.getPoseStack(), Fill.ITEM);
		}

		@SubscribeEvent
		public static void cancelGameClick(PlayerInteractEvent event)
		{
			Player player=event.getPlayer();
			boolean notCorrectEvent=event instanceof RightClickEmpty||event instanceof LeftClickEmpty;
			if(notCorrectEvent||!player.hasEffect(ENGMobEffects.ANEMIA.get())||player.getAbilities().instabuild) return;
			if(event instanceof RightClickBlock blockEvent)
			{
				blockEvent.setUseItem(Result.DENY);
				blockEvent.setUseBlock(Result.ALLOW);
			}
			else event.setCanceled(true);
		}

		@SubscribeEvent
		public static void cancelGuiClick(MouseClickedEvent.Pre event)
		{
			Player player=event.getScreen().getMinecraft().player;
			MultiPlayerGameMode gameMode=event.getScreen().getMinecraft().gameMode;
			boolean isSurvival=gameMode!=null&&gameMode.getPlayerMode().isSurvival();
			if(event.getScreen() instanceof AbstractContainerScreen&&player.hasEffect(ENGMobEffects.ANEMIA.get())&&isSurvival) event.setCanceled(true);
		}

		@SubscribeEvent
		public static void cancelItemPickup(EntityItemPickupEvent event)
		{
			Player player=event.getPlayer();
			if(player.hasEffect(ENGMobEffects.ANEMIA.get())&&!player.getAbilities().instabuild) event.setCanceled(true);
		}

		private enum Fill
		{
			SLOT(0x30FF0000),
			ITEM(0x30FFFFFF);

			private final int color;

			Fill(int color)
			{
				this.color=color;
			}
		}
	}
}