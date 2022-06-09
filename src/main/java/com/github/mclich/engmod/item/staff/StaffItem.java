package com.github.mclich.engmod.item.staff;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.particle.StaffParticleData;
import com.github.mclich.engmod.register.ENGTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public abstract class StaffItem extends Item implements IVanishable
{
	protected int particleColor;
	
	public StaffItem(Rarity rarity, int particleColor, int durability)
	{
		super(new Item.Properties().rarity(rarity).durability(durability).tab(ENGTabs.COMBAT));
		this.particleColor=particleColor;
	}
	
	protected static boolean hasPlayerShieldEquipped(PlayerEntity player, Hand useItemHand)
	{
		return player.getItemInHand(useItemHand==Hand.MAIN_HAND?Hand.OFF_HAND:Hand.MAIN_HAND).isShield(player);
	}
	
	@Override
	public abstract boolean isValidRepairItem(ItemStack staffStack, ItemStack repairStack);
	
	public abstract void applyEffect(World world, LivingEntity entity, ItemStack itemStack);
	
	@Override
	public UseAction getUseAnimation(ItemStack itemStack)
	{
		return UseAction.BOW;
	}
	
	@Override
	public void onUseTick(World world, LivingEntity entity, ItemStack itemStack, int ticks)
	{
		if(ticks%2==0) entity.playSound(SoundEvents.PARROT_IMITATE_VEX, 0.7F, world.random.nextFloat()*0.7F+0.3F);
		if(!world.isClientSide()) ((ServerWorld)world).sendParticles(new StaffParticleData(this.particleColor), entity.getX(), entity.getY(), entity.getZ(), 1, 0D, 0D, 0D, 1D);
		//SoundEvents.EVOKER_PREPARE_SUMMON
	}
	
	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
	private static abstract class EventHandler
	{
		@SubscribeEvent
		public static void renderFPV(RenderHandEvent event)
		{
			PlayerEntity player=Minecraft.getInstance().player;
			if(player.isUsingItem()&&player.getUseItem().getItem() instanceof StaffItem&&player.getUsedItemHand()!=event.getHand())
			{
				event.setCanceled(true);
			}
		}
	}
}