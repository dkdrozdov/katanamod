package com.falanero.katanamod;

import com.falanero.katanamod.component.Components;
import com.falanero.katanamod.item.Items;
import com.falanero.katanamod.util.CombatLogger;
import com.falanero.katanamod.command.Commands;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.*;

public class KatanaMod implements ModInitializer {
    public static final String MOD_ID = "katanamod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static void onSweepingAttack(Entity target, PlayerEntity attacker) {
        LOGGER.info(String.format("Event raised: Sweeping Attack (%s attacked %s)",
                attacker.getName().getString(), target.getName().getString()));
    }

    private static void onCritAttack(Entity target, PlayerEntity attacker) {
        LOGGER.info(String.format("Event raised: Crit Attack (%s attacked %s)",
                attacker.getName().getString(), target.getName().getString()));
    }

    private static void onSprintAttack(Entity target, PlayerEntity attacker) {
        LOGGER.info(String.format("Event raised: Sprint Attack (%s attacked %s)",
                attacker.getName().getString(), target.getName().getString()));
    }

    private static ActionResult onItemUse(World world, PlayerEntity user, Hand hand) {
        LOGGER.info(String.format("[katanamod:OnItemUseCallback]/Event raised: Item Usage (%s used %s)",
                user.getName().getString(), user.getStackInHand(hand).getItem().getName().getString()));

        return ActionResult.PASS;
    }

    private static void afterKilledOtherEntityFAPI(ServerWorld serverWorld, Entity entity, LivingEntity killedEntity) {
        LOGGER.info(String.format("[afterKilledOtherEntityFAPI]/Event raised: Entity Killed (%s killed %s, isClient = %s)",
                entity.getName().getString(), killedEntity.getName().getString(), serverWorld.isClient));
    }

    private static void afterKilledOtherEntityKatanaMod(LivingEntity killer) {
        LOGGER.info(String.format("[afterKilledOtherEntityKatanaMod]/Event raised: Entity Killed (%s killed someone, isClient = %s)",
                killer == null ? "null" : killer.getName().getString(), killer == null ? "?" : killer.getWorld().isClient));
    }

    private static void afterDeath(LivingEntity livingEntity, DamageSource damageSource) {
        LOGGER.info(
                String.format("[afterDeathFAPI]/Event raised: Entity died (%s died, isClient = %s) (%s)",
                        livingEntity.getName().getString(),
                        livingEntity.getWorld().isClient,
                        damageSource.getDeathMessage(livingEntity).getString()
                )
        );
        LOGGER.info("Attacker: {} Source: {}", damageSource.getAttacker() != null ? damageSource.getAttacker().getName().getString() : "null",
                damageSource.getSource() != null ? damageSource.getSource().getName().getString() : "null");
    }

    public static CombatLogger combatLogger = new CombatLogger();

    @Override
    public void onInitialize() {
        Items.initialize();
//        Entities.register();
        Components.initialize();
        Commands.register();

        KatanaMod.LOGGER.info("katanamod initialized");
    }
}
