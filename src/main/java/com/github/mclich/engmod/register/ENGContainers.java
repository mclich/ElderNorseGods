package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.block.BreweryBlock;
import com.github.mclich.engmod.block.container.BreweryContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ENGContainers
{
	public static final DeferredRegister<MenuType<?>> CONTAINERS=DeferredRegister.create(ForgeRegistries.CONTAINERS, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<MenuType<BreweryContainer>> BREWERY_CONTAINER=ENGContainers.CONTAINERS.register(BreweryBlock.ID, ()->IForgeMenuType.create(BreweryContainer::new));
}