package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.enchantment.VeinMinerEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.PickaxeItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ENGEnchantments
{
	public static final DeferredRegister<Enchantment> ENCHANTMENTS=DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<Enchantment> VEIN_MINER=ENGEnchantments.ENCHANTMENTS.register(VeinMinerEnchantment.ID, VeinMinerEnchantment::new);

	public static abstract class Types
	{
		public static final EnchantmentType PICKAXE_ONLY=EnchantmentType.create("pickaxe", item->item instanceof PickaxeItem);
	}
}