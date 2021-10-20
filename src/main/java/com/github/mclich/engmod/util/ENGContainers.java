package com.github.mclich.engmod.util;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.block.BreweryBlock;
import com.github.mclich.engmod.block.container.BreweryContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ENGContainers
{
	public static final DeferredRegister<ContainerType<?>> CONTAINERS=DeferredRegister.create(ForgeRegistries.CONTAINERS, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<ContainerType<BreweryContainer>> BREWERY_CONTAINER=ENGContainers.CONTAINERS.register(BreweryBlock.ID, ()->IForgeContainerType.create(BreweryContainer::new));
}