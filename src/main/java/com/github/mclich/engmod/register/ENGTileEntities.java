package com.github.mclich.engmod.register;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.block.BreweryBlock;
import com.github.mclich.engmod.entity.tile.BreweryTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.tileentity.TileEntityType.Builder;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ENGTileEntities
{
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES=DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ElderNorseGods.MOD_ID);
	
	public static final RegistryObject<TileEntityType<BreweryTileEntity>> BREWERY_TILE=ENGTileEntities.TILE_ENTITIES.register(BreweryBlock.ID, ()->Builder.of(BreweryTileEntity::new, ENGBlocks.BREWERY.get()).build(null));
}
