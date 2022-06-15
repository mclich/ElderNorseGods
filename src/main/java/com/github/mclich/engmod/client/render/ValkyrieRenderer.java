package com.github.mclich.engmod.client.render;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.client.model.ValkyrieModel;
import com.github.mclich.engmod.entity.ValkyrieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ValkyrieRenderer extends MobRenderer<ValkyrieEntity, ValkyrieModel>
{
	private static final ResourceLocation TEXTURE=new ResourceLocation(ElderNorseGods.MOD_ID, "textures/entity/"+ValkyrieEntity.ID+".png");
	
	public ValkyrieRenderer(EntityRendererProvider.Context context)
	{
		super(context, new ValkyrieModel(context.getModelSet().bakeLayer(ValkyrieModel.LAYER_LOCATION)), 0.5F);
	}
	
	@Override
	public ResourceLocation getTextureLocation(ValkyrieEntity valkyrie)
	{
		return ValkyrieRenderer.TEXTURE;
	}
}