package com.github.mclich.engmod.effect;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGEffects;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper.UnableToAccessFieldException;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class LossEffect extends InstantenousMobEffect
{
    public static final String ID="instant_loss";
    public static final String POTION_ID="losing";

    public LossEffect()
    {
        super(MobEffectCategory.HARMFUL, 0xBABABA);
    }

    public static MobEffectInstance getInstance()
    {
        return new MobEffectInstance(ENGEffects.LOSS.get(), 1);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier)
    {
        this.applyInstantenousEffect(null, null, entity, amplifier, 0D);
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity potion, @Nullable Entity thrower, LivingEntity entity, int amplifier, double health)
    {
        ThreadLocalRandom random=ThreadLocalRandom.current();
        if(entity instanceof Player player)                          //probably bagged!!!
        {
            Inventory inventory=player.getInventory();
            List<ItemStack> allItems=Stream.of(inventory.items, inventory.armor, inventory.offhand).flatMap(Collection::stream).collect(Collectors.toList());
            List<ItemStack> nonEmptyItems=MethodHandler.obtainNonEmptyItems(allItems);
            if(!nonEmptyItems.isEmpty())
            {
                ItemStack randomItem=nonEmptyItems.get(random.nextInt(nonEmptyItems.size()));
                inventory.setItem(allItems.indexOf(randomItem), ItemStack.EMPTY);
                MethodHandler.dropItemAt(randomItem, player);
            }
        }
        else if(entity instanceof Fox fox)
        {
            ItemStack mouthItem=fox.getItemBySlot(EquipmentSlot.MAINHAND);
            if(!mouthItem.isEmpty())
            {
                fox.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                MethodHandler.dropItemAt(mouthItem, fox);
            }
        }
        else if(entity instanceof Pig||entity instanceof Strider)
        {
            if(((Saddleable)entity).isSaddled())
            {
                if(entity instanceof Pig) MethodHandler.unequipSaddle(Pig.class, (Pig)entity);
                else MethodHandler.unequipSaddle(Strider.class, (Strider)entity);
                MethodHandler.dropItemAt(new ItemStack(Items.SADDLE), entity);
                entity.ejectPassengers();
            }
        }
        else if(entity instanceof AbstractHorse horse)
        {
            SimpleContainer inventory=MethodHandler.obtainInv(AbstractHorse.class, horse);
            List<ItemStack> items=MethodHandler.obtainInvItems(inventory);
            List<ItemStack> nonEmptyItems=MethodHandler.obtainNonEmptyItems(items);
            if(!nonEmptyItems.isEmpty())
            {
                ItemStack randomItem=nonEmptyItems.get(random.nextInt(nonEmptyItems.size()));
                inventory.setItem(items.indexOf(randomItem), ItemStack.EMPTY);
                MethodHandler.dropItemAt(randomItem, horse);
                if(randomItem.getItem()==Items.SADDLE) horse.ejectPassengers();
            }
        }
        else if(entity instanceof Villager villager)
        {
            SimpleContainer inventory=villager.getInventory();
            List<ItemStack> items=MethodHandler.obtainInvItems(inventory);
            List<ItemStack> nonEmptyItems=MethodHandler.obtainNonEmptyItems(items);
            if(!nonEmptyItems.isEmpty())
            {
                ItemStack randomItem=nonEmptyItems.get(random.nextInt(nonEmptyItems.size()));
                inventory.setItem(items.indexOf(randomItem), ItemStack.EMPTY);
                MethodHandler.dropItemAt(randomItem, villager);
            }
            MethodHandler.performTargeting(villager, thrower, true);
            if(thrower instanceof LivingEntity) MethodHandler.makeVillagerRunAway(villager, (LivingEntity)thrower);
        }
        else if(entity instanceof EnderMan enderman)
        {
            BlockState carriedBlock=enderman.getCarriedBlock();
            if(carriedBlock!=null)
            {
                enderman.setCarriedBlock(null);
                MethodHandler.dropItemAt(new ItemStack(carriedBlock.getBlock().asItem()), enderman);
            }
            enderman.hurt(DamageSource.indirectMagic(potion, thrower), 0F);
            MethodHandler.performTargeting(enderman, thrower, false);
        }
        else if(entity instanceof Piglin piglin)
        {
            SimpleContainer inventory=MethodHandler.obtainInv(Piglin.class, piglin);
            List<ItemStack> invItems=MethodHandler.obtainInvItems(inventory);
            List<ItemStack> slotItems=MethodHandler.obtainSlotItems(piglin);
            List<ItemStack> notEmptyItems=MethodHandler.obtainNonEmptyItems(Stream.concat(invItems.stream(), slotItems.stream()));
            if(!notEmptyItems.isEmpty())
            {
                ItemStack randomItem=notEmptyItems.get(random.nextInt(notEmptyItems.size()));
                if(invItems.contains(randomItem)) inventory.setItem(invItems.indexOf(randomItem), ItemStack.EMPTY);
                else MethodHandler.findItemAndSetEmpty(randomItem, piglin);
                MethodHandler.dropItemAt(randomItem, piglin);
            }
            if(thrower instanceof Player) PiglinAi.angerNearbyPiglins((Player)thrower, true);
        }
        else if(MethodHandler.isCorrectMonster(entity))
        {
            Monster monster=(Monster)entity;
            List<ItemStack> items=MethodHandler.obtainNonEmptyItems(MethodHandler.obtainSlotItems(monster));
            if(!items.isEmpty())
            {
                ItemStack randomItem=items.get(random.nextInt(items.size()));
                MethodHandler.findItemAndSetEmpty(randomItem, monster);
                MethodHandler.dropItemAt(randomItem, monster);
                if(monster instanceof Pillager) ((Pillager)monster).setChargingCrossbow(false);
            }
            if(monster instanceof ZombifiedPiglin)
            {
                MethodHandler.performTargeting(monster, thrower, false);
                MethodHandler.alertOthersZombiePiglins((ZombifiedPiglin)monster);
            }
        }
    }

    private static abstract class MethodHandler
    {
        private static final int PICKUP_DELAY=40;

        private static boolean isCorrectMonster(LivingEntity entity)
        {
            return entity instanceof Pillager||entity instanceof Vindicator||entity instanceof PiglinBrute||entity instanceof Zombie||entity instanceof AbstractSkeleton;
        }

        @Nullable
        private static <E extends LivingEntity> SimpleContainer obtainInv(Class<? super E> classToAccess, E instance)
        {
            try
            {
                return ObfuscationReflectionHelper.getPrivateValue(classToAccess, instance, "inventory");
            }
            catch(UnableToAccessFieldException exc)
            {
                ElderNorseGods.LOGGER.error("No inventory found in provided living entity", exc);
                return null;
            }
        }

        @Nullable
        private static List<ItemStack> obtainInvItems(SimpleContainer inventory)
        {
            try
            {
                return ObfuscationReflectionHelper.getPrivateValue(SimpleContainer.class, inventory, "items");
            }
            catch(UnableToAccessFieldException exc)
            {
                ElderNorseGods.LOGGER.error("No items found in provided inventory", exc);
                return null;
            }
        }

        private static List<ItemStack> obtainSlotItems(Entity entity)
        {
            return StreamSupport.stream(entity.getAllSlots().spliterator(), false).collect(Collectors.toList());
        }

        private static List<ItemStack> obtainNonEmptyItems(List<ItemStack> nullableItems)
        {
            return nullableItems.stream().filter(i->!i.isEmpty()).collect(Collectors.toList());
        }

        private static List<ItemStack> obtainNonEmptyItems(Stream<ItemStack> nullableItemStream)
        {
            return nullableItemStream.filter(i->!i.isEmpty()).collect(Collectors.toList());
        }

        private static void findItemAndSetEmpty(ItemStack item, LivingEntity entity)
        {
            Stream.of(EquipmentSlot.values()).filter(t->item==entity.getItemBySlot(t)).findFirst().ifPresent(t->entity.setItemSlot(t, ItemStack.EMPTY));
        }

        private static <E extends Saddleable> void unequipSaddle(Class<E> classToAccess, E entity)
        {
            try
            {
                ObfuscationReflectionHelper.<ItemBasedSteering, E>getPrivateValue(classToAccess, entity, "steering").setSaddle(false);
            }
            catch(UnableToAccessFieldException exc)
            {
                ElderNorseGods.LOGGER.error("No field 'steering' found in provided entity", exc);
            }
        }

        private static void dropItemAt(ItemStack item, Entity entity)
        {
            entity.spawnAtLocation(item).setPickUpDelay(MethodHandler.PICKUP_DELAY);
        }

        private static void makeVillagerRunAway(Villager villager, LivingEntity avoidEntity)
        {
            villager.getBrain().setActiveActivityIfPossible(Activity.PANIC);
            villager.getBrain().setMemory(MemoryModuleType.HURT_BY_ENTITY, avoidEntity);
        }

        private static void alertOthersZombiePiglins(ZombifiedPiglin zombiePiglin)
        {
            try
            {
                ObfuscationReflectionHelper.findMethod(ZombifiedPiglin.class, "alertOthers").invoke(zombiePiglin);
            }
            catch(ReflectiveOperationException exc)
            {
                ElderNorseGods.LOGGER.error("No method 'alertOthers' found in provided entity", exc);
            }
        }

        private static void performTargeting(Mob performer, Entity target, boolean forVillager)
        {
            if(target instanceof Player playerTarget)
            {
                if(forVillager)
                {
                    performer.setLastHurtByMob(playerTarget);
                    performer.setLastHurtByPlayer(playerTarget);
                }
                else if(!playerTarget.getAbilities().instabuild) performer.setTarget(playerTarget);
            }
            else if(target instanceof LivingEntity livingTarget)
            {
                if(forVillager) performer.setLastHurtByMob(livingTarget);
                else performer.setTarget(livingTarget);
            }
        }
    }
}