package com.github.mclich.engmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class CustomBlock extends Block
{
	public static final String ID="custom_block";
	public CustomBlock()
	{
		super
		(
			Block.Properties.of(Material.STONE)
			.strength(5.0F, 6.0F)
			.harvestLevel(2)
			.harvestTool(ToolType.PICKAXE)
			.requiresCorrectToolForDrops()
			.sound(SoundType.NETHERITE_BLOCK)
		);
	}
}