package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.enchantment.QuickSwingEnchantment;
import com.github.mclich.engmod.enchantment.VeinMinerEnchantment;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ENGEnchantments
{
	public static final DeferredRegister<Enchantment> ENCHANTMENTS=DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<Enchantment> VEIN_MINER=ENGEnchantments.ENCHANTMENTS.register(VeinMinerEnchantment.ID, VeinMinerEnchantment::new);
	public static final RegistryObject<Enchantment> QUICK_SWING=ENGEnchantments.ENCHANTMENTS.register(QuickSwingEnchantment.ID, QuickSwingEnchantment::new);

	public static abstract class Types
	{
		public static final EnchantmentCategory PICKAXE_ONLY=EnchantmentCategory.create("pickaxe", item->item instanceof PickaxeItem);
		public static final EnchantmentCategory DAMAGING=EnchantmentCategory.create("damaging", item->item instanceof SwordItem||item instanceof AxeItem||item instanceof TridentItem);
	}
}