package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.callback.OnAttackCallback;
import com.falanero.katanamod.callback.OnKilledByCallback;
import com.falanero.katanamod.callback.PlayerEntityTickCallback;
import com.falanero.katanamod.callback.ToolBreakCallback;
import com.falanero.katanamod.registry.RegistryRecords;
import com.falanero.katanamod.util.Ability.Diamond.SkyboundDiamondAbility;
import com.falanero.katanamod.util.Ability.Diamond.SwiftnessDiamondAbility;
import com.falanero.katanamod.util.Nbt;
import com.falanero.katanamod.util.Souls;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class DiamondKatanaItem extends KatanaItem {
    public DiamondKatanaItem(int attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, settings);
        ToolBreakCallback.EVENT.register((entity) -> onKatanaBreak(entity, (Item)RegistryRecords.DIAMOND_SOULGEM.record.instance(), true));
        OnKilledByCallback.EVENT.register(this::onKilledEntity);
        PlayerEntityTickCallback.EVENT.register(this::updateEffect);
        OnAttackCallback.ON_SWEEPING_ATTACK_CALLBACK_EVENT.register(this::sweepingAttack);
        OnAttackCallback.ON_CRIT_ATTACK_CALLBACK_EVENT.register(this::critAttack);
        OnAttackCallback.ON_SPRINT_ATTACK_CALLBACK_EVENT.register(this::sprintAttack);
    }

    private void sweepingAttack(Entity target, PlayerEntity player){
        if(player == null)
            return;
        ItemStack stack = player.getMainHandStack();
        if((stack.getItem() instanceof DiamondKatanaItem) && !player.world.isClient)
        {
            KatanaMod.LOGGER.info("It's a sweep attack!!!");
        }
    }
    private void critAttack(Entity target, PlayerEntity player){
        if(player == null)
            return;
        ItemStack stack = player.getMainHandStack();
        if((stack.getItem() instanceof DiamondKatanaItem) && !player.world.isClient) {
            KatanaMod.LOGGER.info("It's a crit attack!!!");
        }
    }
    private void sprintAttack(Entity target, PlayerEntity player){
        if(player == null)
            return;
        ItemStack stack = player.getMainHandStack();
        if((stack.getItem() instanceof DiamondKatanaItem) && !player.world.isClient) {
            KatanaMod.LOGGER.info("It's a sprint attack!!!");
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        user.getItemCooldownManager().set(this, 20*5);

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if((target == null) || (attacker == null) || (stack == null))
            return false;

        int level = Souls.getCurrentLevel(Nbt.getSoulCount(stack));
        SkyboundDiamondAbility.tryApply(stack, target, attacker, level);
        return super.postHit(stack, target, attacker);
    }

    @Override
    protected void updateEffect(PlayerEntity player) {
        if(player == null)
            return;

        ItemStack stack = player.getMainHandStack();
        if(stack == null)
            return;

        if((stack.getItem() instanceof DiamondKatanaItem)) {
            int level = Souls.getCurrentLevel(Nbt.getSoulCount(stack));
            SwiftnessDiamondAbility.effectTick(player, level);
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
            appendTooltipExtra(new Pair<>(itemStack, tooltip));
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
