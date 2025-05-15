package com.falanero.katanamod;

import com.falanero.katanamod.component.Components;
import com.falanero.katanamod.item.Items;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    }

    @Override
    public void onInitialize() {
        Items.initialize();
//        Entities.register();
        Components.initialize();


        KatanaMod.LOGGER.info("katanamod initialized");
    }
}
