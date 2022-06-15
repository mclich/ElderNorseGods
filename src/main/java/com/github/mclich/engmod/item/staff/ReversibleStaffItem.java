package com.github.mclich.engmod.item.staff;

import com.github.mclich.engmod.data.capability.IManaStorage;
import com.github.mclich.engmod.register.ENGCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStack.TooltipPart;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class ReversibleStaffItem extends StaffItem
{
	private float consumedMana;
	
	protected final int manaUseDelay;
	protected final float manaToConsume;
	protected final float effectToApply;
	protected final float cooldown;
	
	public ReversibleStaffItem(Rarity rarity, int particleColor, int durability, int manaUseDelay, float manaToConsume, float effectToApply, float cooldown)
	{
		super(rarity, particleColor, durability);
		this.consumedMana=0F;
		this.manaUseDelay=manaUseDelay;
		this.manaToConsume=manaToConsume;
		this.effectToApply=effectToApply;
		this.cooldown=cooldown;
	}
	
	@Override
	public int getUseDuration(ItemStack itemStack)
	{
		return ((int)this.manaToConsume+1)*this.manaUseDelay;
	}
	
	@Override
	public void appendHoverText(ItemStack itemStack, Level world, List<Component> textField, TooltipFlag isAdvanced)
	{
		if(itemStack.isEnchanted())
		{
			itemStack.hideTooltipPart(TooltipPart.ENCHANTMENTS);
			ItemStack.appendEnchantmentNames(textField, itemStack.getEnchantmentTags());
			textField.add(TextComponent.EMPTY);
		}
		textField.add(new TranslatableComponent("staff.engmod.cooldown", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(this.cooldown)).withStyle(ChatFormatting.RED));
		textField.add(TextComponent.EMPTY);
		textField.add(new TranslatableComponent("staff.engmod.used").withStyle(ChatFormatting.GRAY));
		textField.add(new TranslatableComponent("staff.engmod.rev_mana", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(this.manaToConsume)).withStyle(ChatFormatting.DARK_GREEN));
		textField.add(new TranslatableComponent("staff.engmod.rev_effect_regen", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(this.effectToApply)).withStyle(ChatFormatting.DARK_GREEN));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack handItem=player.getItemInHand(hand);
		if(StaffItem.hasPlayerShieldEquipped(player, hand)) return InteractionResultHolder.fail(handItem);
		IManaStorage mana=player.getCapability(ENGCapabilities.MANA).orElse(null);
		if(player.getAbilities().instabuild)
		{
			player.startUsingItem(hand);
			return InteractionResultHolder.consume(handItem);
		}
		else if(mana!=null&&mana.getStatus())
		{
			if(mana.getMana()>=this.manaToConsume)
			{
				player.startUsingItem(hand);
				return InteractionResultHolder.consume(handItem);
			}
			else
			{
				player.displayClientMessage(new TranslatableComponent("message.engmod.no_mana_points"), true);
				return InteractionResultHolder.fail(handItem);
			}
		}
		else
		{
			player.displayClientMessage(new TranslatableComponent("message.engmod.no_mana_access"), true);
			return InteractionResultHolder.fail(handItem);
		}
	}
	
	@Override
	public void onUseTick(Level world, LivingEntity entity, ItemStack itemStack, int ticks)
	{
		super.onUseTick(world, entity, itemStack, ticks);
		if(!world.isClientSide()&&entity instanceof ServerPlayer player)
		{
			int power=this.getUseDuration(itemStack)-ticks;
			IManaStorage mana=player.getCapability(ENGCapabilities.MANA).orElse(null);
			if(mana!=null&&mana.getMana()>0F&&!player.getAbilities().instabuild)
			{
				if(power==0) power=1;
				if(power%this.manaUseDelay==0&&this.consumedMana<this.manaToConsume)
				{
					this.consumedMana++;
					mana.setAndUpdateMana(player, mana.getMana()>1F?mana.getMana()-1F:0F);
				}
			}
		}
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, Level world, LivingEntity entity)
	{
		if(entity instanceof Player player)
		{
			player.getCooldowns().addCooldown(this, Math.round(20*this.cooldown));
		}
		itemStack.hurtAndBreak(1, entity, e->e.broadcastBreakEvent(entity.getUsedItemHand()));
		this.applyEffect(world, entity, itemStack);
		this.consumedMana=0F;
		return itemStack;
	}

	@Override
	public void releaseUsing(ItemStack itemStack, Level world, LivingEntity entity, int timeCharged)
	{
		if(!world.isClientSide()&&entity instanceof ServerPlayer player)
		{
			player.getCapability(ENGCapabilities.MANA).ifPresent(mana->mana.setAndUpdateMana(player, mana.getMana()+this.consumedMana));
			this.consumedMana=0F;
		}
	}
}