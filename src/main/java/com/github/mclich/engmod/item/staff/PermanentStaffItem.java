package com.github.mclich.engmod.item.staff;

import com.github.mclich.engmod.data.capability.IManaStorage;
import com.github.mclich.engmod.register.ENGCapabilities;
import com.github.mclich.engmod.register.ENGItemTiers.StaffTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStack.TooltipPart;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import java.util.List;

public abstract class PermanentStaffItem extends StaffItem
{
	protected final int manaUseDelay;
	protected final int effectUseDelay;
	
	public PermanentStaffItem(StaffTier tier, int manaUseDelay, int effectUseDelay)
	{
		super(tier);
		this.manaUseDelay=manaUseDelay;
		this.effectUseDelay=effectUseDelay;
	}
	
	@Override
	public int getUseDuration(ItemStack itemStack)
	{
		return 72000;
	}
	
	@Override
	public void appendHoverText(ItemStack itemStack, Level world, List<Component> textField, TooltipFlag isAdvanced)
	{
		if(itemStack.isEnchanted())
		{
			itemStack.hideTooltipPart(TooltipPart.ENCHANTMENTS);
			ItemStack.appendEnchantmentNames(textField, itemStack.getEnchantmentTags());
		}
		textField.add(Component.empty());
		textField.add(Component.translatable("staff.engmod.used").withStyle(ChatFormatting.GRAY));
		textField.add(Component.translatable("staff.engmod.perm_mana", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(20F/this.manaUseDelay)).withStyle(ChatFormatting.DARK_GREEN));
		textField.add(Component.translatable("staff.engmod.perm_effect_heal", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(20F/this.effectUseDelay)).withStyle(ChatFormatting.DARK_GREEN));
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
			if(mana.getMana()>0F)
			{
				player.startUsingItem(hand);
				return InteractionResultHolder.consume(handItem);
			}
			else
			{
				player.displayClientMessage(Component.translatable("message.engmod.no_mana_points"), true);
				return InteractionResultHolder.fail(handItem);
			}
		}
		else
		{
			player.displayClientMessage(Component.translatable("message.engmod.no_mana_access"), true);
			return InteractionResultHolder.fail(handItem);
		}
	}
	
	@Override
	public void onUseTick(Level world, LivingEntity entity, ItemStack itemStack, int ticks)
	{
		super.onUseTick(world, entity, itemStack, ticks);
		if(entity instanceof Player player)
		{
			int power=this.getUseDuration(itemStack)-ticks;
			IManaStorage mana=player.getCapability(ENGCapabilities.MANA).orElse(null);
			if(mana!=null&&mana.getMana()>0F&&!player.getAbilities().instabuild)
			{
				if(power==0) power=1;
				if(power%this.manaUseDelay==0) mana.setMana(mana.getMana()>1F?mana.getMana()-1F:0F);
				if(power%this.effectUseDelay==0) this.applyEffect(world, player, itemStack);
				if(power%(this.effectUseDelay*3)==0) itemStack.hurtAndBreak(1, player, e->e.broadcastBreakEvent(player.getUsedItemHand()));
			}
		}
	}
}