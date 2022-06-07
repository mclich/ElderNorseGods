package com.github.mclich.engmod.client.render.player;

import com.github.mclich.engmod.item.staff.StaffItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel.ArmPose;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class CastingPlayerRenderer extends PlayerRenderer
{
	public CastingPlayerRenderer(EntityRendererManager renderManager, boolean slim)
	{
		super(renderManager, slim);
	}
	
	private void poseMainArmAndSleeve(boolean armFlag, boolean reverseFlag)
	{
		ModelRenderer mainArm=armFlag?this.model.rightArm:this.model.leftArm;
		ModelRenderer mainSleeve=armFlag?this.model.rightSleeve:this.model.leftSleeve;
		float x=reverseFlag?0.74F:-0.74F;
		mainArm.xRot+=x;
		mainSleeve.xRot+=x;
	}
	private void poseOffArmAndSleeve(boolean armFlag, float ageInTicks)
	{
		ModelRenderer offArm=armFlag?this.model.leftArm:this.model.rightArm;
		ModelRenderer offSleeve=armFlag?this.model.rightSleeve:this.model.leftSleeve;
		float x=3.14F+MathHelper.cos(ageInTicks*0.6662F)*0.25F;//(this.model.crouching?2.74F:3.14F)
		float z=armFlag?0.5F:-0.5F;//0.35F
		offArm.xRot+=x;
		offArm.zRot+=z;
		offSleeve.xRot+=x;
		offSleeve.zRot+=z;
	}
	
	@Override
	public void render(AbstractClientPlayerEntity player, float playerYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light)
	{
		if(player.isUsingItem()&&player.getUseItem().getItem() instanceof StaffItem)
		{
			if(player.isSpectator())
			{
				this.model.setAllVisible(false);
				this.model.head.visible=true;
				this.model.hat.visible=true;
			}
			else
			{
				this.model.setAllVisible(true);
				this.model.hat.visible=player.isModelPartShown(PlayerModelPart.HAT);
				this.model.jacket.visible=player.isModelPartShown(PlayerModelPart.JACKET);
				this.model.leftPants.visible=player.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
				this.model.rightPants.visible=player.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
				this.model.leftSleeve.visible=player.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
				this.model.rightSleeve.visible=player.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
				this.model.crouching=player.isCrouching();
				this.model.rightArmPose=ArmPose.EMPTY;
				this.model.leftArmPose=ArmPose.EMPTY;
			}
			matrixStack.pushPose();
			this.model.attackTime=this.getAttackAnim(player, partialTicks);
			boolean shouldSit=player.isPassenger()&&(player.getVehicle()!=null&&player.getVehicle().shouldRiderSit());
			this.model.riding=shouldSit;
			this.model.young=player.isBaby();
			float bodyRotYaw=MathHelper.rotLerp(partialTicks, player.yBodyRotO, player.yBodyRot);
			float headRotYaw=MathHelper.rotLerp(partialTicks, player.yHeadRotO, player.yHeadRot);
			float netHeadYaw=headRotYaw-bodyRotYaw;
			if(shouldSit&&player.getVehicle() instanceof LivingEntity)
			{
				LivingEntity livingentity=(LivingEntity)player.getVehicle();
				bodyRotYaw=MathHelper.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
				netHeadYaw=headRotYaw-bodyRotYaw;
				float tmp=MathHelper.wrapDegrees(netHeadYaw);
				if(tmp<-85F) tmp=-85F;
				if(tmp>=85F) tmp=85F;
				bodyRotYaw=headRotYaw-tmp;
				if(tmp*tmp>2500F) bodyRotYaw+=tmp*0.2F;
				netHeadYaw=headRotYaw-bodyRotYaw;
			}
			float headPitch=MathHelper.lerp(partialTicks, player.xRotO, player.xRot);
			if(player.getPose()==Pose.SLEEPING)
			{
				Direction direction=player.getBedOrientation();
				if(direction!=null)
				{
					float height=player.getEyeHeight(Pose.STANDING)-0.1F;
					matrixStack.translate(-direction.getStepX()*height, 0D, -direction.getStepZ()*height);
				}
			}
			float ageInTicks=this.getBob(player, partialTicks);
			this.setupRotations(player, matrixStack, ageInTicks, bodyRotYaw, partialTicks);
			matrixStack.scale(-1F, -1F, 1F);
			this.scale(player, matrixStack, partialTicks);
			matrixStack.translate(0D, -1.501, 0D);
			float limbSwingAmount=0F;
			float limbSwing=0F;
			if(!shouldSit&&player.isAlive())
			{
				limbSwingAmount=MathHelper.lerp(partialTicks, player.animationSpeedOld, player.animationSpeed);
				limbSwing=player.animationPosition-player.animationSpeed*(1F-partialTicks);
				if(player.isBaby()) limbSwing*=3F;
				if(limbSwingAmount>1F) limbSwingAmount=1F;
			}
			this.model.prepareMobModel(player, limbSwing, limbSwingAmount, partialTicks);
			this.model.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			Minecraft mc=Minecraft.getInstance();
			boolean isInvisible=this.isBodyVisible(player);
			boolean isInvisibleTo=!isInvisible&&!player.isInvisibleTo(mc.player);
			RenderType type=this.getRenderType(player, isInvisible, isInvisibleTo, mc.shouldEntityAppearGlowing(player));
			boolean armFlag=player.getUsedItemHand()==Hand.MAIN_HAND;
			this.poseMainArmAndSleeve(armFlag, false);
			this.poseOffArmAndSleeve(armFlag, ageInTicks);
			if(type!=null)
			{
				IVertexBuilder vertexBuilder=buffer.getBuffer(type);
				int overlay=LivingRenderer.getOverlayCoords(player, this.getWhiteOverlayProgress(player, partialTicks));
				this.model.renderToBuffer(matrixStack, vertexBuilder, light, overlay, 1F, 1F, 1F, isInvisibleTo?0.15F:1F);
			}
			if(!player.isSpectator())
			{
				for(LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> layer:this.layers)
				{
					if(layer instanceof HeldItemLayer) this.poseMainArmAndSleeve(armFlag, true);
					layer.render(matrixStack, buffer, light, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
					if(layer instanceof HeldItemLayer) this.poseMainArmAndSleeve(armFlag, false);
				}
			}
			matrixStack.popPose();
		}
		else super.render(player, playerYaw, partialTicks, matrixStack, buffer, light);
	}
}