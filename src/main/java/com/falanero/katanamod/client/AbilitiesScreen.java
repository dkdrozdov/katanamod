package com.falanero.katanamod.client;

import com.falanero.katanamod.ability.Ability;
import com.falanero.katanamod.item.katana.KatanaItem;
import com.falanero.katanamod.katana.Katana;
import com.falanero.katanamod.katana.Katanas;
import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;

import java.util.*;
import java.util.function.Consumer;

public class AbilitiesScreen extends CottonClientScreen {
    public AbilitiesScreen(GuiDescription description) {
        super(description);
    }


    public AbilitiesScreen(ClientPlayerEntity player) {
        super(new KatanaModGui(player));
    }

    public static void open() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        MinecraftClient.getInstance().setScreen(new AbilitiesScreen(player));
    }

    public static class KatanaModGui extends LightweightGuiDescription {

        private WGridPanel KatanaInfoPanel(KatanaTabInfo info, int width, int height, int gridWidth, int gridHeight) {

            WGridPanel panel = new WGridPanel();
            panel.setSize(width, height);
            panel.setGaps(4, 4);
            panel.setInsets(Insets.ROOT_PANEL);

            WSprite icon = new WSprite(info.iconTexture);
            panel.add(icon, 0, 0, 2, 2);

            WText name = new WText(info.name);
            panel.add(name, 2, 0, 8, 1);

            WText description = new WText(info.description);
            panel.add(description, 2, 1, 8, 2);

            WGridPanel abilitiesPanel = new WGridPanel();
            abilitiesPanel.setBackgroundPainter(BackgroundPainter.SLOT);
            abilitiesPanel.setInsets(Insets.ROOT_PANEL);
            abilitiesPanel.setGaps(21, 21);
            panel.add(abilitiesPanel, 0, 2, gridWidth, gridHeight - 2);

            int abilityGridPosition = 0;
            for (var abilityInfo : info.abilitiesInfo) {
                WGridPanel abilityPanel = new WGridPanel();
                abilityPanel.setBackgroundPainter(BackgroundPainter.VANILLA);
                abilityPanel.setGaps(7, 7);
                abilityPanel.setInsets(new Insets(2));
                TooltipSprite abilityIcon = new TooltipSprite(abilityInfo.icon);
                abilityPanel.add(abilityIcon, 0, 0, 1, 1);
                abilityIcon.appendTooltip(abilityInfo.name.copy().append("\n").formatted(Formatting.BOLD))
                        .appendTooltip(abilityInfo.description);

                abilitiesPanel.add(abilityPanel, (abilityGridPosition) % gridWidth, abilityGridPosition / gridWidth);

                abilityGridPosition += 1;
            }
//
//            WButton button = new WButton(Text.translatable("item.katanamod.diamond_katana.ability.skybound.title", 3));
//            panel.add(button, 0, 3, 4, 1);
//
//            WLabel label = new WLabel(Text.literal("Test"), 0xFFFFFF);
//            panel.add(label, 0, 4, 2, 1);

            return panel;
        }

        public KatanaModGui(PlayerEntity player) {
            int gridSize = 18;
            int gridWidth = 10;
            int gridHeight = 6;
            int tabWidth = gridSize * gridWidth;
            int tabHeight = gridSize * gridHeight;

            setUseDefaultRootBackground(false);
            WTabPanel root = new WTabPanel();
            setRootPanel(root);

            List<Katana> katanas = List.of(
                    Katanas.IRON_KATANA,
                    Katanas.DIAMOND_KATANA
            );

            for (var katana : katanas) {
                var info = new KatanaTabInfo(katana);
                var tab = KatanaInfoPanel(info, tabWidth, tabHeight, gridWidth, gridHeight);

                root.add(tab, t -> t
                        .tooltip(info.tabTooltip)
                        .icon(info.itemIcon)
                );
            }

            root.validate(this);
        }

        @Override
        public void addPainters() {

        }
    }

    private static class KatanaTabInfo {
        Identifier iconTexture;
        KatanaItem katanaItem;
        ItemIcon itemIcon;
        Text description;
        Text name;
        Text tabTooltip;
        List<AbilityPanelInfo> abilitiesInfo;

        public KatanaTabInfo(Katana katana) {
            katanaItem = katana.getItem();
            itemIcon = new ItemIcon(katanaItem);
            iconTexture = katana.getIconTexture();
            description = katana.getDescription();
            name = katana.getName();
            tabTooltip = katana.getName();
            var abilities = katana.getAbilities();

            abilitiesInfo = new ArrayList<>();
            for (var ability : abilities) {
                abilitiesInfo.add(new AbilityPanelInfo(ability));
            }
        }


        public static class AbilityPanelInfo {
            Ability<?> ability;
            Identifier icon;
            Text description;
            Text name;

            public AbilityPanelInfo(Ability<?> ability) {
                this.ability = ability;
                icon = ability.getIconTexture();
                name = ability.getName();
                description = ability.getDescription();
            }
        }
    }
}

class TooltipSprite extends WSprite {

    public TooltipSprite(Identifier image) {
        super(image);
    }

    MutableText lines = Text.empty();
    MinecraftClient client = MinecraftClient.getInstance();
    private static final int[] SPLIT_OFFSET_CANDIDATES = new int[]{0, 10, -10, 25, -25};

    public TooltipSprite appendTooltip(Text... lines) {
        for (Text line : lines) {
            this.lines.append(line);
        }

        return this;
    }


    private static float getMaxWidth(TextHandler textHandler, List<StringVisitable> lines) {
        return (float) lines.stream().mapToDouble(textHandler::getWidth).max().orElse(0.0);
    }

    private List<StringVisitable> wrapDescription(Text text, int width) {
        TextHandler textHandler = this.client.textRenderer.getTextHandler();
        List<StringVisitable> list = null;
        float f = Float.MAX_VALUE;

        for (int i : SPLIT_OFFSET_CANDIDATES) {
            List<StringVisitable> list2 = textHandler.wrapLines(text, width - i, Style.EMPTY);
            float g = Math.abs(getMaxWidth(textHandler, list2) - width);
            if (g <= 10.0F) {
                return list2;
            }

            if (g < f) {
                f = g;
                list = list2;
            }
        }

        return list;
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        int i = 80;
        int k = 29 + i;


        tooltip.add(Language.getInstance()
                .reorder(wrapDescription(Texts.setStyleIfAbsent(lines.copy(), Style.EMPTY.withColor(Formatting.WHITE)), k)).toArray(new OrderedText[0]));
    }
}



