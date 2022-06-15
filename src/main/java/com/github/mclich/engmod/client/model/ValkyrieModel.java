package com.github.mclich.engmod.client.model;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.entity.ValkyrieEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class ValkyrieModel extends EntityModel<ValkyrieEntity>
{
	public static final ModelLayerLocation LAYER_LOCATION=new ModelLayerLocation(new ResourceLocation(ElderNorseGods.MOD_ID, "valkyrie"), "body");

	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;
	private final ModelPart rightWing;
	private final ModelPart leftWing;

	public ValkyrieModel(ModelPart root)
	{
		this.head=root.getChild("head");
		this.body=root.getChild("body");
		this.rightArm=root.getChild("rightArm");
		this.leftArm=root.getChild("leftArm");
		this.rightLeg=root.getChild("rightLeg");
		this.leftLeg=root.getChild("leftLeg");
		this.rightWing=root.getChild("rightWing");
		this.leftWing=root.getChild("leftWing");
	}

	@SuppressWarnings("unused")
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh=new MeshDefinition();
		PartDefinition parts=mesh.getRoot();
		PartDefinition head=parts.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.5F, -3.5F, 1.5F));
		PartDefinition head1=head.addOrReplaceChild("head1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.4974F, -16.4061F, -2.9261F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0026F, 12.9061F, -2.4261F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition helmet=head.addOrReplaceChild("helmet", CubeListBuilder.create()
				.texOffs(28, 0).addBox(-1.5F, 0.1667F, -7.5F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(60, 6).addBox(0.5F, -1.8333F, -7.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -2.6667F, 0.5F));
		PartDefinition leftWing1=helmet.addOrReplaceChild("leftWing1", CubeListBuilder.create().texOffs(36, 34).addBox(2.8554F, -14.5304F, 7.4734F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5026F, 15.5727F, -2.9261F, 0.7105F, 0.2319F, 0.0987F));
		PartDefinition rightWing1=helmet.addOrReplaceChild("rightWing1", CubeListBuilder.create().texOffs(28, 30).addBox(-3.8604F, -14.5301F, 7.4746F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5026F, 15.5727F, -2.9261F, 0.7105F, -0.2319F, -0.0987F));
		PartDefinition body=parts.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 9).addBox(-2.0F, -6.5F, -5.75F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(44, 26).addBox(-1.5F, -1.5F, -5.25F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(57, 47).addBox(-1.0F, 0.5F, -4.75F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(28, 19).addBox(-2.5F, 4.5F, -5.25F, 7.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(28, 24).addBox(-2.0F, 2.5F, -5.25F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, 1.75F));
		PartDefinition cape=body.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(50, 9).addBox(-3.0F, 3.0F, -1.24F, 8.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(20, 54).addBox(-3.01F, 4.0F, -5.25F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(44, 44).addBox(-3.0F, 5.0F, -5.26F, 8.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(12, 54).addBox(5.01F, 4.0F, -5.25F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rightArm=parts.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(-8.5F, 6.9F, 1.5F));
		PartDefinition rightArmLower=rightArm.addOrReplaceChild("rightArmLower", CubeListBuilder.create().texOffs(54, 54).addBox(-6.6191F, -8.9569F, -1.5739F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0026F, 2.5061F, -2.4261F, 0.0F, 0.0F, 0.1309F));
		PartDefinition rightArmUpper=rightArm.addOrReplaceChild("rightArmUpper", CubeListBuilder.create().texOffs(52, 0).addBox(-5.587F, -10.8209F, -2.0739F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0026F, 2.5061F, -2.4261F, 0.0F, 0.0F, -0.0873F));
		PartDefinition leftArm=parts.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(7.5F, 6.5F, 1.5F));
		PartDefinition leftArmLower=leftArm.addOrReplaceChild("leftArmLower", CubeListBuilder.create().texOffs(28, 51).addBox(4.5711F, -8.7049F, -1.5739F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9974F, 2.9061F, -2.4261F, 0.0F, 0.0F, -0.1309F));
		PartDefinition leftArmUpper=leftArm.addOrReplaceChild("leftArmUpper", CubeListBuilder.create().texOffs(21, 0).addBox(1.57F, -11.0846F, -2.0739F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9974F, 2.9061F, -2.4261F, 0.0F, 0.0F, 0.0873F));
		PartDefinition rightLeg=parts.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 58).addBox(-0.5F, -5.0F, -4.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(46, 15).addBox(-1.0F, 0.0F, -5.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(60, 35).addBox(-0.5F, 2.0F, -4.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(44, 32).addBox(-1.0F, 3.0F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(50, 39).addBox(-1.0F, -6.0F, -5.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 18.0F, 1.5F));
		PartDefinition rightWing2=rightLeg.addOrReplaceChild("rightWing2", CubeListBuilder.create().texOffs(58, 17).addBox(-4.4273F, 8.4426F, -2.4578F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0026F, -8.5939F, -2.4261F, 0.4894F, -0.2426F, -0.0826F));
		PartDefinition leftLeg=parts.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(36, 56).addBox(-0.5F, -5.0F, -4.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(40, 50).addBox(-1.0F, 0.0F, -5.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(59, 24).addBox(-0.5F, 2.0F, -4.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(28, 44).addBox(-1.0F, 3.0F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(46, 21).addBox(-1.0F, -6.0F, -5.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 18.0F, 1.5F));
		PartDefinition leftWing2=leftLeg.addOrReplaceChild("leftWing2", CubeListBuilder.create().texOffs(58, 28).addBox(3.4222F, 8.4424F, -2.4591F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9974F, -8.5939F, -2.4261F, 0.4894F, 0.2426F, 0.0826F));
		PartDefinition rightWing=parts.addOrReplaceChild("rightWing", CubeListBuilder.create(), PartPose.offset(-1.0F, 24.0F, 0.0F));
		PartDefinition rightWing3=rightWing.addOrReplaceChild("rightWing3", CubeListBuilder.create().texOffs(0, 22).addBox(-0.9139F, -6.2683F, -15.1549F, 0.0F, 22.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5026F, -14.5939F, -0.9261F, -3.0728F, -0.4071F, 0.0105F));
		PartDefinition leftWing=parts.addOrReplaceChild("leftWing", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition leftWing3=leftWing.addOrReplaceChild("leftWing3", CubeListBuilder.create().texOffs(0, 0).addBox(0.8273F, -6.3057F, -15.5471F, 0.0F, 22.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5026F, -14.5939F, -0.9261F, -3.0728F, 0.4071F, -0.0105F));
		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(ValkyrieEntity valkyrie, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int light, int overlay, float red, float green, float blue, float alpha)
	{
		this.head.render(poseStack, buffer, light, overlay, red, green, blue, alpha);
		this.body.render(poseStack, buffer, light, overlay, red, green, blue, alpha);
		this.rightArm.render(poseStack, buffer, light, overlay, red, green, blue, alpha);
		this.leftArm.render(poseStack, buffer, light, overlay, red, green, blue, alpha);
		this.rightLeg.render(poseStack, buffer, light, overlay, red, green, blue, alpha);
		this.leftLeg.render(poseStack, buffer, light, overlay, red, green, blue, alpha);
		this.rightWing.render(poseStack, buffer, light, overlay, red, green, blue, alpha);
		this.leftWing.render(poseStack, buffer, light, overlay, red, green, blue, alpha);
	}
}