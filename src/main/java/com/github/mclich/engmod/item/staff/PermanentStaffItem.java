package com.github.mclich.engmod.item.staff;

import java.util.List;
import com.github.mclich.engmod.data.capability.ManaCapability;
import com.github.mclich.engmod.data.handler.IManaHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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

public abstract class PermanentStaffItem extends StaffItem
{
	protected final int manaPerTicks;
	protected final int effectPerTicks;
	
	public PermanentStaffItem(Rarity rarity, int durability, int manaPerTicks, int effectPerTicks)
	{
		super(rarity, durability);
		this.manaPerTicks=manaPerTicks;
		this.effectPerTicks=effectPerTicks;
	}
	
	@Override
	public int getUseDuration(ItemStack itemStack)
	{
		return 72000;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack itemStack, World world, List<ITextComponent> textField, ITooltipFlag advanced)
	{
		if(itemStack.isEnchanted())
		{
			itemStack.hideTooltipPart(TooltipDisplayFlags.ENCHANTMENTS);
			ItemStack.appendEnchantmentNames(textField, itemStack.getEnchantmentTags());
		}
		textField.add(StringTextComponent.EMPTY);
		textField.add(new TranslationTextComponent("staff.engmod.used").withStyle(TextFormatting.GRAY));
		textField.add(new TranslationTextComponent("staff.engmod.perm_mana", StaffItem.toRoundedString(20F/this.manaPerTicks)).withStyle(TextFormatting.DARK_GREEN));
		textField.add(new TranslationTextComponent("staff.engmod.perm_effect_heal", StaffItem.toRoundedString(20F/this.effectPerTicks)).withStyle(TextFormatting.DARK_GREEN));
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
			if(mana.getMana()>0F)
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
		if(entity instanceof PlayerEntity)
		{
			PlayerEntity player=(PlayerEntity)entity;
			int power=this.getUseDuration(itemStack)-ticks;
			IManaHandler mana=player.getCapability(ManaCapability.CAP_INSTANCE).orElse(null);
			if(mana!=null&&mana.getMana()>0F&&!player.abilities.instabuild)
			{
				if(power==0) power=1;
				if(power%this.manaPerTicks==0) mana.setMana(mana.getMana()>1F?mana.getMana()-1F:0F);
				if(power%this.effectPerTicks==0) this.applyEffect(world, player, itemStack);
				if(power%(this.effectPerTicks*3)==0) itemStack.hurtAndBreak(1, player, e->e.broadcastBreakEvent(player.getUsedItemHand()));
			}
		}
	}
}