package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.TierSortingRegistry;
import java.util.List;

public abstract class ENGItemTiers
{
    public static final Tier CUSTOM_SWORD_TIER=TierSortingRegistry.registerTier(new SimpleTier(Tiers.DIAMOND.getLevel(), 3000, 10F, 4F, 16, Ingredient.of(Items.DIAMOND)), new ResourceLocation(ElderNorseGods.MOD_ID, "custom_sword_tier"), List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE));
    public static final StaffTier NOVICE_STAFF_TIER=(StaffTier)TierSortingRegistry.registerTier(new StaffTier(0, 200, 5, 0xFFFFFF, Rarity.COMMON, Ingredient.of()), new ResourceLocation(ElderNorseGods.MOD_ID, "novice_staff_tier"), List.of(), List.of());

    public static class StaffTier implements Tier
    {
        private final int level;
        private final int uses;
        private final int enchantmentValue;
        private final int color;
        private final Rarity rarity;
        private final Ingredient repairIngredient;

        public StaffTier(int level, int uses, int enchantmentValue, int color, Rarity rarity, Ingredient repairIngredient)
        {
            this.level=level;
            this.uses=uses;
            this.enchantmentValue=enchantmentValue;
            this.color=color;
            this.rarity=rarity;
            this.repairIngredient=repairIngredient;
        }

        @Override
        public int getLevel()
        {
            return this.level;
        }

        @Override
        public int getUses()
        {
            return this.uses;
        }

        @Override
        public float getSpeed()
        {
            return 1F;
        }

        @Override
        public float getAttackDamageBonus()
        {
            return 0;
        }

        @Override
        public int getEnchantmentValue()
        {
            return this.enchantmentValue;
        }

        public int getColor()
        {
            return this.color;
        }

        public Rarity getRarity()
        {
            return this.rarity;
        }

        @Override
        public Ingredient getRepairIngredient()
        {
            return this.repairIngredient;
        }
    }

    //todo: remove in future
    @Deprecated(forRemoval=true, since="1.0")
    public static class SimpleTier implements Tier
    {
        private final int level;
        private final int uses;
        private final float speed;
        private final float damage;
        private final int enchantmentValue;
        private final Ingredient repairIngredient;

        public SimpleTier(int level, int uses, float speed, float damage, int enchantmentValue, Ingredient repairIngredient)
        {
            this.level=level;
            this.uses=uses;
            this.speed=speed;
            this.damage=damage;
            this.enchantmentValue=enchantmentValue;
            this.repairIngredient=repairIngredient;
        }

        @Override
        public int getLevel()
        {
            return this.level;
        }

        @Override
        public int getUses()
        {
            return this.uses;
        }

        @Override
        public float getSpeed()
        {
            return this.speed;
        }

        @Override
        public float getAttackDamageBonus()
        {
            return this.damage;
        }

        @Override
        public int getEnchantmentValue()
        {
            return this.enchantmentValue;
        }

        @Override
        public Ingredient getRepairIngredient()
        {
            return this.repairIngredient;
        }
    }
}