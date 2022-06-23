package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.block.BreweryBlock;
import com.github.mclich.engmod.entity.block.BreweryBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ENGBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES=DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<BlockEntityType<BreweryBlockEntity>> BREWERY_ENTITY=ENGBlockEntities.BLOCK_ENTITIES.register(BreweryBlock.ID, ()->Builder.of(BreweryBlockEntity::new, ENGBlocks.BREWERY.get()).build(null));
}
