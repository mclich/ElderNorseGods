package com.github.mclich.engmod.client.render;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.client.model.ValkyrieModel;
import com.github.mclich.engmod.entity.ValkyrieEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class ValkyrieRenderer extends MobRenderer<ValkyrieEntity, ValkyrieModel>
{
	private static final ResourceLocation TEXTURE=new ResourceLocation(ElderNorseGods.MOD_ID, "textures/entity/"+ValkyrieEntity.ID+".png");
	
	public ValkyrieRenderer(EntityRendererManager renderManager)
	{
		super(renderManager, new ValkyrieModel(), 0.5F);
	}
	
	@Override
	public ResourceLocation getTextureLocation(ValkyrieEntity entity)
	{
		return ValkyrieRenderer.TEXTURE;
	}
}