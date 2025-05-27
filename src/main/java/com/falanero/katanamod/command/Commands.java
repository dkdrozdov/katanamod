package com.falanero.katanamod.command;

import com.falanero.katanamod.callback.AfterDeathCallback;
import com.falanero.katanamod.callback.OnDamageAppliedCallback;
import com.falanero.katanamod.item.Items;
import com.falanero.katanamod.item.katana.KatanaItem;
import com.falanero.katanamod.util.CombatLogger;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import static com.falanero.katanamod.util.Souls.getSoulsForLevel;
import static com.falanero.katanamod.util.Souls.getSoulsNeeded;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class Commands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("katana").then(CommandManager.literal("level").then(CommandManager.argument("level", IntegerArgumentType.integer())
                    .executes(context -> {
                        if (context.getSource().getPlayer() instanceof PlayerEntity player) {
                            var level = IntegerArgumentType.getInteger(context, "level");

                            ItemStack katanaStack = null;
                            if (player.getStackInHand(Hand.OFF_HAND).getItem() instanceof KatanaItem)
                                katanaStack = player.getStackInHand(Hand.OFF_HAND);
                            if (player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof KatanaItem)
                                katanaStack = player.getStackInHand(Hand.MAIN_HAND);

                            if (katanaStack != null)
                                KatanamodItemStackData.setSoulCount(katanaStack, getSoulsForLevel(level));

                            context.getSource().sendFeedback(() -> Text.literal("Set katana level to " + level + "."), true);
                            return 1;
                        }
                        return 0;
                    }))));
        });
    }
}
