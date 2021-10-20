package com.github.mclich.engmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.HayBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class BarleyHayBlock extends HayBlock
{
	public static final String ID="barley_hay_block";
	
	public BarleyHayBlock()
	{
		super(Block.Properties.of(Material.GRASS, MaterialColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.GRASS));
	}
}