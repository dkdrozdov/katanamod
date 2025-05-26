package com.falanero.katanamod.client;

import com.falanero.katanamod.ability.Ability;
import com.falanero.katanamod.item.katana.IronKatanaItem;
import com.falanero.katanamod.item.katana.KatanaItem;
import com.falanero.katanamod.katana.IronKatana;
import com.falanero.katanamod.katana.Katana;
import com.falanero.katanamod.katana.Katanas;
import com.falanero.katanamod.util.Souls;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;

import java.util.*;

import static com.falanero.katanamod.util.Souls.getSoulsForLevel;
import static com.falanero.katanamod.util.Souls.getSoulsNeeded;

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

        private WPanel KatanaInfoPanel(KatanaTabInfo info, int width, int height, int gridWidth, int gridHeight, int gridSize) {
            WPlainPanel rootPanel = new WPlainPanel();
            rootPanel.setInsets(Insets.ROOT_PANEL);
            rootPanel.setSize(width, height);

            WSprite icon = new WSprite(info.iconTexture);
            rootPanel.add(icon, 0, 0, gridSize * 2, gridSize * 2);

            WGridPanel panel = new WGridPanel();
            panel.setGaps(1, 0);
            panel.setInsets(Insets.ROOT_PANEL);
            rootPanel.add(panel, gridSize * 2, 7, gridSize * 9, 2 * gridSize);

            WText name = new WText(info.name);
            panel.add(name, 0, 0, 6, 1);

            WText description = new WText(info.description);
            panel.add(description, 0, 1, 9, 1);

            if (info.stackInfo != null) {
                KatanaTabInfo.KatanaStackInfo stackInfo = info.stackInfo;
                WText level = new WText(Text.translatable("katanamod.katana.level", stackInfo.level))
                        .setHorizontalAlignment(HorizontalAlignment.RIGHT);
                panel.add(level, 6, 0, 3, 1);

                BarPropertyDelegate barPropertyDelegate = new BarPropertyDelegate();
                barPropertyDelegate.current = stackInfo.soulsCurrent;
                barPropertyDelegate.max = stackInfo.soulsNeeded;

                WPlainPanel soulsBarPanel = new WPlainPanel();
                rootPanel.add(soulsBarPanel, 4 + 2 * gridSize, 0, 9 * gridSize, 10);

                WBar soulsBar = new WBar(
                        Identifier.ofVanilla("textures/gui/sprites/hud/experience_bar_background.png"),
                        Identifier.ofVanilla("textures/gui/sprites/hud/experience_bar_progress.png"),
                        0, 1, WBar.Direction.RIGHT);
                soulsBar.setProperties(barPropertyDelegate);

                soulsBar = soulsBar.withTooltip(Text.translatable("katanamod.katana.souls",
                        stackInfo.soulsCurrent,
                        stackInfo.soulsNeeded));

                soulsBarPanel.add(soulsBar, 0, 0, 180, 5);
            }

            WGridPanel abilitiesPanel = new WGridPanel();
            abilitiesPanel.setBackgroundPainter(BackgroundPainter.SLOT);
            abilitiesPanel.setInsets(Insets.ROOT_PANEL);
            abilitiesPanel.setGaps(21, 26);

            int abilityGridPosition = 0;
            for (var abilityInfo : info.abilitiesInfo) {
                WBox abilityBox = new WBox(Axis.VERTICAL);
                abilityBox.setInsets(Insets.NONE);
                abilityBox.setHorizontalAlignment(HorizontalAlignment.CENTER);
                abilityBox.setSpacing(1);

                WPlainPanel abilityPanel = new WPlainPanel();
                abilityPanel.setBackgroundPainter(BackgroundPainter.VANILLA);
                abilityPanel.setInsets(new Insets(3));
                TooltipSprite abilityIcon = new TooltipSprite(abilityInfo.icon);
                abilityPanel.add(abilityIcon, 0, 0, gridSize, gridSize);
                abilityIcon.appendTooltip(abilityInfo.name.copy().append("\n").formatted(Formatting.BOLD))
                        .appendTooltip(abilityInfo.description.copy().append("\n"));

                abilityBox.add(abilityPanel, gridSize, gridSize);
                if (abilityInfo.stackInfo != null) {
                    abilityBox.add(new WLabel(Text.translatable("katanamod.katana.ability.level",
                            abilityInfo.stackInfo.level,
                            abilityInfo.stackInfo.maxLevel)).setHorizontalAlignment(HorizontalAlignment.CENTER), abilityPanel.getWidth(), 5);

                    abilityIcon.appendTooltip(Text.translatable("katanamod.katana.ability.nextLevel",abilityInfo.stackInfo.nextLevelAt).formatted(Formatting.ITALIC, Formatting.GRAY));
                    if(abilityInfo.stackInfo.level == 0){
                        abilityPanel.setBackgroundPainter(BackgroundPainter.createColorful(0x666666, 0.5f));
                        abilityIcon.setOpaqueTint(0x666666);
                    }
                }

                abilitiesPanel.add(abilityBox, (abilityGridPosition) % gridWidth, abilityGridPosition / gridWidth, 2, 2);

                abilityGridPosition += 1;
            }
            rootPanel.add(abilitiesPanel, 0, 7 + 3 * gridSize, width, 4 * gridSize);

//
//            WButton button = new WButton(Text.translatable("item.katanamod.diamond_katana.ability.skybound.title", 3));
//            panel.add(button, 0, 3, 4, 1);
//
//            WLabel label = new WLabel(Text.literal("Test"), 0xFFFFFF);
//            panel.add(label, 0, 4, 2, 1);

            return rootPanel;
        }

        public KatanaModGui(PlayerEntity player) {
            int gridSize = 18;
            int gridWidth = 12;
            int gridHeight = 9;
            int tabWidth = gridSize * gridWidth;
            int tabHeight = gridSize * gridHeight;

            ItemStack katanaStack = null;
            Katana katana = null;
            if (player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof KatanaItem)
                katanaStack = player.getStackInHand(Hand.MAIN_HAND);
            if (player.getStackInHand(Hand.OFF_HAND).getItem() instanceof KatanaItem)
                katanaStack = player.getStackInHand(Hand.OFF_HAND);
            if (katanaStack != null && !(katanaStack.getItem() instanceof IronKatanaItem))
                katana = ((KatanaItem) katanaStack.getItem()).katana;


            setUseDefaultRootBackground(false);
            WTabPanel root = new WTabPanel();
            setRootPanel(root);

            List<Katana> katanas = List.of(
                    Katanas.IRON_KATANA,
                    Katanas.DIAMOND_KATANA
            );

            int i = 0;
            for (var k : katanas) {
                var info = new KatanaTabInfo(k, katana == k ? katanaStack : null);
                var tab = KatanaInfoPanel(info, tabWidth, tabHeight, gridWidth, gridHeight, gridSize);

                root.add(tab, t -> t
                        .tooltip(info.tabTooltip)
                        .icon(info.itemIcon)
                );

                if (katana == k) root.setSelectedIndex(i);

                i++;
            }

            root.validate(this);
        }

        @Override
        public void addPainters() {

        }
    }


    private static class KatanaTabInfo {

        private static class KatanaStackInfo {
            int level;
            int soulsCurrent;
            int soulsNeeded;

            public KatanaStackInfo(ItemStack katanaStack) {
                var soulsTotal = KatanamodItemStackData.getSoulCount(katanaStack);
                level = Souls.getCurrentLevel(soulsTotal);
                soulsCurrent = soulsTotal - getSoulsForLevel(level);
                soulsNeeded = getSoulsNeeded(level + 1);
            }
        }

        Identifier iconTexture;
        KatanaItem katanaItem;
        ItemIcon itemIcon;
        Text description;
        Text name;
        Text tabTooltip;
        List<AbilityPanelInfo> abilitiesInfo = new ArrayList<>();

        KatanaStackInfo stackInfo = null;

        public KatanaTabInfo(Katana katana, ItemStack katanaStack) {
            katanaItem = katana.getItem();
            itemIcon = new ItemIcon(katanaItem);
            iconTexture = katana.getIconTexture();
            description = katana.getDescription();
            name = katana.getName();
            tabTooltip = katana.getName();
            var abilities = katana.getAbilities();

            if (katanaStack != null) {
                stackInfo = new KatanaStackInfo(katanaStack);
            }

            for (var ability : abilities) {
                AbilityPanelInfo abilityInfo;

                if (stackInfo != null)
                    abilityInfo = new AbilityPanelInfo(ability, stackInfo);
                else
                    abilityInfo = new AbilityPanelInfo(ability);

                abilitiesInfo.add(abilityInfo);
            }
        }


        public static class AbilityPanelInfo {

            private static class AbilityStackInfo {
                int level;
                boolean isAvailible;
                int maxLevel;
                int nextLevelAt;

                public AbilityStackInfo(Ability<?> ability, KatanaStackInfo katanaStackInfo) {
                    var katanaLevel = katanaStackInfo.level;
                    level = ability.getAbilityLevel(katanaLevel);
                    isAvailible = level >= 1;
                    maxLevel = ability.getMaxLevel();
                    nextLevelAt = ability.getNeededLevelFor(level + 1);
                }
            }

            Ability<?> ability;
            Identifier icon;
            Text description;
            Text name;
            AbilityStackInfo stackInfo = null;

            public AbilityPanelInfo(Ability<?> ability, KatanaStackInfo katanaStackInfo) {
                this(ability);
                if (katanaStackInfo == null) return;

                stackInfo = new AbilityStackInfo(ability, katanaStackInfo);
            }

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


        tooltip.add(Language.getInstance()
                .reorder(wrapDescription(Texts.setStyleIfAbsent(lines.copy(), Style.EMPTY.withColor(Formatting.WHITE)), 140)).toArray(new OrderedText[0]));
    }
}

class BarPropertyDelegate implements PropertyDelegate {
    int current = 0;
    int max = 0;

    @Override
    public int get(int index) {
        return index == 0 ? current : max;
    }

    @Override
    public void set(int index, int value) {
        if (index == 0)
            current = value;
        else
            max = value;
    }

    @Override
    public int size() {
        return 2;
    }
}

