package com.github.mclich.engmod.item.staff;

import java.util.List;
import com.github.mclich.engmod.data.capability.ManaCapability;
import com.github.mclich.engmod.data.handler.IManaHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack.TooltipDisplayFlags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class ReversibleStaffItem extends StaffItem
{
	private static final int USE_DELAY=5;
	
	private float consumedMana;
	
	protected final float manaToConsume;
	protected final float effectToApply;
	protected float cooldown;
	
	public ReversibleStaffItem(Rarity rarity, int durability, float manaToConsume, float effectToApply, float cooldown)
	{
		super(rarity, durability);
		this.consumedMana=0F;
		this.manaToConsume=manaToConsume;
		this.effectToApply=effectToApply;
		this.cooldown=cooldown;
	}
	
	@Override
	public int getUseDuration(ItemStack itemStack)
	{
		return ((int)this.manaToConsume+1)*ReversibleStaffItem.USE_DELAY;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack itemStack, World world, List<ITextComponent> textField, ITooltipFlag advanced)
	{
		if(itemStack.isEnchanted())
		{
			itemStack.hideTooltipPart(TooltipDisplayFlags.ENCHANTMENTS);
			ItemStack.appendEnchantmentNames(textField, itemStack.getEnchantmentTags());
			textField.add(StringTextComponent.EMPTY);
		}
		textField.add(new TranslationTextComponent("staff.engmod.cooldown", StaffItem.toRoundedString(this.cooldown)).withStyle(TextFormatting.RED));
		textField.add(StringTextComponent.EMPTY);
		textField.add(new TranslationTextComponent("staff.engmod.used").withStyle(TextFormatting.GRAY));
		textField.add(new TranslationTextComponent("staff.engmod.rev_mana", StaffItem.toRoundedString(this.manaToConsume)).withStyle(TextFormatting.DARK_GREEN));
		textField.add(new TranslationTextComponent("staff.engmod.rev_effect_regen", StaffItem.toRoundedString(this.effectToApply)).withStyle(TextFormatting.DARK_GREEN));
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack handItem=player.getItemInHand(hand);
		IManaHandler mana=player.getCapability(ManaCapability.CAP_INSTANCE).orElse(null);
		if(player.abilities.instabuild)
		{
			player.startUsingItem(hand);
			return ActionResult.consume(handItem);
		}
		else if(mana!=null&&mana.getStatus())
		{
			if(mana.getMana()>=this.manaToConsume)
			{
				player.startUsingItem(hand);
				return ActionResult.consume(handItem);
			}
			else
			{
				player.displayClientMessage(new TranslationTextComponent("message.engmod.no_mana_points"), true);
				return ActionResult.fail(handItem);
			}
		}
		else
		{
			player.displayClientMessage(new TranslationTextComponent("message.engmod.no_mana_access"), true);
			return ActionResult.fail(handItem);
		}
	}
	
	@Override
	public void onUseTick(World world, LivingEntity entity, ItemStack itemStack, int ticks)
	{
		if(!world.isClientSide()&&entity instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player=(ServerPlayerEntity)entity;
			int power=this.getUseDuration(itemStack)-ticks;
			IManaHandler mana=player.getCapability(ManaCapability.CAP_INSTANCE).orElse(null);
			if(mana!=null&&mana.getMana()>0F&&!player.abilities.instabuild)
			{
				if(power==0) power=1;
				if(power%ReversibleStaffItem.USE_DELAY==0&&this.consumedMana<this.manaToConsume)
				{
					this.consumedMana++;
					mana.setAndUpdateMana(player, mana.getMana()>1F?mana.getMana()-1F:0F);
				}
			}
		}
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, World world, LivingEntity entity)
	{
		if(entity instanceof PlayerEntity)
		{
			PlayerEntity player=(PlayerEntity)entity;
			player.getCooldowns().addCooldown(this, Math.round(20*this.cooldown));
			itemStack.hurtAndBreak(1, player, e->e.broadcastBreakEvent(player.getUsedItemHand()));
		}
		this.applyEffect(world, entity, itemStack);
		this.consumedMana=0F;
		return itemStack;
	}

	@Override
	public void releaseUsing(ItemStack itemStack, World world, LivingEntity entity, int timeCharged)
	{
		if(!world.isClientSide()&&entity instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player=(ServerPlayerEntity)entity;
			player.getCapability(ManaCapability.CAP_INSTANCE).ifPresent(mana->mana.setAndUpdateMana(player, mana.getMana()+this.consumedMana));
			this.consumedMana=0F;
		}
	}
}