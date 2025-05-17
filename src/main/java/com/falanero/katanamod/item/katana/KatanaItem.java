package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.ability.*;
import com.falanero.katanamod.ability.common.kill.SeizeCommonAbility;
import com.falanero.katanamod.callback.AfterDeathCallback;
import com.falanero.katanamod.callback.OnAttackCallback;
import com.falanero.katanamod.callback.OnItemUseCallback;
import com.falanero.katanamod.callback.PlayerEntityTickCallback;
import com.falanero.katanamod.util.Souls;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;

public abstract class KatanaItem extends Item {

    public KatanaItem(int attackDamage, float attackSpeed, Item.Settings settings) {
        super(settings.sword(ToolMaterial.IRON, attackDamage, attackSpeed));

        registerAbilities();
    }

    private void registerAbilities() {
        registerAttackAbilities(OnAttackCallback.ON_SWEEPING_ATTACK_CALLBACK_EVENT, getOnSweepAttackAbilities());
        registerAttackAbilities(OnAttackCallback.ON_CRIT_ATTACK_CALLBACK_EVENT, getOnCritAttackAbilities());
        registerAttackAbilities(OnAttackCallback.ON_SPRINT_ATTACK_CALLBACK_EVENT, getOnSprintAttackAbilities());
        registerOnKilledEntityAbilities();
        registerTickAbilities();
        registerConsumables();
    }

    private void registerOnKilledEntityAbilities() {
        KillAbility seizeAbility = hasSeizeAbility() ? new SeizeCommonAbility() : null;
        List<KillAbility> abilities = getKillAbilities();

        AfterDeathCallback.EVENT.register((entity, damageSource) -> {
            if (entity.getPrimeAdversary() instanceof LivingEntity killerLivingEntity
                    && killerLivingEntity.getWorld() instanceof ServerWorld serverWorld) {
                KatanaMod.LOGGER.info(damageSource.getDeathMessage(entity).getString());

                ItemStack stack = getPrioritizedKatanaStack(killerLivingEntity);

                if (stack != null) {
                    if (seizeAbility != null) seizeAbility.apply(killerLivingEntity, stack);
                    abilities.stream().filter(Objects::nonNull).forEach(ability -> {
                        ability.apply(killerLivingEntity, stack);
                    });
                }
            }
        });
    }

    private void registerTickAbilities() {
        List<TickAbility> abilities = getTickAbilities();

        PlayerEntityTickCallback.EVENT.register((PlayerEntity player) -> {
            if (player == null) return;

            ItemStack stack = getKatanaStack(player, null);
            if (stack != null) {
                int level = getCurrentLevel(KatanamodItemStackData.getSoulCount(stack));
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

            int level = getCurrentLevel(KatanamodItemStackData.getSoulCount(stack));
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
     * @return the stack of runtime katana type the entity has in hands.
     * Returns {@code null} if this type's katana is not the main one, when the entity has 2 katanas in their hands.
     */
    public ItemStack getPrioritizedKatanaStack(LivingEntity entity) {
        ItemStack mainStackKatana = getKatanaStack(entity, Hand.MAIN_HAND);
        ItemStack offStackKatana = getKatanaStack(entity, Hand.OFF_HAND);


        if ((mainStackKatana != null) || ((offStackKatana != null) && !(entity.getMainHandStack().getItem() instanceof KatanaItem))) {
            return mainStackKatana != null ? mainStackKatana : offStackKatana;
        }
        return null;
    }

    /**
     * @param hand hand to get stack from. If {@code null}, checks both hands.
     * @return the stack of runtime katana type the entity has in the given hand, or {@code null} if no such stack is found.
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
    protected abstract Map<Predicate<Item>, ConsumableAbility> getConsumableAbilities();

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

    protected abstract boolean hasSeizeAbility();

    public abstract void appendTooltipExtra(ItemStack itemStack, Consumer<Text> tooltip);

    protected abstract void appendInlaidKatanaDescription(Consumer<Text> tooltip);

    private void registerConsumables() {
        OnItemUseCallback.ON_ITEM_USE_CALLBACK.register((playerEntity, world, hand) -> {
            Item usedItem = playerEntity.getStackInHand(hand).getItem();

            List<ConsumableAbility> consumableAbilities = getConsumableAbilities()
                    .entrySet()
                    .stream()
                    .filter((p) -> p.getKey().test(playerEntity.getStackInHand(hand).getItem()))
                    .map(Map.Entry::getValue)
                    .toList();

            for (var ability : consumableAbilities) {
                if ((ability != null) && (isHeldBy(playerEntity, null))) {
                    int itemLevel = getCurrentLevel(KatanamodItemStackData.getSoulCount(getKatanaStack(playerEntity, null)));
                    if (ability.apply(world, playerEntity, hand, itemLevel))
                        return ActionResult.CONSUME;
                }
            }
            return ActionResult.PASS;
        });
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
//        if ((target == null) || (attacker == null) || (stack == null))
//            return false;

        int level = getCurrentLevel(KatanamodItemStackData.getSoulCount(stack));
        List<AttackAbility> abilities = getPostAttackAbilities();
        abilities.stream().filter(Objects::nonNull).forEach(ability -> {
            ability.apply(stack, target, attacker, level);
        });
    }

    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        if (Screen.hasShiftDown()) {
            appendTooltipExtra(itemStack, textConsumer);
        } else {
            //Katana description
            textConsumer.accept(Text.translatable("item.katanamod.katana_item.tooltip_line_1"));
            //Inlaid katana description
            appendInlaidKatanaDescription(textConsumer);
            //Soul count
            Souls.appendTooltipSoulCount(itemStack, textConsumer);
            //More info
            textConsumer.accept(Text.translatable("item.katanamod.tooltip_display_more_info").formatted(Formatting.GRAY));
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable(this.getTranslationKey(), getCurrentLevel(KatanamodItemStackData.getSoulCount(stack)));
    }


}