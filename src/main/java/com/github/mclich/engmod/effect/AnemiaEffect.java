package com.github.mclich.engmod.effect;

import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Options;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class AnemiaEffect extends MobEffect
{
	public static final String ID="anemia";
	
	private final Key keyDrop;
	private final Key keySwapOffhand;
	
	public AnemiaEffect()
	{
		super(MobEffectCategory.HARMFUL, 0xED7E7E);
		Options options=Minecraft.getInstance().options;
		this.keyDrop=options.keyDrop.getKey();
		this.keySwapOffhand=options.keySwapOffhand.getKey();
		//dizziness, stun, vertigo
	}
	
	public static MobEffectInstance getInstance()
	{
		return new MobEffectInstance(ENGEffects.ANEMIA.get(), 400, 0);
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return true;
	}
	
	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		Player player=Minecraft.getInstance().player;
		if(entity.getCommandSenderWorld().isClientSide()&&entity==player)
		{
			Options options=Minecraft.getInstance().options;
			if(entity.getEffect(this).getDuration()==400||!player.getAbilities().instabuild)
			{
				options.keyDrop.setKey(InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_UNKNOWN));
				options.keySwapOffhand.setKey(InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_UNKNOWN));
			}
			if(entity.getEffect(this).getDuration()==1||player.getAbilities().instabuild)
			{
				options.keyDrop.setKey(this.keyDrop);
				options.keySwapOffhand.setKey(this.keySwapOffhand);
			}
		}
	}
	
	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
	private static abstract class EventHandler
	{
		private static void enableDepthFunc()
		{
			RenderSystem.enableDepthTest();
			RenderSystem.depthFunc(GL11.GL_GREATER);
		}
		
		private static void disableDepthFunc()
		{
			RenderSystem.depthFunc(GL11.GL_LEQUAL);
			RenderSystem.disableDepthTest();
		}
		
		private static void drawSlots(AbstractContainerScreen<?> screen, PoseStack matrixStack, Fill fill)
		{
			boolean flag=fill==Fill.ITEM;
			Player player=screen.getMinecraft().player;
			if(!player.hasEffect(ENGEffects.ANEMIA.get())||player.getAbilities().instabuild) return;
			int i=(flag?0:1)*screen.getGuiLeft();
			int j=(flag?0:1)*screen.getGuiTop();
			for(Slot slot:screen.getMenu().slots)
			{
				if(flag) EventHandler.enableDepthFunc();
				Gui.fill(matrixStack, slot.x+i, slot.y+j, slot.x+i+16, slot.y+j+16, fill.color);
				if(flag) EventHandler.disableDepthFunc();
			}
		}
		
		private static void drawHotbarSlots(IIngameOverlay overlay, PoseStack matrixStack, Fill fill)
		{
			boolean flag=fill==Fill.ITEM;
			Minecraft mc=Minecraft.getInstance();
			if(overlay!=ForgeIngameGui.HOTBAR_ELEMENT||!mc.player.hasEffect(ENGEffects.ANEMIA.get())||mc.player.getAbilities().instabuild) return;
			Window window=mc.getWindow();
			int i=window.getGuiScaledWidth()/2-72;
			int j=window.getGuiScaledHeight()-3;
			if(mc.player.hasItemInSlot(EquipmentSlot.OFFHAND))
			{
				if(flag) EventHandler.enableDepthFunc();
				Gui.fill(matrixStack, i-29, j, i-45, j-16, fill.color);
				if(flag) EventHandler.disableDepthFunc();
			}
			for(int k=0; k<9; k++)
			{
				if(flag) EventHandler.enableDepthFunc();
				else
				{
					RenderSystem.enableBlend();
					RenderSystem.blendFunc(SourceFactor.CONSTANT_COLOR, DestFactor.ONE_MINUS_CONSTANT_COLOR);
				}
				Gui.fill(matrixStack, i+k*20, j, i+k*20-16, j-16, fill.color);
				if(flag) EventHandler.disableDepthFunc();
				else RenderSystem.disableBlend();
			}
		}
		
		@SubscribeEvent
		public static void drawBgSlots(GuiContainerEvent.DrawBackground event)
		{
			EventHandler.drawSlots(event.getGuiContainer(), event.getMatrixStack(), Fill.SLOT);
		}
		
		@SubscribeEvent
		public static void drawFgSlots(GuiContainerEvent.DrawForeground event)
		{
			EventHandler.drawSlots(event.getGuiContainer(), event.getMatrixStack(), Fill.ITEM);
		}
		
		@SubscribeEvent
		public static void drawBgHotbarSlots(RenderGameOverlayEvent.PreLayer event)
		{
			EventHandler.drawHotbarSlots(event.getOverlay(), event.getMatrixStack(), Fill.SLOT);
		}
		
		@SubscribeEvent
		public static void drawFgHotbarSlots(RenderGameOverlayEvent.PostLayer event)
		{
			EventHandler.drawHotbarSlots(event.getOverlay(), event.getMatrixStack(), Fill.ITEM);
		}
		
		@SubscribeEvent
		public static void cancelGameClick(PlayerInteractEvent event)
		{
			Player player=event.getPlayer();
			boolean correctEvent=!(event instanceof RightClickEmpty)&&!(event instanceof LeftClickEmpty);
			if(player.hasEffect(ENGEffects.ANEMIA.get())&&correctEvent&&!player.getAbilities().instabuild)
			{
				if(event instanceof RightClickBlock)
				{
					((RightClickBlock)event).setUseItem(Result.DENY);
					((RightClickBlock)event).setUseBlock(Result.ALLOW);
				}
				else event.setCanceled(true);
			}
		}
		
		@SubscribeEvent
		public static void cancelGuiClick(MouseClickedEvent.Pre event)
		{
			Player player=event.getGui().getMinecraft().player;
			if(event.getGui() instanceof AbstractContainerScreen&&player.hasEffect(ENGEffects.ANEMIA.get())&&!player.getAbilities().instabuild) event.setCanceled(true);
		}
		
		@SubscribeEvent
		public static void cancelItemPickup(EntityItemPickupEvent event)
		{
			Player player=event.getPlayer();
			if(player.hasEffect(ENGEffects.ANEMIA.get())&&!player.getAbilities().instabuild) event.setCanceled(true);
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