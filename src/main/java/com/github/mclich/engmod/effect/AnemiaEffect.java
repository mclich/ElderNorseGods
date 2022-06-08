package com.github.mclich.engmod.effect;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGEffects;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.GameSettings;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.util.InputMappings.Input;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
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

public class AnemiaEffect extends Effect
{
	public static final String ID="anemia";
	
	private final Input keyDrop;
	private final Input keySwapOffhand;
	
	public AnemiaEffect()
	{
		super(EffectType.HARMFUL, 0xDAE054);
		GameSettings options=Minecraft.getInstance().options;
		this.keyDrop=options.keyDrop.getKey();
		this.keySwapOffhand=options.keySwapOffhand.getKey();
		//dizziness, stun, vertigo
	}
	
	public static EffectInstance getInstance()
	{
		return new EffectInstance(ENGEffects.ANEMIA.get(), 400, 0);
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return true;
	}
	
	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		PlayerEntity player=Minecraft.getInstance().player;
		if(entity.getCommandSenderWorld().isClientSide()&&entity==player)
		{
			GameSettings options=Minecraft.getInstance().options;
			if(entity.getEffect(this).getDuration()==400||!player.abilities.instabuild)
			{
				options.keyDrop.setKey(InputMappings.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_UNKNOWN));
				options.keySwapOffhand.setKey(InputMappings.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_UNKNOWN));
			}
			if(entity.getEffect(this).getDuration()==1||player.abilities.instabuild)
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
		
		private static void drawSlots(ContainerScreen<?> screen, MatrixStack matrixStack, Fill fill)
		{
			boolean flag=fill==Fill.ITEM;
			PlayerEntity player=screen.getMinecraft().player;
			if(!player.hasEffect(ENGEffects.ANEMIA.get())||player.abilities.instabuild) return;
			int i=(flag?0:1)*screen.getGuiLeft();
			int j=(flag?0:1)*screen.getGuiTop();
			for(Slot slot:screen.getMenu().slots)
			{
				if(flag) EventHandler.enableDepthFunc();
				IngameGui.fill(matrixStack, slot.x+i, slot.y+j, slot.x+i+16, slot.y+j+16, fill.color);
				if(flag) EventHandler.disableDepthFunc();
			}
		}
		
		private static void drawHotbarSlots(ElementType type, MatrixStack matrixStack, Fill fill)
		{
			boolean flag=fill==Fill.ITEM;
			Minecraft mc=Minecraft.getInstance();
			if(type!=ElementType.HOTBAR||!mc.player.hasEffect(ENGEffects.ANEMIA.get())||mc.player.abilities.instabuild) return;
			MainWindow window=mc.getWindow();
			int i=window.getGuiScaledWidth()/2-72;
			int j=window.getGuiScaledHeight()-3;
			if(mc.player.hasItemInSlot(EquipmentSlotType.OFFHAND))
			{
				if(flag) EventHandler.enableDepthFunc();
				IngameGui.fill(matrixStack, i-29, j, i-45, j-16, fill.color);
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
				IngameGui.fill(matrixStack, i+k*20, j, i+k*20-16, j-16, fill.color);
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
		public static void drawBgHotbarSlots(RenderGameOverlayEvent.Pre event)
		{
			EventHandler.drawHotbarSlots(event.getType(), event.getMatrixStack(), Fill.SLOT);
		}
		
		@SubscribeEvent
		public static void drawFgHotbarSlots(RenderGameOverlayEvent.Post event)
		{
			EventHandler.drawHotbarSlots(event.getType(), event.getMatrixStack(), Fill.ITEM);
		}
		
		@SubscribeEvent
		public static void cancelGameClick(PlayerInteractEvent event)
		{
			PlayerEntity player=event.getPlayer();
			boolean correctEvent=!(event instanceof RightClickEmpty)&&!(event instanceof LeftClickEmpty);
			if(player.hasEffect(ENGEffects.ANEMIA.get())&&correctEvent&&!player.abilities.instabuild)
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
			PlayerEntity player=event.getGui().getMinecraft().player;
			if(event.getGui() instanceof ContainerScreen&&player.hasEffect(ENGEffects.ANEMIA.get())&&!player.abilities.instabuild) event.setCanceled(true);
		}
		
		@SubscribeEvent
		public static void cancelItemPickup(EntityItemPickupEvent event)
		{
			PlayerEntity player=event.getPlayer();
			if(player.hasEffect(ENGEffects.ANEMIA.get())&&!player.abilities.instabuild) event.setCanceled(true);
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