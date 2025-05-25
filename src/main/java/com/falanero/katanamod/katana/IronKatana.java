package com.falanero.katanamod.katana;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.ability.*;
import com.falanero.katanamod.item.Items;
import com.falanero.katanamod.item.katana.DiamondKatanaItem;
import com.falanero.katanamod.item.katana.KatanaItem;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class IronKatana implements Katana {

    @Override
    public @NotNull Map<Predicate<Item>, ConsumableAbility> getConsumableAbilities() {
        return Collections.emptyMap();
    }

    @Override
    public @NotNull List<AttackAbility> getOnSweepAttackAbilities() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<AttackAbility> getOnCritAttackAbilities() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<AttackAbility> getOnSprintAttackAbilities() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<AttackAbility> getPostAttackAbilities() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<TickAbility> getTickAbilities() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<KillAbility> getKillAbilities() {
        return Collections.emptyList();
    }

    @Override
    public Identifier getIconTexture() {
        return Identifier.of(KatanaMod.MOD_ID, "textures/item/iron_katana.png");
    }

    @Override
    public KatanaItem getItem() {
        return (KatanaItem) Items.IRON_KATANA;
    }

    @Override
    public Text getDescription() {
        return Text.translatable("katanamod.katana.iron_katana.description");
    }

    @Override
    public Text getName() {
        return Text.translatable("katanamod.katana.iron_katana.name");
    }

    @Override
    public List<Ability<?>> getAbilities() {
        return Collections.emptyList();
    }
}
