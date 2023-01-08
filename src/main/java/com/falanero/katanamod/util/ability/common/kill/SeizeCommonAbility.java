package com.falanero.katanamod.util.ability.common.kill;

import com.falanero.katanamod.util.Nbt;
import com.falanero.katanamod.util.Souls;
import com.falanero.katanamod.util.ability.KillAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;
import static com.falanero.katanamod.util.Souls.getSoulsForLevel;

public class SeizeCommonAbility implements KillAbility {
    @Override
    public void apply(LivingEntity killer, ItemStack stack) {
        int soulCount = Nbt.getSoulCount(stack) + 1;
        int level = getCurrentLevel(soulCount);
        Nbt.setSoulCount(stack, soulCount);
        ServerPlayerEntity serverPlayerEntity = Objects.requireNonNull(killer.getServer()).getPlayerManager().getPlayer(killer.getUuid());
        assert serverPlayerEntity != null;
        serverPlayerEntity.sendMessage(Text.translatable("item.katanamod.tooltip_souls",
                soulCount - getSoulsForLevel(level), Souls.getSoulsNeeded(level + 1)), true);
    }
}
