package com.github.mclich.engmod.item;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.util.ENGItems;
import com.github.mclich.engmod.util.ENGTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.FORGE)
public class TotemOfAbyssItem extends Item
{
	public static final String ID="totem_of_abyss";
	
	public TotemOfAbyssItem()
	{
		super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).tab(ENGTabs.COMBAT));
	}

	@SubscribeEvent
	public static void onVoidDamage(LivingDamageEvent event)
	{
		if(event.getEntityLiving() instanceof PlayerEntity)
		{
			Item totem=ENGItems.TOTEM_OF_ABYSS.get();
			PlayerEntity player=(PlayerEntity)event.getEntityLiving();
			if((player.getMainHandItem().getItem()==totem||player.getOffhandItem().getItem()==totem)&&event.getSource()==DamageSource.OUT_OF_WORLD)
			{
				event.setCanceled(true);
			}
		}
	}
	
	@Override
	public void inventoryTick(ItemStack itemStack, World world, Entity entity, int tick, boolean flag)
	{
		if(!world.isClientSide()&&entity instanceof ServerPlayerEntity&&entity.blockPosition().getY()<0&&((ServerPlayerEntity)entity).getLastDamageSource()==DamageSource.OUT_OF_WORLD)
		{
			ServerPlayerEntity player=(ServerPlayerEntity)entity;
			if(player.getMainHandItem().getItem()==this) this.activateTotem((ServerWorld)world, player, player.getMainHandItem());
			else if(player.getOffhandItem().getItem()==this) this.activateTotem((ServerWorld)world, player, player.getOffhandItem());
		}
	}
	
	private void activateTotem(ServerWorld world, final ServerPlayerEntity player, ItemStack itemStack)
	{
		player.fallDistance=0F;
		ItemStack totem=itemStack.copy();
		if(player.gameMode.isSurvival()) itemStack.shrink(1);
		if(world.dimension()==World.END) player.moveTo(100.5F, 49F, 0.5F);
		else player.getServer().getPlayerList().respawn(player, true);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->Minecraft.getInstance().gameRenderer.displayItemActivation(totem));
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->Minecraft.getInstance().particleEngine.createTrackingEmitter(player, ParticleTypes.PORTAL, 40));
		world.playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundCategory.PLAYERS, 2F, 1F);
		//player.playSound(SoundEvents.TOTEM_USE, 2F, 1F);
	}
}