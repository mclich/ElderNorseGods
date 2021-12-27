package com.github.mclich.engmod.item.staff;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGTabs;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.FORGE, value=Dist.CLIENT)
public abstract class StaffItem extends Item implements IVanishable
{
	public StaffItem(Rarity rarity, int durability)
	{
		super(new Item.Properties().rarity(rarity).durability(durability).tab(ENGTabs.COMBAT));
	}
	
	protected static String toRoundedString(float number)
	{
		return number%1==0?Integer.toString((int)number):Float.toString(Math.round(number*10)/10F);
	}
	
	@Override
	public abstract boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack);
	
	public abstract void applyEffect(World world, LivingEntity entity, ItemStack itemStack);
	
	@Override
	public UseAction getUseAnimation(ItemStack itemStack)
	{
		return UseAction.BOW;
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void renderArms(RenderPlayerEvent.Pre event)
	{
		if(event.getPlayer().getUseItem().getItem() instanceof StaffItem)
		{
			PlayerRenderer renderer=event.getRenderer();
			PlayerModel<AbstractClientPlayerEntity> model=renderer.getModel();
			model.rightArm.xRot=model.rightArm.xRot*0.5F-(float)Math.PI;
			model.rightArm.yRot=0.0F;
			model.leftArm.xRot=model.leftArm.xRot*0.5F-(float)Math.PI;
			model.leftArm.yRot=0.0F;
			model.leftArm.visible=false;
			//renderer.render((ClientPlayerEntity)event.getPlayer(), -0.1F, event.getPartialRenderTick(), event.getMatrixStack(), event.getBuffers(), renderer.getPackedLightCoords((ClientPlayerEntity)event.getPlayer(), event.getPartialRenderTick()));
			renderer.renderRightHand(event.getMatrixStack(), event.getBuffers(), renderer.getPackedLightCoords((ClientPlayerEntity)event.getPlayer(), event.getPartialRenderTick()), (ClientPlayerEntity)event.getPlayer());
			renderer.renderLeftHand(event.getMatrixStack(), event.getBuffers(), renderer.getPackedLightCoords((ClientPlayerEntity)event.getPlayer(), event.getPartialRenderTick()), (ClientPlayerEntity)event.getPlayer());
		}
	}
}