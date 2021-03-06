package com.github.mclich.engmod.item.staff;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.particle.StaffParticleOptions;
import com.github.mclich.engmod.register.ENGItemTiers.StaffTier;
import com.github.mclich.engmod.register.ENGTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public abstract class StaffItem extends TieredItem implements Vanishable
{
	protected int particleColor;

	public StaffItem(StaffTier tier)
	{
		super(tier, new Item.Properties().rarity(tier.getRarity()).durability(tier.getUses()).tab(ENGTabs.COMBAT));
		this.particleColor=tier.getColor();
	}
	
	protected static boolean hasPlayerShieldEquipped(Player player, InteractionHand hand)
	{
		return player.getItemInHand(hand==InteractionHand.MAIN_HAND?InteractionHand.OFF_HAND:InteractionHand.MAIN_HAND).getItem()==Items.SHIELD;
	}
	
	public abstract void applyEffect(Level world, LivingEntity entity, ItemStack itemStack);
	
	@Override
	public UseAnim getUseAnimation(ItemStack itemStack)
	{
		return UseAnim.BOW;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void onUseTick(Level world, LivingEntity entity, ItemStack itemStack, int ticks)
	{
		if(ticks%2==0) entity.playSound(SoundEvents.PARROT_IMITATE_VEX, 0.7F, world.random.nextFloat()*0.7F+0.3F);
		if(!world.isClientSide()) ((ServerLevel)world).sendParticles(new StaffParticleOptions(this.particleColor), entity.getX(), entity.getY(), entity.getZ(), 1, 0D, 0D, 0D, 1D);
		//SoundEvents.EVOKER_PREPARE_SUMMON
	}
	
	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
	private static abstract class EventHandler
	{
		@SubscribeEvent
		public static void renderFPV(RenderHandEvent event)
		{
			Player player=Minecraft.getInstance().player;
			if(player.isUsingItem()&&player.getUseItem().getItem() instanceof StaffItem&&player.getUsedItemHand()!=event.getHand())
			{
				event.setCanceled(true);
			}
		}
	}
}