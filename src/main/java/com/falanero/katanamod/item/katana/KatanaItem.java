package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.callback.ToolBreakCallback;
import com.falanero.katanamod.util.Nbt;
import com.falanero.katanamod.util.Souls;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

import static com.falanero.katanamod.util.Souls.*;

public class KatanaItem extends SwordItem {

    public KatanaItem(int attackDamage, float attackSpeed, Item.Settings settings) {
        super(ToolMaterials.IRON, attackDamage, attackSpeed, settings);
    }

    protected void updateEffect(PlayerEntity player) {
        //I'm not too sure what to write here
    }

    /**
     * Called when the katana breaks after use, hit or mine.
     * <p>Usually called before the decrement of its {@link ItemStack}.</p>
     * @param entity the entity that holds the katana.
     * @param droppedMaterial the item that should be dropped.
     * Iron ingot in case of IronKatana, otherwise the SoulStoneItem of the same type as katana.
     * @param preserveNbt whether the dropped item should have katana's data or not,
     * {@code true} if katana is not IronKatana.
     */
    protected void onKatanaBreak(LivingEntity entity, Item droppedMaterial, boolean preserveNbt)
    {
        ItemStack stack = entity.getStackInHand(Hand.MAIN_HAND);
        if(!entity.world.isClient() && (stack.getItem() instanceof KatanaItem)){
            ItemStack droppedStack = Objects.requireNonNull(entity.dropItem(droppedMaterial, 2)).getStack();
            ItemStack ironSwordStack = Objects.requireNonNull(entity.dropItem(Items.IRON_SWORD, 1)).getStack();
            ironSwordStack.setDamage((int)(ironSwordStack.getMaxDamage()*0.7));

            if(preserveNbt){
                int soulCount = Nbt.getSoulCount(stack);
                if(!droppedStack.hasNbt()){
                    droppedStack.setNbt(new NbtCompound());
                }
                Nbt.setSoulCount(droppedStack, soulCount);
            }
        }
    }

    protected void onKilledEntity(LivingEntity killer){
        ItemStack stack = killer.getStackInHand(Hand.MAIN_HAND);
        if((stack.getItem() instanceof KatanaItem) && !killer.world.isClient) {
            int soulCount = Nbt.getSoulCount(stack) + 1;
            int level = getCurrentLevel(soulCount);
            Nbt.setSoulCount(stack, soulCount);
            ServerPlayerEntity serverPlayerEntity = Objects.requireNonNull(killer.getServer()).getPlayerManager().getPlayer(killer.getUuid());
            assert serverPlayerEntity != null;
            serverPlayerEntity.sendMessage(Text.translatable("item.katanamod.tooltip_souls",
                    soulCount - getSoulsForLevel(level), Souls.getSoulsNeeded(level+1)), true);
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e ->{
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            ToolBreakCallback.EVENT.invoker().notify(e);
        });
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getHardness(world, pos) != 0.0f) {
            stack.damage(2, miner, e ->{
                e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                ToolBreakCallback.EVENT.invoker().notify(e);
            });
        }
        return true;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable(this.getTranslationKey(stack), getCurrentLevel(Nbt.getSoulCount(stack)));
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.katanamod.katana_item.tooltip_line_1"));
    }

    public void appendTooltipLevel(ItemStack stack, List<Text> tooltip){
        Souls.appendTooltipLevel(stack, tooltip);
    }

    public void appendTooltipSoulCount(ItemStack stack, List<Text> tooltip){
        Souls.appendTooltipSoulCount(stack, tooltip);
    }
}
