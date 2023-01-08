package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.callback.*;
import com.falanero.katanamod.util.Nbt;
import com.falanero.katanamod.util.Souls;
import com.falanero.katanamod.util.ability.*;
import com.falanero.katanamod.util.ability.common.kill.SeizeCommonAbility;
import com.falanero.katanamod.util.ability.common.kill.ShatterCommonAbility;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.NonnullDefault;

import java.util.*;

import static com.falanero.katanamod.util.Souls.*;

public abstract class KatanaItem extends SwordItem {

    public KatanaItem(int attackDamage, float attackSpeed, Item.Settings settings) {
        super(ToolMaterials.IRON, attackDamage, attackSpeed, settings);

        registerAbilities();
    }

    private void registerAbilities() {
        registerAttackAbilities(OnAttackCallback.ON_SWEEPING_ATTACK_CALLBACK_EVENT, getOnSweepAttackAbilities());
        registerAttackAbilities(OnAttackCallback.ON_CRIT_ATTACK_CALLBACK_EVENT, getOnCritAttackAbilities());
        registerAttackAbilities(OnAttackCallback.ON_SPRINT_ATTACK_CALLBACK_EVENT, getOnSprintAttackAbilities());
        registerOnKatanaBreakAbilities();
        registerOnKilledEntityAbilities();
        registerTickAbilities();
        registerConsumables();
    }

    private void registerOnKatanaBreakAbilities() {
        List<OnKatanaBreakAbility> abilities = getOnKatanaBreakAbilities();

        ToolBreakCallback.EVENT.register((LivingEntity entity) -> {
            ItemStack stack = getKatanaStack(entity, Hand.MAIN_HAND);
            OnKatanaBreakAbility shatterAbility = new ShatterCommonAbility();

            if ((stack != null) && !entity.world.isClient()) {
                shatterAbility.apply(entity, stack, this);
                abilities.stream().filter(Objects::nonNull).forEach(ability -> {
                    ability.apply(entity, stack, this);
                });
            }
        });
    }

    private void registerOnKilledEntityAbilities() {
        KillAbility seizeAbility = hasSeizeAbility() ? new SeizeCommonAbility() : null;
        List<KillAbility> abilities = getKillAbilities();

        OnKilledByCallback.EVENT.register((LivingEntity killer) -> {
            if (killer == null)
                return;

            ItemStack stack = getKatanaStack(killer, Hand.MAIN_HAND);
            if ((stack != null) && !killer.world.isClient) {
                if (seizeAbility != null) seizeAbility.apply(killer, stack);
                abilities.stream().filter(Objects::nonNull).forEach(ability -> {
                    ability.apply(killer, stack);
                });
            }
        });
    }

    private void registerTickAbilities() {
        List<TickAbility> abilities = getTickAbilities();

        PlayerEntityTickCallback.EVENT.register((PlayerEntity player) -> {
            if (player == null) return;

            ItemStack stack = getKatanaStack(player, null);
            if (stack != null) {
                int level = Souls.getCurrentLevel(Nbt.getSoulCount(stack));
                abilities.stream().filter(Objects::nonNull).forEach(ability -> {
                    ability.apply(player, level);
                });
            }
        });
    }

    private void registerAttackAbilities(Event<OnAttackCallback> event, List<AttackAbility> abilities) {
        event.register((Entity target, PlayerEntity attacker) -> {
            ItemStack stack = getKatanaStack(attacker, Hand.MAIN_HAND);
            if ((target == null) || (attacker == null) || (stack == null))
                return;

            int level = Souls.getCurrentLevel(Nbt.getSoulCount(stack));
            abilities.stream().filter(Objects::nonNull).forEach(ability -> {
                ability.apply(stack, (LivingEntity) target, attacker, level);
            });
        });
    }

    /**
     * @param hand hand to check. If {@code null}, checks both hands.
     * @return if the entity has this item in the given hand.
     */
    public boolean isHeldBy(LivingEntity entity, Hand hand) {
        if (hand == null)
            return (entity.getMainHandStack().getItem().getClass() == getClass()) ||
                    (entity.getOffHandStack().getItem().getClass() == getClass());
        return entity.getStackInHand(hand).getItem().getClass() == getClass();
    }

    /**
     * @param hand hand to get stack from. If {@code null}, checks both hands.
     * @return the stack of runtime katana class the entity has in the given hand, or {@code null} if no such stack is found.
     */
    public ItemStack getKatanaStack(LivingEntity entity, Hand hand) {
        if (hand == null) {
            ItemStack mainStack = entity.getMainHandStack();
            ItemStack offStack = entity.getOffHandStack();
            return (mainStack.getItem().getClass() == getClass()) ? mainStack :
                    (offStack.getItem().getClass() == getClass()) ? offStack : null;
        }

        ItemStack stack = entity.getStackInHand(hand);
        return (stack.getItem().getClass() == getClass()) ? stack : null;
    }


    @NotNull
    protected abstract Map<Item, ConsumableAbility> getConsumableAbilities();

    @NotNull
    protected abstract List<AttackAbility> getOnSweepAttackAbilities();

    @NotNull
    protected abstract List<AttackAbility> getOnCritAttackAbilities();

    @NotNull
    protected abstract List<AttackAbility> getOnSprintAttackAbilities();

    @NotNull
    protected abstract List<AttackAbility> getPostAttackAbilities();

    @NotNull
    protected abstract List<TickAbility> getTickAbilities();

    @NotNull
    protected abstract List<KillAbility> getKillAbilities();

    @NotNull
    protected abstract List<OnKatanaBreakAbility> getOnKatanaBreakAbilities();

    @NotNull
    public abstract Item getShatterItem();

    protected abstract boolean hasSeizeAbility();

    public abstract void appendTooltipExtra(ItemStack itemStack, List<Text> tooltip);

    protected abstract void appendInlaidKatanaDescription(List<Text> tooltip);

    private void registerConsumables() {
        OnItemUseCallback.ON_ITEM_USE_CALLBACK.register((world, user, hand) -> {
            Item usedItem = user.getStackInHand(hand).getItem();
            ConsumableAbility ability = getConsumableAbilities().get(usedItem);
            if ((ability != null) && (isHeldBy(user, null)))
                if (ability.apply(world, user, hand))
                    return ActionResult.CONSUME;
            return ActionResult.PASS;
        });
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if ((target == null) || (attacker == null) || (stack == null))
            return false;

        int level = Souls.getCurrentLevel(Nbt.getSoulCount(stack));
        List<AttackAbility> abilities = getPostAttackAbilities();
        abilities.stream().filter(Objects::nonNull).forEach(ability -> {
            ability.apply(stack, target, attacker, level);
        });

        stack.damage(1, attacker, e -> {
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            ToolBreakCallback.EVENT.invoker().notify(e);
        });
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getHardness(world, pos) != 0.0f) {
            stack.damage(2, miner, e -> {
                e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                ToolBreakCallback.EVENT.invoker().notify(e);
            });
        }
        return true;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable(this.getTranslationKey(stack), getCurrentLevel(Nbt.getSoulCount(stack)));
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (Screen.hasShiftDown()) {
            appendTooltipExtra(itemStack, tooltip);
        } else {
            //Katana description
            tooltip.add(Text.translatable("item.katanamod.katana_item.tooltip_line_1"));
            //Inlaid katana description
            appendInlaidKatanaDescription(tooltip);
            //Soul count
            Souls.appendTooltipSoulCount(itemStack, tooltip);
            //More info
            tooltip.add(Text.translatable("item.katanamod.tooltip_display_more_info").formatted(Formatting.GRAY));
        }
    }
}