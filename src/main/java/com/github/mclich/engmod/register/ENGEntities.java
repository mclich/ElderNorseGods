package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.client.model.ValkyrieModel;
import com.github.mclich.engmod.client.render.ValkyrieRenderer;
import com.github.mclich.engmod.client.render.player.CastingPlayerRenderer;
import com.github.mclich.engmod.entity.ValkyrieEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.Map;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.MOD)
public abstract class ENGEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITIES=DeferredRegister.create(ForgeRegistries.ENTITIES, ElderNorseGods.MOD_ID);

	public static final RegistryObject<EntityType<ValkyrieEntity>> VALKYRIE=ENGEntities.ENTITIES.register(ValkyrieEntity.ID, ()->Builder.of(ValkyrieEntity::new, MobCategory.MONSTER).sized(1F, 2F).build(new ResourceLocation(ElderNorseGods.MOD_ID, ValkyrieEntity.ID).toString()));
	
	@SubscribeEvent
	public static void registerEntityAttributes(EntityAttributeCreationEvent event)
	{
		event.put(ENGEntities.VALKYRIE.get(), ValkyrieEntity.createAttributes().build());
	}

	@SubscribeEvent
	public static void registerEntityLayers(RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(ValkyrieModel.LAYER_LOCATION, ValkyrieModel::createBodyLayer);
	}

	@SubscribeEvent
    public static void registerEntityRenderers(RegisterRenderers event)
    {
		event.registerEntityRenderer(ENGEntities.VALKYRIE.get(), ValkyrieRenderer::new);
    }

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void registerPlayerRenderers(EntityRenderersEvent.AddLayers event)
	{
		Minecraft mc=Minecraft.getInstance();
		EntityRenderDispatcher dispatcher=mc.getEntityRenderDispatcher();
		EntityRendererProvider.Context context=new EntityRendererProvider.Context(dispatcher, mc.getItemRenderer(), mc.getResourceManager(), event.getEntityModels(), mc.font);
		Map<String, EntityRenderer<? extends Player>> playerRenderers=ImmutableMap.of("default", new CastingPlayerRenderer(context, false), "slim", new CastingPlayerRenderer(context, true));
		ObfuscationReflectionHelper.setPrivateValue(EntityRenderDispatcher.class, dispatcher, playerRenderers, "playerRenderers");
	}
}