package com.github.mclich.engmod.effect;

import com.github.mclich.engmod.ElderNorseGods;
import com.github.mclich.engmod.register.ENGEffects;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.InstantEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper.UnableToAccessFieldException;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class LossEffect extends InstantEffect
{
    public static final String ID="instant_loss";
    public static final String POTION_ID="losing";

    public LossEffect()
    {
        super(EffectType.HARMFUL, 0xBABABA);
    }

    public static EffectInstance getInstance()
    {
        return new EffectInstance(ENGEffects.LOSS.get(), 1);
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
        if(entity instanceof PlayerEntity)                          //probably bagged!!!
        {
            PlayerEntity player=(PlayerEntity)entity;
            PlayerInventory inventory=player.inventory;
            List<ItemStack> allItems=Stream.of(inventory.items, inventory.armor, inventory.offhand).flatMap(Collection::stream).collect(Collectors.toList());
            List<ItemStack> nonEmptyItems=MethodHandler.obtainNonEmptyItems(allItems);
            if(!nonEmptyItems.isEmpty())
            {
                ItemStack randomItem=nonEmptyItems.get(random.nextInt(nonEmptyItems.size()));
                inventory.setItem(allItems.indexOf(randomItem), ItemStack.EMPTY);
                MethodHandler.dropItemAt(randomItem, player);
            }
        }
        else if(entity instanceof FoxEntity)
        {
            FoxEntity fox=(FoxEntity)entity;
            ItemStack mouthItem=fox.getItemBySlot(EquipmentSlotType.MAINHAND);
            if(!mouthItem.isEmpty())
            {
                fox.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                MethodHandler.dropItemAt(mouthItem, fox);
            }
        }
        else if(entity instanceof PigEntity||entity instanceof StriderEntity)
        {
            if(((IEquipable)entity).isSaddled())
            {
                if(entity instanceof PigEntity) MethodHandler.unequipSaddle(PigEntity.class, (PigEntity)entity);
                else MethodHandler.unequipSaddle(StriderEntity.class, (StriderEntity)entity);
                MethodHandler.dropItemAt(new ItemStack(Items.SADDLE), entity);
                entity.ejectPassengers();
            }
        }
        else if(entity instanceof AbstractHorseEntity)
        {
            AbstractHorseEntity horse=(AbstractHorseEntity)entity;
            Inventory inventory=MethodHandler.obtainInv(AbstractHorseEntity.class, horse);
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
        else if(entity instanceof VillagerEntity)
        {
            VillagerEntity villager=(VillagerEntity)entity;
            Inventory inventory=villager.getInventory();
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
        else if(entity instanceof EndermanEntity)
        {
            EndermanEntity enderman=(EndermanEntity)entity;
            BlockState carriedBlock=enderman.getCarriedBlock();
            if(carriedBlock!=null)
            {
                enderman.setCarriedBlock(null);
                MethodHandler.dropItemAt(new ItemStack(carriedBlock.getBlock().asItem()), enderman);
            }
            enderman.hurt(DamageSource.indirectMagic(potion, thrower), 0F);
            MethodHandler.performTargeting(enderman, thrower, false);
        }
        else if(entity instanceof PiglinEntity)
        {
            PiglinEntity piglin=(PiglinEntity)entity;
            Inventory inventory=MethodHandler.obtainInv(PiglinEntity.class, piglin);
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
            if(thrower instanceof PlayerEntity) PiglinTasks.angerNearbyPiglins((PlayerEntity)thrower, true);
        }
        else if(MethodHandler.isCorrectMonster(entity))
        {
            MonsterEntity monster=(MonsterEntity)entity;
            List<ItemStack> items=MethodHandler.obtainNonEmptyItems(MethodHandler.obtainSlotItems(monster));
            if(!items.isEmpty())
            {
                ItemStack randomItem=items.get(random.nextInt(items.size()));
                MethodHandler.findItemAndSetEmpty(randomItem, monster);
                MethodHandler.dropItemAt(randomItem, monster);
                if(monster instanceof PillagerEntity) ((PillagerEntity)monster).setChargingCrossbow(false);
            }
            if(monster instanceof ZombifiedPiglinEntity)
            {
                MethodHandler.performTargeting(monster, thrower, false);
                MethodHandler.alertOthersZombiePiglins((ZombifiedPiglinEntity)monster);
            }
        }
    }

    private static abstract class MethodHandler
    {
        private static final int PICKUP_DELAY=40;

        private static boolean isCorrectMonster(LivingEntity entity)
        {
            return entity instanceof PillagerEntity||entity instanceof VindicatorEntity||entity instanceof PiglinBruteEntity||entity instanceof ZombieEntity||entity instanceof AbstractSkeletonEntity;
        }

        @Nullable
        private static <E extends LivingEntity> Inventory obtainInv(Class<? super E> classToAccess, E instance)
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
        private static List<ItemStack> obtainInvItems(Inventory inventory)
        {
            try
            {
                return ObfuscationReflectionHelper.getPrivateValue(Inventory.class, inventory, "items");
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
            Stream.of(EquipmentSlotType.values()).filter(t->item==entity.getItemBySlot(t)).findFirst().ifPresent(t->entity.setItemSlot(t, ItemStack.EMPTY));
        }

        private static <E extends IEquipable> void unequipSaddle(Class<E> classToAccess, E entity)
        {
            try
            {
                ObfuscationReflectionHelper.<BoostHelper, E>getPrivateValue(classToAccess, entity, "steering").setSaddle(false);
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

        private static void makeVillagerRunAway(VillagerEntity villager, LivingEntity avoidEntity)
        {
            villager.getBrain().setActiveActivityIfPossible(Activity.PANIC);
            villager.getBrain().setMemory(MemoryModuleType.HURT_BY_ENTITY, avoidEntity);
        }

        private static void alertOthersZombiePiglins(ZombifiedPiglinEntity zombiePiglin)
        {
            try
            {
                ObfuscationReflectionHelper.findMethod(ZombifiedPiglinEntity.class, "alertOthers").invoke(zombiePiglin);
            }
            catch(ReflectiveOperationException exc)
            {
                ElderNorseGods.LOGGER.error("No method 'alertOthers' found in provided entity", exc);
            }
        }

        private static void performTargeting(MobEntity performer, Entity target, boolean forVillager)
        {
            if(target instanceof PlayerEntity)
            {
                PlayerEntity playerTarget=(PlayerEntity)target;
                if(forVillager)
                {
                    performer.setLastHurtByMob(playerTarget);
                    performer.setLastHurtByPlayer(playerTarget);
                }
                else if(!playerTarget.abilities.instabuild) performer.setTarget(playerTarget);
            }
            else if(target instanceof LivingEntity)
            {
                LivingEntity livingTarget=(LivingEntity)target;
                if(forVillager) performer.setLastHurtByMob(livingTarget);
                else performer.setTarget(livingTarget);
            }
        }
    }
}