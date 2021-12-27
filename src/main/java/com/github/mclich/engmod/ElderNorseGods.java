package com.github.mclich.engmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.mclich.engmod.register.*;

@Mod(ElderNorseGods.MOD_ID)
public class ElderNorseGods
{
	public static final String MOD_ID="engmod";
	public static final Logger LOGGER=LogManager.getLogger(ElderNorseGods.class);
	
	public ElderNorseGods()
	{
		IEventBus eventBus=FMLJavaModLoadingContext.get().getModEventBus();
		ENGEffects.EFFECTS.register(eventBus);
		ENGPotions.POTIONS.register(eventBus);
		ENGEnchantments.ENCHANTMENTS.register(eventBus);
		ENGItems.ITEMS.register(eventBus);
		ENGBlocks.BLOCKS.register(eventBus);
		ENGTileEntities.TILE_ENTITIES.register(eventBus);
		ENGContainers.CONTAINERS.register(eventBus);
		ENGSerializers.SERIALIZERS.register(eventBus);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	/*
	@SubscribeEvent
	public static void test(ItemTooltipEvent event)
	{
		if(event.getItemStack().getItem()==Items.HAY_BLOCK&&event.getFlags().isAdvanced())
		{
			StringTextComponent text=new StringTextComponent("minecraft:wheat_hay_block");
			text.setStyle(text.getStyle().withColor(TextFormatting.DARK_GRAY));
			List<ITextComponent> tips=event.getToolTip();
			tips.remove(tips.size()-1);
			tips.add(text);
		}
	} 
	*/
}