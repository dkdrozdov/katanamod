package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.item.soulgem.DiamondSoulgemItem;
import com.falanero.katanamod.katana.Katana;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class DiamondKatanaItem extends KatanaItem {

    public DiamondKatanaItem(int attackDamage, float attackSpeed, Settings settings, Katana katana) {
        super(attackDamage, attackSpeed, settings, katana);
    }

    @Override
    protected boolean hasSeizeAbility() {
        return true;
    }

    //    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        ItemStack itemStack = user.getStackInHand(hand);
//        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
//        user.getItemCooldownManager().set(this, 20 * 5);
//
//        return TypedActionResult.success(itemStack, world.isClient());
//    }
//
//    private void debugHorizontalVelocity(PlayerEntity player) {
//        if (player.world.isClient()) {
//            double x = player.getVelocity().x;
//            double z = player.getVelocity().z;
//            float hv = (float) Math.sqrt(x * x + z * z);
//            player.sendMessage(Text.literal("horizontal velocity: " + String.format("%.2f", hv)), true);
//        }
//    }
//
    @Override
    public void appendTooltipExtra(ItemStack stack, Consumer<Text> tooltip) {
        DiamondSoulgemItem.appendExtra(stack, tooltip);
    }

    @Override
    protected void appendInlaidKatanaDescription(Consumer<Text> tooltip) {
        tooltip.accept(Text.translatable("item.katanamod.diamond_katana.tooltip.soulgem"));
        tooltip.accept(Text.translatable("item.katanamod.diamond_katana.tooltip.element"));
    }
}
