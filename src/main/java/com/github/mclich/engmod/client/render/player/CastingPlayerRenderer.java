package com.github.mclich.engmod.client.render.player;

import com.github.mclich.engmod.item.staff.StaffItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.PlayerModelPart;

public class CastingPlayerRenderer extends PlayerRenderer
{
	public CastingPlayerRenderer(EntityRendererProvider.Context context, boolean slim)
	{
		super(context, slim);
	}
	
	private void poseMainArmAndSleeve(boolean armFlag, boolean reverseFlag)
	{
		ModelPart mainArm=armFlag?this.model.rightArm:this.model.leftArm;
		ModelPart mainSleeve=armFlag?this.model.rightSleeve:this.model.leftSleeve;
		float x=reverseFlag?0.74F:-0.74F;
		mainArm.xRot+=x;
		mainSleeve.xRot+=x;
	}

	private void poseOffArmAndSleeve(boolean armFlag, float ageInTicks)
	{
		ModelPart offArm=armFlag?this.model.leftArm:this.model.rightArm;
		ModelPart offSleeve=armFlag?this.model.rightSleeve:this.model.leftSleeve;
		float x=3.14F+Mth.cos(ageInTicks*0.6662F)*0.25F;//(this.model.crouching?2.74F:3.14F)
		float z=armFlag?0.5F:-0.5F;//0.35F
		offArm.xRot+=x;
		offArm.zRot+=z;
		offSleeve.xRot+=x;
		offSleeve.zRot+=z;
	}
	
	@Override
	public void render(AbstractClientPlayer player, float playerYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light)
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
			poseStack.pushPose();
			this.model.attackTime=this.getAttackAnim(player, partialTicks);
			boolean shouldSit=player.isPassenger()&&(player.getVehicle()!=null&&player.getVehicle().shouldRiderSit());
			this.model.riding=shouldSit;
			this.model.young=player.isBaby();
			float bodyRotYaw=Mth.rotLerp(partialTicks, player.yBodyRotO, player.yBodyRot);
			float headRotYaw=Mth.rotLerp(partialTicks, player.yHeadRotO, player.yHeadRot);
			float netHeadYaw=headRotYaw-bodyRotYaw;
			if(shouldSit&&player.getVehicle() instanceof LivingEntity livingEntity)
			{
				bodyRotYaw=Mth.rotLerp(partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot);
				netHeadYaw=headRotYaw-bodyRotYaw;
				float tmp=Mth.wrapDegrees(netHeadYaw);
				if(tmp<-85F) tmp=-85F;
				if(tmp>=85F) tmp=85F;
				bodyRotYaw=headRotYaw-tmp;
				if(tmp*tmp>2500F) bodyRotYaw+=tmp*0.2F;
				netHeadYaw=headRotYaw-bodyRotYaw;
			}
			float headPitch=Mth.lerp(partialTicks, player.xRotO, player.getXRot());
			if(player.getPose()==Pose.SLEEPING)
			{
				Direction direction=player.getBedOrientation();
				if(direction!=null)
				{
					float height=player.getEyeHeight(Pose.STANDING)-0.1F;
					poseStack.translate(-direction.getStepX()*height, 0D, -direction.getStepZ()*height);
				}
			}
			float ageInTicks=this.getBob(player, partialTicks);
			this.setupRotations(player, poseStack, ageInTicks, bodyRotYaw, partialTicks);
			poseStack.scale(-1F, -1F, 1F);
			this.scale(player, poseStack, partialTicks);
			poseStack.translate(0D, -1.501, 0D);
			float limbSwingAmount=0F;
			float limbSwing=0F;
			if(!shouldSit&&player.isAlive())
			{
				limbSwingAmount=Mth.lerp(partialTicks, player.animationSpeedOld, player.animationSpeed);
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
			boolean armFlag=player.getUsedItemHand()==InteractionHand.MAIN_HAND;
			this.poseMainArmAndSleeve(armFlag, false);
			this.poseOffArmAndSleeve(armFlag, ageInTicks);
			if(type!=null)
			{
				VertexConsumer vertexBuilder=buffer.getBuffer(type);
				int overlay=LivingEntityRenderer.getOverlayCoords(player, this.getWhiteOverlayProgress(player, partialTicks));
				this.model.renderToBuffer(poseStack, vertexBuilder, light, overlay, 1F, 1F, 1F, isInvisibleTo?0.15F:1F);
			}
			if(!player.isSpectator())
			{
				for(RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> layer:this.layers)
				{
					if(layer instanceof ItemInHandLayer) this.poseMainArmAndSleeve(armFlag, true);
					layer.render(poseStack, buffer, light, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
					if(layer instanceof ItemInHandLayer) this.poseMainArmAndSleeve(armFlag, false);
				}
			}
			poseStack.popPose();
		}
		else super.render(player, playerYaw, partialTicks, poseStack, buffer, light);
	}
}