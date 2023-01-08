package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.callback.*;
import com.falanero.katanamod.item.soulgem.DiamondSoulgemItem;
import com.falanero.katanamod.registry.Instances;
import com.falanero.katanamod.util.Nbt;
import com.falanero.katanamod.util.ability.*;
import com.falanero.katanamod.util.ability.diamond.FeatherfallDiamondAbility;
import com.falanero.katanamod.util.ability.diamond.attack.SkyboundDiamondAbility;
import com.falanero.katanamod.util.ability.diamond.tick.SpringDiamondAbility;
import com.falanero.katanamod.util.ability.diamond.tick.SwiftnessDiamondAbility;
import com.falanero.katanamod.util.ability.diamond.consumable.FeatherbladeDiamondAbility;
import com.falanero.katanamod.util.ability.diamond.consumable.WindbombDiamondAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;
import static net.minecraft.item.Items.FEATHER;
import static net.minecraft.item.Items.PHANTOM_MEMBRANE;

public class DiamondKatanaItem extends KatanaItem {

    public DiamondKatanaItem(int attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, settings);

        OnGetAirStrafingSpeedCallback.ON_GET_AIR_STRAFING_SPEED_CALLBACK_EVENT.register(this::onGetAirStrafingSpeed);
        OnComputeFallDamage.ON_COMPUTE_FALL_DAMAGE_CALLBACK_EVENT.register(this::onComputeFallDamage);
    }

    @Override
    public @NotNull Map<Item, ConsumableAbility> getConsumableAbilities() {
        return Map.of(
                PHANTOM_MEMBRANE, WindbombDiamondAbility.getAbility(),
                FEATHER, FeatherbladeDiamondAbility.getAbility());
    }

    @Override
    protected @NotNull List<AttackAbility> getOnSweepAttackAbilities() {
        return Collections.emptyList();
    }

    @Override
    protected @NotNull List<AttackAbility> getOnCritAttackAbilities() {
        return Collections.emptyList();
    }

    @Override
    protected @NotNull List<AttackAbility> getOnSprintAttackAbilities() {
        return Collections.emptyList();
    }

    @Override
    protected @NotNull List<AttackAbility> getPostAttackAbilities() {
        return List.of(SkyboundDiamondAbility.getAbility());
    }

    @Override
    protected @NotNull List<TickAbility> getTickAbilities() {
        return List.of(SwiftnessDiamondAbility.getAbility(),
                SpringDiamondAbility.getAbility());
    }

    @Override
    protected @NotNull List<KillAbility> getKillAbilities() {
        return Collections.emptyList();
    }

    @Override
    protected @NotNull List<OnKatanaBreakAbility> getOnKatanaBreakAbilities() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull Item getShatterItem() {
        return Instances.DIAMOND_SOULGEM;
    }

    @Override
    protected boolean hasSeizeAbility() {
        return true;
    }

    private TypedActionResult<Float> onGetAirStrafingSpeed(float airStrafingSpeed, LivingEntity entity) {
        if (entity instanceof PlayerEntity player && isHeldBy(player, null)) {
            int itemLevel = getCurrentLevel(Nbt.getSoulCount(getKatanaStack(player, null)));
            return SwiftnessDiamondAbility.onGetAirStrafingSpeed(airStrafingSpeed, player, itemLevel);
        }
        return new TypedActionResult<>(ActionResult.FAIL, airStrafingSpeed);
    }

    private TypedActionResult<Integer> onComputeFallDamage(int fallDamage, float fallDistance, float damageMultiplier, LivingEntity entity) {
        if (entity instanceof PlayerEntity player && isHeldBy(player, null)) {
            int itemLevel = getCurrentLevel(Nbt.getSoulCount(getKatanaStack(player, null)));
            return FeatherfallDiamondAbility.onComputeFallDamage(fallDamage, fallDistance, damageMultiplier, entity, itemLevel);
        }
        return new TypedActionResult<>(ActionResult.FAIL, fallDamage);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        user.getItemCooldownManager().set(this, 20 * 5);

        return TypedActionResult.success(itemStack, world.isClient());
    }

    private void debugHorizontalVelocity(PlayerEntity player) {
        if (player.world.isClient()) {
            double x = player.getVelocity().x;
            double z = player.getVelocity().z;
            float hv = (float) Math.sqrt(x * x + z * z);
            player.sendMessage(Text.literal("horizontal velocity: " + String.format("%.2f", hv)), true);
        }
    }

    @Override
    public void appendTooltipExtra(ItemStack stack, List<Text> tooltip) {
        DiamondSoulgemItem.appendExtra(stack, tooltip);
    }

    @Override
    protected void appendInlaidKatanaDescription(List<Text> tooltip){
        tooltip.add(Text.translatable("item.katanamod.diamond_katana.tooltip.soulgem"));
        tooltip.add(Text.translatable("item.katanamod.diamond_katana.tooltip.element"));
    }
}
