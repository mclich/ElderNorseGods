package com.github.mclich.engmod.effect;

import java.util.concurrent.ThreadLocalRandom;
import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=ElderNorseGods.MOD_ID, bus=EventBusSubscriber.Bus.FORGE)
public class DrunkennessEffect extends Effect
{
	public static final String ID="drunkenness";
	
	private static final int DELAY=140;
	
	public DrunkennessEffect()
	{
		super(EffectType.HARMFUL, 0xf0c02c);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "1f8955f0-2985-4658-9dd8-0eb83fe32283", -0.05D, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.ATTACK_SPEED, "490e03c0-319b-4b22-b094-a98636fa6a4a", -0.05D, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "9855509b-0900-48de-a4b0-82eefaa24d62", 0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
	
	public static EffectInstance getInstance()
	{
		//new EffectInstance(Effects.DIG_SLOWDOWN, 1200, 3, true, false, false)
		return new EffectInstance(ENGEffects.DRUNKENNESS.get(), 1260, 0, true, true);
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return duration%DrunkennessEffect.DELAY==0;
	}
	
	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier)
	{
		entity.addEffect(new EffectInstance(Effects.CONFUSION, DrunkennessEffect.DELAY, 0, true, false, false));
	}
	
	@SubscribeEvent
	public static void randomWalking(InputUpdateEvent event)
	{
		PlayerEntity player=event.getPlayer();
		ThreadLocalRandom random=ThreadLocalRandom.current();
		if(player.getCommandSenderWorld().isClientSide()&&player.hasEffect(ENGEffects.DRUNKENNESS.get())&&random.nextInt(0, 6)<1)
	    {
			int impulse=250;
			MovementInput moves=event.getMovementInput();
			if(moves.up||moves.down) moves.leftImpulse+=random.nextInt(-impulse, impulse+1);
			if(moves.left||moves.right) moves.forwardImpulse+=random.nextInt(-impulse, impulse+1);
	    }
	}
}