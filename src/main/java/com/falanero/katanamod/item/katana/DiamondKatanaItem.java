package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.callback.OnKilledByCallback;
import com.falanero.katanamod.callback.PlayerEntityTickCallback;
import com.falanero.katanamod.callback.ToolBreakCallback;
import com.falanero.katanamod.registry.RegistryRecords;
import com.falanero.katanamod.util.Nbt;
import com.falanero.katanamod.util.Souls;
import com.falanero.katanamod.util.Tooltip;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;

public class DiamondKatanaItem extends KatanaItem {
    static final int SPEED_MAX_AMPLIFIER = 5;
    static final int JUMP_BOOST_MAX_AMPLIFIER = 5;
    static final int HIT_COUNT_TRIGGER_LEVEL_1 = 7;

    public DiamondKatanaItem(int attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, settings);
        ToolBreakCallback.EVENT.register((entity) -> {
            onKatanaBreak(entity, (Item)RegistryRecords.DIAMOND_SOULGEM.record.instance(), true);
        });
        OnKilledByCallback.EVENT.register(this::onKilledEntity);
        PlayerEntityTickCallback.EVENT.register(this::updateEffect);
    }

    private void swiftnessEffectUpdate(PlayerEntity player, ItemStack stack, int level){
        if((player == null) || (stack == null))
            return;
        int speedAmplifier = Math.min(level-1, SPEED_MAX_AMPLIFIER);
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SPEED,
                1,
                speedAmplifier,
                false,
                false,
                true));
    }

    private boolean hitCountAbilityTriggered(int level, int hitCount){
        int currentHitCountTrigger;
        switch (level){
            case 1:
            default:
                currentHitCountTrigger = HIT_COUNT_TRIGGER_LEVEL_1;
        }
        return hitCount >= currentHitCountTrigger;
    }

    private void skyboundAbilityTrigger(ItemStack stack, LivingEntity target, LivingEntity player, int level){
        if(level>=2 && !player.world.isClient){   //TODO: Katana level to ability level
            int jumpBoostAmplifier = Math.min(level-1-1, JUMP_BOOST_MAX_AMPLIFIER);
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.JUMP_BOOST,
                    (5+(int)(level*0.75))*20,
                    jumpBoostAmplifier));

            double knockbackResistance;
            knockbackResistance = Math.max(0.0, 1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            target.setVelocity(target.getVelocity().add(
                    0.0,
                    (0.30 + (float)(level-1)/5f) * knockbackResistance,
                    0.0));
            player.setVelocity(player.getVelocity().add(
                    0.0,
                    target.getVelocity().y-player.getVelocity().y,
                    0.0));
            player.velocityModified = true;
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if((target == null) || (attacker == null) || (stack == null))
            return false;

        int hitCount = Nbt.getHitCount(stack)+1;
        int level = Souls.getCurrentLevel(Nbt.getSoulCount(stack));
        if (hitCountAbilityTriggered(level, hitCount)){
            hitCount = 0;
            skyboundAbilityTrigger(stack, target, attacker, level);
        }
        Nbt.setHitCount(stack, hitCount);
        return super.postHit(stack, target, attacker);
    }

    @Override
    protected void updateEffect(PlayerEntity player) {
        if(player == null)
            return;
        ItemStack stack = player.getMainHandStack();
        if((stack.getItem() instanceof DiamondKatanaItem) && !player.world.isClient) {
            int level = Souls.getCurrentLevel(Nbt.getSoulCount(stack));
            swiftnessEffectUpdate(player, stack, level);
        }
    }

    @Override
    protected void onKatanaBreak(LivingEntity entity, Item droppedMaterial, boolean preserveNbt)
    {
        ItemStack stack = entity.getStackInHand(Hand.MAIN_HAND);
        if((stack.getItem() instanceof DiamondKatanaItem) && !entity.world.isClient){
            super.onKatanaBreak(entity, droppedMaterial, preserveNbt);
        }
    }

    @Override
    protected void onKilledEntity(LivingEntity adversary){
        if(adversary == null)
            return;
        ItemStack stack = adversary.getStackInHand(Hand.MAIN_HAND);
        if((stack.getItem() instanceof DiamondKatanaItem) && !adversary.world.isClient){
            super.onKilledEntity(adversary);
        }
    }

    public void appendTooltipExtra(Pair<ItemStack, List<Text>> callbackContext){
        Souls.appendTooltipExtraDiamond(callbackContext);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if(Screen.hasShiftDown()){
            appendTooltipExtra(new Pair(itemStack, tooltip));
        }else{
            //Katana description
            super.appendTooltip(itemStack, world, tooltip, tooltipContext);
            //Inlaid katana description
            tooltip.add(Text.translatable("item.katanamod.diamond_katana.tooltip.soulgem"));
            tooltip.add(Text.translatable("item.katanamod.diamond_katana.tooltip.element"));
            //Soul count
            super.appendTooltipSoulCount(itemStack, tooltip);
            //More info
            tooltip.add(Text.translatable("item.katanamod.tooltip_display_more_info").formatted(Formatting.GRAY));
        }
    }

}
