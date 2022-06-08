package com.github.mclich.engmod.client.model;

import com.github.mclich.engmod.entity.ValkyrieEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;


public class ValkyrieModel extends EntityModel<ValkyrieEntity>
{
	private final ModelRenderer head;
	private final ModelRenderer head1;
	private final ModelRenderer helmet;
	private final ModelRenderer leftWing1;
	private final ModelRenderer rightWing1;
	private final ModelRenderer body;
	private final ModelRenderer cape;
	private final ModelRenderer rightArm;
	private final ModelRenderer rightArmLower1;
	private final ModelRenderer rightArmUpper1;
	private final ModelRenderer leftArm;
	private final ModelRenderer leftArmLower1;
	private final ModelRenderer leftArmUpper1;
	private final ModelRenderer rightLeg;
	private final ModelRenderer rightWing2;
	private final ModelRenderer leftLeg;
	private final ModelRenderer leftWing2;
	private final ModelRenderer rightWing;
	private final ModelRenderer rightWing3;
	private final ModelRenderer leftWing;
	private final ModelRenderer leftWing3;

	public ValkyrieModel()
	{
		this.texWidth=128;
		this.texHeight=128;

		this.head=new ModelRenderer(this);
		this.head.setPos(-0.5F, -3.5F, 1.5F);

		this.head1=new ModelRenderer(this);
		this.head1.setPos(1.0026F, 12.9061F, -2.4261F);
		this.head.addChild(this.head1);
		this.setRotationAngles(this.head1, -3.1416F, 0.0F, 3.1416F);
		this.head1.texOffs(0, 0).addBox(-3.4974F, -16.4061F, -2.9261F, 7.0F, 7.0F, 7.0F, 0.0F, false);

		this.helmet=new ModelRenderer(this);
		this.helmet.setPos(-1.5F, -2.6667F, 0.5F);
		this.head.addChild(this.helmet);
		this.helmet.texOffs(28, 0).addBox(-1.5F, 0.1667F, -7.5F, 8.0F, 1.0F, 8.0F, 0.0F, false);
		this.helmet.texOffs(60, 6).addBox(0.5F, -1.8333F, -7.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);

		this.leftWing1=new ModelRenderer(this);
		this.leftWing1.setPos(2.5026F, 15.5727F, -2.9261F);
		this.helmet.addChild(this.leftWing1);
		this.setRotationAngles(this.leftWing1, 0.7105F, 0.2319F, 0.0987F);
		this.leftWing1.texOffs(36, 34).addBox(2.8554F, -14.5304F, 7.4734F, 1.0F, 4.0F, 6.0F, 0.0F, false);

		this.rightWing1=new ModelRenderer(this);
		this.rightWing1.setPos(2.5026F, 15.5727F, -2.9261F);
		this.helmet.addChild(this.rightWing1);
		this.setRotationAngles(this.rightWing1, 0.7105F, -0.2319F, -0.0987F);
		this.rightWing1.texOffs(28, 30).addBox(-3.8604F, -14.5301F, 7.4746F, 1.0F, 4.0F, 6.0F, 0.0F, false);

		this.body=new ModelRenderer(this);
		this.body.setPos(-0.5F, 6.5F, 1.75F);
		this.body.texOffs(28, 9).addBox(-2.0F, -6.5F, -5.75F, 6.0F, 5.0F, 5.0F, 0.0F, false);
		this.body.texOffs(44, 26).addBox(-1.5F, -1.5F, -5.25F, 5.0F, 2.0F, 4.0F, 0.0F, false);
		this.body.texOffs(57, 47).addBox(-1.0F, 0.5F, -4.75F, 4.0F, 2.0F, 3.0F, 0.0F, false);
		this.body.texOffs(28, 19).addBox(-2.5F, 4.5F, -5.25F, 7.0F, 1.0F, 4.0F, 0.0F, false);
		this.body.texOffs(28, 24).addBox(-2.0F, 2.5F, -5.25F, 6.0F, 2.0F, 4.0F, 0.0F, false);

		this.cape=new ModelRenderer(this);
		this.cape.setPos(0.0F, 0.0F, 0.0F);
		this.body.addChild(this.cape);
		this.cape.texOffs(50, 9).addBox(-3.0F, 3.0F, -1.24F, 8.0F, 6.0F, 0.0F, 0.0F, false);
		this.cape.texOffs(20, 54).addBox(-3.01F, 4.0F, -5.25F, 0.0F, 6.0F, 4.0F, 0.0F, false);
		this.cape.texOffs(44, 44).addBox(-3.0F, 5.0F, -5.26F, 8.0F, 6.0F, 0.0F, 0.0F, false);
		this.cape.texOffs(12, 54).addBox(5.01F, 4.0F, -5.25F, 0.0F, 6.0F, 4.0F, 0.0F, false);

		this.rightArm=new ModelRenderer(this);
		this.rightArm.setPos(-8.5F, 6.9F, 1.5F);

		this.rightArmLower1=new ModelRenderer(this);
		this.rightArmLower1.setPos(9.0026F, 2.5061F, -2.4261F);
		this.rightArm.addChild(this.rightArmLower1);
		this.setRotationAngles(this.rightArmLower1, 0.0F, 0.0F, 0.1309F);
		this.rightArmLower1.texOffs(54, 54).addBox(-6.6191F, -8.9569F, -1.5739F, 2.0F, 12.0F, 2.0F, 0.0F, false);

		this.rightArmUpper1=new ModelRenderer(this);
		this.rightArmUpper1.setPos(9.0026F, 2.5061F, -2.4261F);
		this.rightArm.addChild(this.rightArmUpper1);
		this.setRotationAngles(this.rightArmUpper1, 0.0F, 0.0F, -0.0873F);
		this.rightArmUpper1.texOffs(52, 0).addBox(-5.587F, -10.8209F, -2.0739F, 4.0F, 3.0F, 3.0F, 0.0F, false);

		this.leftArm=new ModelRenderer(this);
		this.leftArm.setPos(7.5F, 6.5F, 1.5F);

		this.leftArmLower1=new ModelRenderer(this);
		this.leftArmLower1.setPos(-6.9974F, 2.9061F, -2.4261F);
		this.leftArm.addChild(this.leftArmLower1);
		this.setRotationAngles(this.leftArmLower1, 0.0F, 0.0F, -0.1309F);
		this.leftArmLower1.texOffs(28, 51).addBox(4.5711F, -8.7049F, -1.5739F, 2.0F, 12.0F, 2.0F, 0.0F, false);

		this.leftArmUpper1=new ModelRenderer(this);
		this.leftArmUpper1.setPos(-6.9974F, 2.9061F, -2.4261F);
		this.leftArm.addChild(this.leftArmUpper1);
		this.setRotationAngles(this.leftArmUpper1, 0.0F, 0.0F, 0.0873F);
		this.leftArmUpper1.texOffs(21, 0).addBox(1.57F, -11.0846F, -2.0739F, 4.0F, 3.0F, 3.0F, 0.0F, false);

		this.rightLeg=new ModelRenderer(this);
		this.rightLeg.setPos(-2.5F, 18.0F, 1.5F);
		this.rightLeg.texOffs(0, 58).addBox(-0.5F, -5.0F, -4.5F, 3.0F, 5.0F, 3.0F, 0.0F, false);
		this.rightLeg.texOffs(46, 15).addBox(-1.0F, 0.0F, -5.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);
		this.rightLeg.texOffs(60, 35).addBox(-0.5F, 2.0F, -4.5F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		this.rightLeg.texOffs(44, 32).addBox(-1.0F, 3.0F, -5.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);
		this.rightLeg.texOffs(50, 39).addBox(-1.0F, -6.0F, -5.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

		this.rightWing2=new ModelRenderer(this);
		this.rightWing2.setPos(3.0026F, -8.5939F, -2.4261F);
		this.rightLeg.addChild(this.rightWing2);
		this.setRotationAngles(this.rightWing2, 0.4894F, -0.2426F, -0.0826F);
		this.rightWing2.texOffs(58, 17).addBox(-4.4273F, 8.4426F, -2.4578F, 1.0F, 3.0F, 4.0F, 0.0F, false);

		this.leftLeg=new ModelRenderer(this);
		this.leftLeg.setPos(1.5F, 18.0F, 1.5F);
		this.leftLeg.texOffs(36, 56).addBox(-0.5F, -5.0F, -4.5F, 3.0F, 5.0F, 3.0F, 0.0F, false);
		this.leftLeg.texOffs(40, 50).addBox(-1.0F, 0.0F, -5.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);
		this.leftLeg.texOffs(59, 24).addBox(-0.5F, 2.0F, -4.5F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		this.leftLeg.texOffs(28, 44).addBox(-1.0F, 3.0F, -5.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);
		this.leftLeg.texOffs(46, 21).addBox(-1.0F, -6.0F, -5.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

		this.leftWing2=new ModelRenderer(this);
		this.leftWing2.setPos(-0.9974F, -8.5939F, -2.4261F);
		this.leftLeg.addChild(this.leftWing2);
		this.setRotationAngles(this.leftWing2, 0.4894F, 0.2426F, 0.0826F);
		this.leftWing2.texOffs(58, 28).addBox(3.4222F, 8.4424F, -2.4591F, 1.0F, 3.0F, 4.0F, 0.0F, false);

		this.rightWing=new ModelRenderer(this);
		this.rightWing.setPos(-1.0F, 24.0F, 0.0F);

		this.rightWing3=new ModelRenderer(this);
		this.rightWing3.setPos(1.5026F, -14.5939F, -0.9261F);
		this.rightWing.addChild(this.rightWing3);
		this.setRotationAngles(this.rightWing3, -3.0728F, -0.4071F, 0.0105F);
		this.rightWing3.texOffs(0, 22).addBox(-0.9139F, -6.2683F, -15.1549F, 0.0F, 22.0F, 14.0F, 0.0F, false);

		this.leftWing=new ModelRenderer(this);
		this.leftWing.setPos(0.0F, 24.0F, 0.0F);

		this.leftWing3=new ModelRenderer(this);
		this.leftWing3.setPos(0.5026F, -14.5939F, -0.9261F);
		this.leftWing.addChild(this.leftWing3);
		this.setRotationAngles(this.leftWing3, -3.0728F, 0.4071F, -0.0105F);
		this.leftWing3.texOffs(0, 0).addBox(0.8273F, -6.3057F, -15.5471F, 0.0F, 22.0F, 14.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(ValkyrieEntity valkyrie, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		/*
		if(valkyrie.isOnGround()&&true)
		{
			float f=1F;
			if(valkyrie.getFallFlyingTicks()>4)
			{
				f=(float)valkyrie.getDeltaMovement().lengthSqr();
				f/=0.2F;
				f=(float)Math.pow(f, 3D);
			}
			if(f<1F) f=1F;
			this.rightLeg.xRot=MathHelper.cos(limbSwing*0.6662F)*1.4F*limbSwingAmount/f;
			this.rightLeg.yRot=0F;
			this.rightLeg.zRot=0F;
			this.leftLeg.xRot=MathHelper.cos(limbSwing*0.6662F+(float)Math.PI)*1.4F*limbSwingAmount/f;
			this.leftLeg.yRot=0F;
			this.leftLeg.zRot=0F;
		}
		*/
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int light, int overlay, float red, float green, float blue, float alpha)
	{
		this.head.render(matrixStack, buffer, light, overlay);
		this.body.render(matrixStack, buffer, light, overlay);
		this.rightArm.render(matrixStack, buffer, light, overlay);
		this.leftArm.render(matrixStack, buffer, light, overlay);
		this.rightLeg.render(matrixStack, buffer, light, overlay);
		this.leftLeg.render(matrixStack, buffer, light, overlay);
		this.rightWing.render(matrixStack, buffer, light, overlay);
		this.leftWing.render(matrixStack, buffer, light, overlay);
	}

	public void setRotationAngles(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.xRot=x;
		modelRenderer.yRot=y;
		modelRenderer.zRot=z;
	}
}