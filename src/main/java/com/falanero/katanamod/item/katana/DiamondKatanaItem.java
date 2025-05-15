package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.ability.AttackAbility;
import com.falanero.katanamod.ability.ConsumableAbility;
import com.falanero.katanamod.ability.KillAbility;
import com.falanero.katanamod.ability.TickAbility;
import com.falanero.katanamod.ability.diamond.tick.SpringDiamondAbility;
import com.falanero.katanamod.ability.diamond.tick.SwiftnessDiamondAbility;
import com.falanero.katanamod.callback.OnGetAirStrafingSpeedCallback;
import com.falanero.katanamod.item.soulgem.DiamondSoulgemItem;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;

public class DiamondKatanaItem extends KatanaItem {

    public DiamondKatanaItem(int attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, settings);
//
        OnGetAirStrafingSpeedCallback.ON_GET_AIR_STRAFING_SPEED_CALLBACK_EVENT.register(this::onGetAirStrafingSpeed);
//        OnComputeFallDamage.ON_COMPUTE_FALL_DAMAGE_CALLBACK_EVENT.register(this::onComputeFallDamage);
    }

    @Override
    public @NotNull Map<Predicate<Item>, ConsumableAbility> getConsumableAbilities() {
        return Map.of(
                (item) -> true, (world, user, hand, level) -> {
                    KatanaMod.LOGGER.info(String.format("[katanamod:OnItemUseCallback]/Event raised: Item Usage (%s used %s)",
                            user.getName().getString(), user.getStackInHand(hand).getItem().getName().getString()));
                    return false;
                }
//                PHANTOM_MEMBRANE, WindbombDiamondAbility.getAbility(),
//                FEATHER, FeatherbladeDiamondAbility.getAbility()
        );
    }

    @Override
    protected @NotNull List<AttackAbility> getOnSweepAttackAbilities() {
        return List.of((stack, target, attacker, level) -> {
            KatanaMod.LOGGER.info(String.format("Event raised: Sweeping Attack (%s attacked %s)",
                    attacker.getName().getString(), target.getName().getString()));
        });
    }

    @Override
    protected @NotNull List<AttackAbility> getOnCritAttackAbilities() {
        return List.of((stack, target, attacker, level) -> {
            KatanaMod.LOGGER.info(String.format("Event raised: Crit Attack (%s attacked %s)",
                    attacker.getName().getString(), target.getName().getString()));
        });
    }

    @Override
    protected @NotNull List<AttackAbility> getOnSprintAttackAbilities() {
        return List.of((stack, target, attacker, level) -> {
            KatanaMod.LOGGER.info(String.format("Event raised: Sprint Attack (%s attacked %s)",
                    attacker.getName().getString(), target.getName().getString()));
        });
    }

    @Override
    protected @NotNull List<AttackAbility> getPostAttackAbilities() {
        return List.of(
                (stack, target, attacker, itemLevel) -> {
                    KatanaMod.LOGGER.info(String.format("Event raised: Post Attack (%s attacked %s)",
                            attacker.getName().getString(), target.getName().getString()));
                }
//                SkyboundDiamondAbility.getAbility()

        );
    }

    @Override
    protected @NotNull List<TickAbility> getTickAbilities() {
        return List.of(SwiftnessDiamondAbility.getAbility(),
                SpringDiamondAbility.getAbility()
        );
    }

    @Override
    protected @NotNull List<KillAbility> getKillAbilities() {
        return Collections.emptyList();
    }

    @Override
    protected boolean hasSeizeAbility() {
        return true;
    }

    private Pair<Boolean, Float> onGetAirStrafingSpeed(float originalSpeed, PlayerEntity entity) {
        if (entity instanceof PlayerEntity player && isHeldBy(player, null)) {
            int itemLevel = getCurrentLevel(KatanamodItemStackData.getSoulCount(getKatanaStack(player, null)));
            return SwiftnessDiamondAbility.onGetAirStrafingSpeed(originalSpeed, player, itemLevel);
        }
        return new ImmutablePair<>(false, originalSpeed);
    }


//    private TypedActionResult<Integer> onComputeFallDamage(int fallDamage, float fallDistance, float damageMultiplier, LivingEntity entity) {
//        if (entity instanceof PlayerEntity player && isHeldBy(player, null)) {
//            int itemLevel = getCurrentLevel(KatanamodItemStackData.getSoulCount(getKatanaStack(player, null)));
//            return FeatherfallDiamondAbility.onComputeFallDamage(fallDamage, fallDistance, damageMultiplier, entity, itemLevel);
//        }
//        return new TypedActionResult<>(ActionResult.FAIL, fallDamage);
//    }
//
//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        ItemStack itemStack = user.getStackInHand(hand);
//        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
//        user.getItemCooldownManager().set(this, 20 * 5);
//
//        return TypedActionResult.success(itemStack, world.isClient());
//    }
//
//    private void debugHorizontalVelocity(PlayerEntity player) {
//        if (player.world.isClient()) {
//            double x = player.getVelocity().x;
//            double z = player.getVelocity().z;
//            float hv = (float) Math.sqrt(x * x + z * z);
//            player.sendMessage(Text.literal("horizontal velocity: " + String.format("%.2f", hv)), true);
//        }
//    }
//
    @Override
    public void appendTooltipExtra(ItemStack stack, Consumer<Text> tooltip) {
        DiamondSoulgemItem.appendExtra(stack, tooltip);
    }

    @Override
    protected void appendInlaidKatanaDescription(Consumer<Text> tooltip){
        tooltip.accept(Text.translatable("item.katanamod.diamond_katana.tooltip.soulgem"));
        tooltip.accept(Text.translatable("item.katanamod.diamond_katana.tooltip.element"));
    }
}
