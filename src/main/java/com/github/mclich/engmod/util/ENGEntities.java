package com.github.mclich.engmod.util;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.client.render.*;
import com.github.mclich.engmod.entity.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.Builder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.MOD)
public abstract class ENGEntities
{
	//.setShouldReceiveVelocityUpdates(false).setTrackingRange(32).setUpdateInterval(3)
	public static final EntityType<CustomEntity> CUSTOM_ENTITY=Builder.of(CustomEntity::new, EntityClassification.CREATURE).sized(1.0f, 2.0f).build(new ResourceLocation(ElderNorseGods.MOD_ID, CustomEntity.ID).toString());
	public static final EntityType<ValkyrieEntity> VALKYRIE_ENTITY=Builder.of(ValkyrieEntity::new, EntityClassification.MONSTER).sized(1.0f, 2.0f).build(new ResourceLocation(ElderNorseGods.MOD_ID, ValkyrieEntity.ID).toString());
	
	@SubscribeEvent
	public static void registerEntities(Register<EntityType<?>> event)
	{
		event.getRegistry().register(ENGEntities.VALKYRIE_ENTITY.setRegistryName(new ResourceLocation(ElderNorseGods.MOD_ID, ValkyrieEntity.ID)));
	}
	
	@SubscribeEvent
	public static void registerEntityAttributes(EntityAttributeCreationEvent event)
	{
		event.put(ENGEntities.VALKYRIE_ENTITY, ValkyrieEntity.createAttributes().build());
	}
	
	@SubscribeEvent
    public static void registerEntityRenderers(FMLClientSetupEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(ENGEntities.VALKYRIE_ENTITY, ValkyrieRenderer::new);
    }
}