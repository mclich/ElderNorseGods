package com.github.mclich.engmod.enchantment;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGEnchantments;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuickSwingEnchantment extends Enchantment
{
    public static final String ID="quick_swing";

    public QuickSwingEnchantment()
    {
        super(Rarity.UNCOMMON, ENGEnchantments.Types.DAMAGING, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMaxLevel()
    {
        return 3;
    }

    @Override
    public int getMinCost(int level)
    {
        return 5+(level-1)*8;
    }

    @Override
    public int getMaxCost(int level)
    {
        return this.getMinCost(level)+20;
    }

    @Mod.EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
    private static abstract class EventHandler
    {
        private static final UUID QS_ATTACK_SPEED=UUID.fromString("ce01406b-c986-4f3a-ae51-681bb13a6b6a");
        private static final String[] MODIFIER_KEYS=new String[]{"attribute.modifier.equals.", "attribute.modifier.plus.", "attribute.modifier.take."};

        private static boolean isModifier(ITextComponent component)
        {
            return component instanceof TranslationTextComponent&&Stream.of(EventHandler.MODIFIER_KEYS).anyMatch(((TranslationTextComponent)component).getKey()::contains);
        }

        @SubscribeEvent
        public static void modifySwing(ItemAttributeModifierEvent event)
        {
            if(event.getSlotType()!=EquipmentSlotType.MAINHAND) return;
            int eLevel=EnchantmentHelper.getItemEnchantmentLevel(ENGEnchantments.QUICK_SWING.get(), event.getItemStack());
            if(eLevel>0) event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(EventHandler.QS_ATTACK_SPEED, "Quick Swing modifier", 0.3*Math.pow(2, eLevel), AttributeModifier.Operation.ADDITION));
        }

        @SubscribeEvent
        public static void setTooltip(ItemTooltipEvent event)
        {
            ItemStack tool=event.getItemStack();
            if(EnchantmentHelper.getItemEnchantmentLevel(ENGEnchantments.QUICK_SWING.get(), tool)>0)
            {
                List<ITextComponent> lines=event.getToolTip();
                List<ITextComponent> tModifiers=lines.stream().filter(c->EventHandler.isModifier(c)||c.getSiblings().stream().anyMatch(EventHandler::isModifier)).collect(Collectors.toList());
                tModifiers.stream().mapToInt(lines::indexOf).min().ifPresent
                (
                    startIndex->
                    {
                        int size=tModifiers.size();
                        PlayerEntity player=event.getPlayer();
                        Multimap<Attribute, AttributeModifier> modifiers=tool.getAttributeModifiers(EquipmentSlotType.MAINHAND);
                        double damageModifier=player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE),
                               damageSum=modifiers.get(Attributes.ATTACK_DAMAGE).stream().mapToDouble(AttributeModifier::getAmount).sum(),
                               speedModifier=player.getAttributeBaseValue(Attributes.ATTACK_SPEED),
                               speedSum=modifiers.get(Attributes.ATTACK_SPEED).stream().mapToDouble(AttributeModifier::getAmount).sum();
                        ITextComponent damage=new StringTextComponent(" ").append(new TranslationTextComponent("attribute.modifier.equals.0", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(damageModifier+damageSum), new TranslationTextComponent(Attributes.ATTACK_DAMAGE.getDescriptionId()))).withStyle(TextFormatting.DARK_GREEN);
                        ITextComponent speed=new StringTextComponent(" ").append(new TranslationTextComponent("attribute.modifier.equals.0", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(speedModifier+speedSum), new TranslationTextComponent(Attributes.ATTACK_SPEED.getDescriptionId()))).withStyle(TextFormatting.DARK_GREEN);
                        for(int i=0; i<size-2; i++) lines.remove(startIndex);
                        lines.set(startIndex, damage);
                        if(size>=2) lines.set(startIndex+1, speed);
                        else lines.add(startIndex+1, speed);
                    }
                );
            }
        }
    }
}