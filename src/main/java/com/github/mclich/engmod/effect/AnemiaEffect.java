package com.github.mclich.engmod.effect;

import org.lwjgl.opengl.GL11;
import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGEffects;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IngameGui;
//import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class AnemiaEffect extends Effect
{
	public static final String ID="anemia";
	
	public AnemiaEffect()
	{
		super(EffectType.HARMFUL, 0xDAE054);
		//dizziness, stun, vertigo
		//EntityItemPickupEvent
		//ItemTossEvent
	}
	
	public static EffectInstance getInstance()
	{
		return new EffectInstance(ENGEffects.ANEMIA.get(), 400, 0);
	}
	
	@Override
	public void renderHUDEffect(EffectInstance effect, AbstractGui gui, MatrixStack matrixStack, int x, int y, float z, float alpha)
	{
		/*
		Minecraft mc=Minecraft.getInstance();
		MainWindow window=mc.getWindow();
		int a=window.getGuiScaledWidth()/2-72;
		int b=window.getGuiScaledHeight()-3;
		if(mc.player.hasItemInSlot(EquipmentSlotType.OFFHAND))
		{
			IngameGui.fill(matrixStack, a-29, b, a-45, b-16, EventHandler.SLOT_FILL);
			EventHandler.enableDepthFunc();
			IngameGui.fill(matrixStack, a-29, b, a-45, b-16, EventHandler.ITEM_FILL);
			EventHandler.disableDepthFunc();
		}
		for(int i=0; i<9; i++)
		{
			IngameGui.fill(matrixStack, a+i*20, b, a+i*20-16, b-16, EventHandler.SLOT_FILL);
			EventHandler.enableDepthFunc();
			IngameGui.fill(matrixStack, a+i*20, b, a+i*20-16, b-16, EventHandler.ITEM_FILL);
			EventHandler.disableDepthFunc();
		}
		*/
	}
	
	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
	private static abstract class EventHandler
	{
		private static final int SLOT_FILL=0x30FF0000;
		private static final int ITEM_FILL=0x30FFFFFF;
		
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
		
		@SuppressWarnings("resource")
		private static void drawSlots(ContainerScreen<?> screen, MatrixStack matrixStack, boolean flag)
		{
			if(!screen.getMinecraft().player.hasEffect(ENGEffects.ANEMIA.get())) return;
			int i=(flag?0:1)*screen.getGuiLeft();
			int j=(flag?0:1)*screen.getGuiTop();
			for(Slot slot:screen.getMenu().slots)
			{
				if(flag) EventHandler.enableDepthFunc();
				IngameGui.fill(matrixStack, slot.x+i, slot.y+j, slot.x+i+16, slot.y+j+16, flag?EventHandler.ITEM_FILL:EventHandler.SLOT_FILL);
				if(flag) EventHandler.disableDepthFunc();
			}
		}
		
		private static void drawHotbarSlots(ElementType type, MatrixStack matrixStack, boolean flag)
		{
			Minecraft mc=Minecraft.getInstance();
			if(type!=ElementType.HOTBAR||!mc.player.hasEffect(ENGEffects.ANEMIA.get())) return;
			MainWindow window=mc.getWindow();
			int i=window.getGuiScaledWidth()/2-72;
			int j=window.getGuiScaledHeight()-3;
			if(mc.player.hasItemInSlot(EquipmentSlotType.OFFHAND))
			{
				if(flag) EventHandler.enableDepthFunc();
				IngameGui.fill(matrixStack, i-29, j, i-45, j-16, flag?EventHandler.ITEM_FILL:EventHandler.SLOT_FILL);
				if(flag) EventHandler.disableDepthFunc();
			}
			for(int k=0; k<9; k++)
			{
				/*
				GL_GEQUAL - no transparency
				GL_ALWAYS - no transparency
				GL_NOTEQUAL - no transparency
				GL_GREATER - no transparency
				GL_LEQUAL - no transparency
				GL_EQUAL - no transparency
				GL_LESS - no transparency
				GL_NEVER - no transparency
				 */
				if(flag) EventHandler.enableDepthFunc();
				else
				{
					RenderSystem.enableBlend();
					RenderSystem.blendFunc(SourceFactor.CONSTANT_COLOR, DestFactor.ONE_MINUS_CONSTANT_COLOR);
				}
				IngameGui.fill(matrixStack, i+k*20, j, i+k*20-16, j-16, flag?EventHandler.ITEM_FILL:EventHandler.SLOT_FILL);
				if(flag) EventHandler.disableDepthFunc();
				else
				{
					//RenderSystem.defaultBlendFunc();
					RenderSystem.disableBlend();
				}
			}
		}
		
		@SubscribeEvent
		public static void drawBgSlots(GuiContainerEvent.DrawBackground event)
		{
			EventHandler.drawSlots(event.getGuiContainer(), event.getMatrixStack(), false);
		}
		
		@SubscribeEvent
		public static void drawFgSlots(GuiContainerEvent.DrawForeground event)
		{
			EventHandler.drawSlots(event.getGuiContainer(), event.getMatrixStack(), true);
		}
		
		@SubscribeEvent
		public static void drawBgHotbarSlots(RenderGameOverlayEvent.Pre event)
		{
			EventHandler.drawHotbarSlots(event.getType(), event.getMatrixStack(), false);
		}
		
		@SubscribeEvent
		public static void drawFgHotbarSlots(RenderGameOverlayEvent.Post event)
		{
			EventHandler.drawHotbarSlots(event.getType(), event.getMatrixStack(), true);
		}
		
		/*
		@SubscribeEvent
		public static void cancelKeyPress(KeyboardKeyEvent event)
		{
			ElderNorseGods.LOGGER.info("pressed");
			Minecraft mc=event.getGui().getMinecraft();
			if(mc.player==null) return;
			ElderNorseGods.LOGGER.info("passed 1");
			if(!mc.player.hasEffect(ENGEffects.ANEMIA.get())) return;
			ElderNorseGods.LOGGER.info("passed 2");
			if(mc.options.keyDrop.getKey().getValue()==event.getKeyCode()||mc.options.keySwapOffhand.getKey().getValue()==event.getKeyCode())
			{
				event.setCanceled(true);
				ElderNorseGods.LOGGER.info("canceled");
			}
		}
		*/
		
		@SubscribeEvent
		public static void cancelGameClick(PlayerInteractEvent event)
		{
			boolean correctEvent=!(event instanceof RightClickEmpty)&&!(event instanceof LeftClickEmpty);
			if(event.getPlayer().hasEffect(ENGEffects.ANEMIA.get())&&correctEvent)
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
			if(event.getGui() instanceof ContainerScreen&&event.getGui().getMinecraft().player.hasEffect(ENGEffects.ANEMIA.get()))
			{
				event.setCanceled(true);
			}
		}
	}
}