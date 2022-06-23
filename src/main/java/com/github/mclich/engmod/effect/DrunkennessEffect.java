package com.github.mclich.engmod.effect;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class DrunkennessEffect extends MobEffect
{	
	public static final String ID="drunkenness";
	
	public DrunkennessEffect()
	{
		super(MobEffectCategory.HARMFUL, 0xF0C02C);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "1f8955f0-2985-4658-9dd8-0eb83fe32283", -0.05D, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.ATTACK_SPEED, "490e03c0-319b-4b22-b094-a98636fa6a4a", -0.05D, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "9855509b-0900-48de-a4b0-82eefaa24d62", 0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
	
	public static MobEffectInstance getInstance()
	{
		return new MobEffectInstance(ENGMobEffects.DRUNKENNESS.get(), 1260, 0, true, true);
	}
	
	@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=Bus.FORGE)
	private static abstract class EventHandler
	{
		//fixme: fix camera roll
		@SubscribeEvent
		public static void renderFPV(EntityViewRenderEvent.CameraSetup event)
		{
			Minecraft mc=Minecraft.getInstance();
			if(mc.player.hasEffect(ENGMobEffects.DRUNKENNESS.get()))
			{
				event.setRoll(5*(float)Math.cos(event.getPartialTick()));
			}
		}
		
		@SubscribeEvent
		public static void randomWalking(MovementInputUpdateEvent event)
		{
			Player player=event.getPlayer();
			RandomSource random=player.getLevel().getRandom();
			if(player.getLevel().isClientSide()&&player.hasEffect(ENGMobEffects.DRUNKENNESS.get())&&Mth.nextInt(random, 0, 6)<1)
		    {
				int impulse=250;
				Input moves=event.getInput();
				if(moves.up||moves.down) moves.leftImpulse+=Mth.nextInt(random, -impulse, impulse+1);
				if(moves.left||moves.right) moves.forwardImpulse+=Mth.nextInt(random, -impulse, impulse+1);
		    }
		}
	}
}