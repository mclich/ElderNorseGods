package com.github.mclich.engmod.register;

import java.util.HashMap;
import java.util.Map;
import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.client.render.*;
import com.github.mclich.engmod.client.render.player.CastingPlayerRenderer;
import com.github.mclich.engmod.entity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.Builder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
public abstract class ENGEntities
{
	//.setShouldReceiveVelocityUpdates(false).setTrackingRange(32).setUpdateInterval(3)
	public static final EntityType<CustomEntity> CUSTOM_ENTITY=Builder.of(CustomEntity::new, EntityClassification.CREATURE).sized(1F, 2F).build(new ResourceLocation(ElderNorseGods.MOD_ID, CustomEntity.ID).toString());
	public static final EntityType<ValkyrieEntity> VALKYRIE=Builder.of(ValkyrieEntity::new, EntityClassification.MONSTER).sized(1F, 2F).build(new ResourceLocation(ElderNorseGods.MOD_ID, ValkyrieEntity.ID).toString());
	
	@SubscribeEvent
	public static void registerEntities(Register<EntityType<?>> event)
	{
		event.getRegistry().register(ENGEntities.VALKYRIE.setRegistryName(new ResourceLocation(ElderNorseGods.MOD_ID, ValkyrieEntity.ID)));
	}
	
	@SubscribeEvent
	public static void registerEntityAttributes(EntityAttributeCreationEvent event)
	{
		event.put(ENGEntities.VALKYRIE, ValkyrieEntity.createAttributes().build());
	}
	
	@SubscribeEvent
    public static void registerEntityRenderers(FMLClientSetupEvent event)
    {
		RenderingRegistry.registerEntityRenderingHandler(ENGEntities.VALKYRIE, ValkyrieRenderer::new);
    }
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void registerPlayerRenderers(FMLClientSetupEvent event)
	{
		event.enqueueWork(()->
		{
			Map<String, PlayerRenderer> renderers=new HashMap<>();
			EntityRendererManager manager=Minecraft.getInstance().getEntityRenderDispatcher();
			manager.getSkinMap().forEach((s, r)->renderers.put(s, new CastingPlayerRenderer(manager, s.equals("slim"))));
			ObfuscationReflectionHelper.setPrivateValue(EntityRendererManager.class, manager, renderers, "playerRenderers");
		});
	}
}