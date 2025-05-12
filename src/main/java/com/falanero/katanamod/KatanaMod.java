package com.falanero.katanamod;

import com.falanero.katanamod.callback.OnAttackCallback;
import com.falanero.katanamod.component.Components;
import com.falanero.katanamod.item.Items;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class KatanaMod implements ModInitializer {
    public static final String MOD_ID = "katanamod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static void getTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> list) {
        list.add(Text.literal("123123"));
    }

    private static void onSweepingAttack(Entity target, PlayerEntity attacker) {
        LOGGER.info(String.format("Event raised: Sweeping Attack (%s attacked %s)",
                attacker.getName(), target.getName()));
    }

    private static void onCritAttack(Entity target, PlayerEntity attacker) {
        LOGGER.info(String.format("Event raised: Crit Attack (%s attacked %s)",
                attacker.getName(), target.getName()));
    }

    private static void onSprintAttack(Entity target, PlayerEntity attacker) {
        LOGGER.info(String.format("Event raised: Sprint Attack (%s attacked %s)",
                attacker.getName(), target.getName()));
    }

    @Override
    public void onInitialize() {
//        ItemTooltipCallback.EVENT.register(KatanaMod::getTooltip);
        Items.initialize();
//        Entities.register();
        Components.initialize();
        OnAttackCallback.ON_SWEEPING_ATTACK_CALLBACK_EVENT.register(KatanaMod::onSweepingAttack);
        OnAttackCallback.ON_CRIT_ATTACK_CALLBACK_EVENT.register(KatanaMod::onCritAttack);
        OnAttackCallback.ON_SPRINT_ATTACK_CALLBACK_EVENT.register(KatanaMod::onSprintAttack);
        KatanaMod.LOGGER.info("mod initialized");
    }
}
