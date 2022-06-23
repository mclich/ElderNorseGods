package com.github.mclich.engmod.enchantment;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGEnchantments;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class QuickSwingEnchantment extends Enchantment
{
    public static final String ID="quick_swing";

    public QuickSwingEnchantment()
    {
        super(Rarity.UNCOMMON, ENGEnchantments.Types.DAMAGING, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
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

        private static boolean isModifier(ComponentContents contents)
        {
            return contents instanceof TranslatableContents transContents&&Stream.of(EventHandler.MODIFIER_KEYS).anyMatch(transContents.getKey()::contains);
        }

        @SubscribeEvent
        public static void modifySwing(ItemAttributeModifierEvent event)
        {
            if(event.getSlotType()!=EquipmentSlot.MAINHAND) return;
            int eLevel=event.getItemStack().getEnchantmentLevel(ENGEnchantments.QUICK_SWING.get());
            if(eLevel>0) event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(EventHandler.QS_ATTACK_SPEED, "Quick Swing modifier", 0.3*Math.pow(2, eLevel), AttributeModifier.Operation.ADDITION));
        }

        @SubscribeEvent
        public static void setTooltip(ItemTooltipEvent event)
        {
            ItemStack tool=event.getItemStack();
            if(tool.getEnchantmentLevel(ENGEnchantments.QUICK_SWING.get())>0)
            {
                List<Component> lines=event.getToolTip();
                List<Component> tModifiers=lines.stream().filter(c->EventHandler.isModifier(c.getContents())||c.getSiblings().stream().anyMatch(ic->EventHandler.isModifier(ic.getContents()))).toList();
                tModifiers.stream().mapToInt(lines::indexOf).min().ifPresent
                (
                    startIndex->
                    {
                        int size=tModifiers.size();
                        Player player=event.getPlayer();
                        Multimap<Attribute, AttributeModifier> modifiers=tool.getAttributeModifiers(EquipmentSlot.MAINHAND);
                        double damageModifier=player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE),
                               damageSum=modifiers.get(Attributes.ATTACK_DAMAGE).stream().mapToDouble(AttributeModifier::getAmount).sum(),
                               speedModifier=player.getAttributeBaseValue(Attributes.ATTACK_SPEED),
                               speedSum=modifiers.get(Attributes.ATTACK_SPEED).stream().mapToDouble(AttributeModifier::getAmount).sum();
                        Component damage=Component.literal(" ").append(Component.translatable("attribute.modifier.equals.0", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(damageModifier+damageSum), Component.translatable(Attributes.ATTACK_DAMAGE.getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN);
                        Component speed=Component.literal(" ").append(Component.translatable("attribute.modifier.equals.0", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(speedModifier+speedSum), Component.translatable(Attributes.ATTACK_SPEED.getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN);
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