package com.falanero.katanamod.util;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.callback.AfterDeathCallback;
import com.falanero.katanamod.callback.OnDamageAppliedCallback;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CombatLogger {

    private static Boolean isEnabled = true;
    private static Boolean logToChat = true;

    public void setEnabled(Boolean isEnabled) {
        CombatLogger.isEnabled = isEnabled;
    }

    public void setChat(Boolean logToChat) {
        CombatLogger.logToChat = logToChat;
    }

    public CombatLogger() {
        AfterDeathCallback.EVENT.register(CombatLogger::afterDeath);
        OnDamageAppliedCallback.ON_DAMAGE_APPLIED_EVENT.register(CombatLogger::onDamageApplied);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("combatlog")
                            .then(literal("enabled").then(argument("isEnabled", BoolArgumentType.bool()).executes(context -> {
                                boolean isEnabled = BoolArgumentType.getBool(context, "isEnabled");
                                setEnabled(isEnabled);
                                context.getSource().sendFeedback(() -> Text.literal("Combat logger is " + (isEnabled ? "enabled." : "disabled.")), false);
                                return 1;
                            })))
                            .then(literal("chat").then(argument("isEnabled", BoolArgumentType.bool()).executes(context -> {
                                boolean isEnabled = BoolArgumentType.getBool(context, "isEnabled");
                                setChat(isEnabled);
                                context.getSource().sendFeedback(() -> Text.literal("Combat logger chat streaming is " + (isEnabled ? "enabled." : "disabled.")), false);
                                return 1;
                            })))
            );
        });

    }

    private static void log(Text message, MinecraftServer server) {
        if (!isEnabled) return;
        KatanaMod.LOGGER.info(message.getString());
        if (logToChat) server.getPlayerManager().broadcast(message, false);
    }

    private static String nonNullGetString(Entity e) {
        return (e != null ? e.getName().getString() : "");
    }

    private static void logCombatEvent(LivingEntity livingEntity, DamageSource damageSource, float amount) {
        var server = livingEntity.getServer();
        var source = damageSource.getSource();
        var attacker = damageSource.getAttacker();
        var weaponStack = damageSource.getWeaponStack();
        MutableText text;
        Formatting damageTypeFormatting = getDamageTypeFormatting(damageSource);

        if (attacker != null) {
            String weapon;
            if (weaponStack == null)
                weapon = nonNullGetString(source);
            else
                weapon = weaponStack.getName().getString();

            text = Text.empty()
                    .append(Text.literal("[CombatLogger]").formatted(Formatting.YELLOW))
                    .append(Text.literal(": "))
                    .append(Text.literal(nonNullGetString(source)))
                    .append(Text.literal(" did "))
                    .append(Text.literal(String.valueOf(amount)))
                    .append(Text.literal(" "))
                    .append(Text.literal(damageSource.getType().msgId()).formatted(damageTypeFormatting))
                    .append(Text.literal(" damage to "))
                    .append(Text.literal(livingEntity.getName().getString()))
                    .append(Text.literal(" with "))
                    .append(Text.literal(weapon)).append(Text.literal(" ("))
                    .append(Text.literal(String.valueOf(livingEntity.getHealth())).formatted(Formatting.RED))
                    .append(Text.literal("->"))
                    .append(Text.literal(String.valueOf(livingEntity.getHealth() - amount)).formatted(Formatting.RED))
                    .append(Text.literal(")"));
        } else {
            text = Text.empty()
                    .append(Text.literal("[CombatLogger]").formatted(Formatting.YELLOW))
                    .append(Text.literal(": "))
                    .append(Text.literal(livingEntity.getName().getString()))
                    .append(Text.literal(" got "))
                    .append(Text.literal(String.valueOf(amount)))
                    .append(Text.literal(" "))
                    .append(Text.literal(damageSource.getType().msgId()).formatted(damageTypeFormatting))
                    .append(Text.literal(" damage ("))
                    .append(Text.literal(String.valueOf(livingEntity.getHealth())).formatted(Formatting.RED))
                    .append(Text.literal("->"))
                    .append(Text.literal(String.valueOf(livingEntity.getHealth() - amount)).formatted(Formatting.RED))
                    .append(Text.literal(")"));
        }

        log(text, server);
    }


    private static Formatting getDamageTypeFormatting(DamageSource source) {
        if (source.isOf(DamageTypes.IN_FIRE)) return Formatting.YELLOW;
        if (source.isOf(DamageTypes.CAMPFIRE)) return Formatting.YELLOW;
        if (source.isOf(DamageTypes.LIGHTNING_BOLT)) return Formatting.BLUE;
        if (source.isOf(DamageTypes.ON_FIRE)) return Formatting.YELLOW;
        if (source.isOf(DamageTypes.LAVA)) return Formatting.YELLOW;
        if (source.isOf(DamageTypes.HOT_FLOOR)) return Formatting.YELLOW;
        if (source.isOf(DamageTypes.IN_WALL)) return Formatting.DARK_GRAY;
        if (source.isOf(DamageTypes.CRAMMING)) return Formatting.DARK_GRAY;
        if (source.isOf(DamageTypes.DROWN)) return Formatting.DARK_BLUE;
        if (source.isOf(DamageTypes.STARVE)) return Formatting.DARK_RED;
        if (source.isOf(DamageTypes.CACTUS)) return Formatting.GREEN;
        if (source.isOf(DamageTypes.FALL)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.ENDER_PEARL)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.FLY_INTO_WALL)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.OUT_OF_WORLD)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.GENERIC)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.MAGIC)) return Formatting.LIGHT_PURPLE;
        if (source.isOf(DamageTypes.WITHER)) return Formatting.BLACK;
        if (source.isOf(DamageTypes.DRAGON_BREATH)) return Formatting.LIGHT_PURPLE;
        if (source.isOf(DamageTypes.DRY_OUT)) return Formatting.DARK_GRAY;
        if (source.isOf(DamageTypes.SWEET_BERRY_BUSH)) return Formatting.GREEN;
        if (source.isOf(DamageTypes.FREEZE)) return Formatting.BLUE;
        if (source.isOf(DamageTypes.STALAGMITE)) return Formatting.DARK_GRAY;
        if (source.isOf(DamageTypes.FALLING_BLOCK)) return Formatting.DARK_GRAY;
        if (source.isOf(DamageTypes.FALLING_ANVIL)) return Formatting.DARK_GRAY;
        if (source.isOf(DamageTypes.FALLING_STALACTITE)) return Formatting.DARK_GRAY;
        if (source.isOf(DamageTypes.STING)) return Formatting.GREEN;
        if (source.isOf(DamageTypes.MOB_ATTACK)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.MOB_ATTACK_NO_AGGRO)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.PLAYER_ATTACK)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.ARROW)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.TRIDENT)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.MOB_PROJECTILE)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.SPIT)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.WIND_CHARGE)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.FIREWORKS)) return Formatting.YELLOW;
        if (source.isOf(DamageTypes.FIREBALL)) return Formatting.YELLOW;
        if (source.isOf(DamageTypes.UNATTRIBUTED_FIREBALL)) return Formatting.YELLOW;
        if (source.isOf(DamageTypes.WITHER_SKULL)) return Formatting.BLACK;
        if (source.isOf(DamageTypes.THROWN)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.INDIRECT_MAGIC)) return Formatting.LIGHT_PURPLE;
        if (source.isOf(DamageTypes.THORNS)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.EXPLOSION)) return Formatting.YELLOW;
        if (source.isOf(DamageTypes.PLAYER_EXPLOSION)) return Formatting.YELLOW;
        if (source.isOf(DamageTypes.SONIC_BOOM)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.BAD_RESPAWN_POINT)) return Formatting.DARK_GRAY;
        if (source.isOf(DamageTypes.OUTSIDE_BORDER)) return Formatting.DARK_GRAY;
        if (source.isOf(DamageTypes.GENERIC_KILL)) return Formatting.GRAY;
        if (source.isOf(DamageTypes.MACE_SMASH)) return Formatting.GRAY;

        return Formatting.WHITE;
    }

    private static void afterDeath(LivingEntity livingEntity, DamageSource damageSource) {
        var server = livingEntity.getServer();
        var adversary = livingEntity.getPrimeAdversary();
        MutableText text;

        if (adversary != null) {
            text = Text.empty()
                    .append(Text.literal("[CombatLogger]").formatted(Formatting.YELLOW))
                    .append(Text.literal(": "))
                    .append(Text.literal(nonNullGetString(adversary)))
                    .append(Text.literal(" killed "))
                    .append(Text.literal(livingEntity.getName().getString()));
        } else {
            text = Text.empty()
                    .append(Text.literal("[CombatLogger]").formatted(Formatting.YELLOW))
                    .append(Text.literal(": "))
                    .append(Text.literal(livingEntity.getName().getString()))
                    .append(Text.literal(" died."));
        }

        log(text, server);
    }

    private static void onDamageApplied(ServerWorld world, DamageSource source, float amount, LivingEntity entity) {
        logCombatEvent(entity, source, amount);
    }
}

