package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.callback.*;
import com.falanero.katanamod.registry.Instances;
import com.falanero.katanamod.util.ability.diamond.SkyboundDiamondAbility;
import com.falanero.katanamod.util.ability.diamond.SwiftnessDiamondAbility;
import com.falanero.katanamod.util.Nbt;
import com.falanero.katanamod.util.Souls;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class DiamondKatanaItem extends KatanaItem {
    public DiamondKatanaItem(int attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, settings);
        ToolBreakCallback.EVENT.register((entity) -> onKatanaBreak(entity, Instances.DIAMOND_SOULGEM, true));
        OnKilledByCallback.EVENT.register(this::onKilledEntity);
        PlayerEntityTickCallback.EVENT.register(this::updateEffect);
        OnAttackCallback.ON_SWEEPING_ATTACK_CALLBACK_EVENT.register(this::sweepingAttack);
        OnAttackCallback.ON_CRIT_ATTACK_CALLBACK_EVENT.register(this::critAttack);
        OnAttackCallback.ON_SPRINT_ATTACK_CALLBACK_EVENT.register(this::sprintAttack);
        OnItemUseCallback.ON_ITEM_USE_CALLBACK.register(this::onItemUse);
        OnGetAirStrafingSpeedCallback.ON_GET_AIR_STRAFING_SPEED_CALLBACK_EVENT.register(SwiftnessDiamondAbility::onGetAirStrafingSpeed);
    }

    private ActionResult onItemUse(World world, PlayerEntity user, Hand hand){
        KatanaMod.LOGGER.info((user.world.isClient()?"CLIENT":"SERVER") + " " + hand.toString() + user.getStackInHand(hand).getItem().getName());
        Hand otherHand = hand == Hand.MAIN_HAND?Hand.OFF_HAND:Hand.MAIN_HAND;
        if(user.getStackInHand(otherHand).getItem() instanceof DiamondKatanaItem){
            Vec3d pos = user.getPos().add(0, -4, 0);
                float power = 10.0f;

                float radius = power * 2.0f;
                int x1 = MathHelper.floor(pos.x - (double)radius - 1.0);
                int x2 = MathHelper.floor(pos.x + (double)radius + 1.0);
                int y1 = MathHelper.floor(pos.y - (double)radius - 1.0);
                int y2 = MathHelper.floor(pos.y + (double)radius + 1.0);
                int z1 = MathHelper.floor(pos.z - (double)radius - 1.0);
                int z2 = MathHelper.floor(pos.z + (double)radius + 1.0);
                List<Entity> list = world.getOtherEntities(null, new Box(x1, y1, z1, x2, y2, z2));

                for (Entity entity : list) {
                    double dx = entity.getX() - pos.x;
                    double dy = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - pos.y;
                    double dz = entity.getZ() - pos.z;
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    double normalDistance = distance / (double) radius;
                    if (entity.isImmuneToExplosion() || !(normalDistance <= 1.0) || distance == 0.0)
                        continue;
                    double normalDx = dx/distance;
                    double normalDy = dy/distance;
                    double normalDz = dz/distance;
                    double velocityMultiplier = (1.0 - normalDistance);
                    if (entity instanceof LivingEntity) {
                        velocityMultiplier = ProtectionEnchantment.transformExplosionKnockback((LivingEntity) entity, velocityMultiplier);
                    }
                    entity.setVelocity(
                            entity.getVelocity().x + normalDx * velocityMultiplier,
                            normalDy * velocityMultiplier,
                            entity.getVelocity().z + normalDz * velocityMultiplier);
                }

            if(world.isClient){
                world.playSound(pos.x, pos.y, pos.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0f, (1.0f + (world.random.nextFloat() - world.random.nextFloat()) * 0.2f) * 0.7f, false);
            }
            world.addParticle(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 1.0, 0.0, 0.0);
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }


    private void sweepingAttack(Entity target, PlayerEntity player){
        if(player == null)
            return;
        ItemStack stack = player.getMainHandStack();
        if((stack.getItem() instanceof DiamondKatanaItem) && !player.world.isClient)
        {
            KatanaMod.LOGGER.info("It's a sweep attack!!!");
        }
    }
    private void critAttack(Entity target, PlayerEntity player){
        if(player == null)
            return;
        ItemStack stack = player.getMainHandStack();
        if((stack.getItem() instanceof DiamondKatanaItem) && !player.world.isClient) {
            KatanaMod.LOGGER.info("It's a crit attack!!!");
        }
    }
    private void sprintAttack(Entity target, PlayerEntity player){
        if(player == null)
            return;
        ItemStack stack = player.getMainHandStack();
        if((stack.getItem() instanceof DiamondKatanaItem) && !player.world.isClient) {
            KatanaMod.LOGGER.info("It's a sprint attack!!!");
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        user.getItemCooldownManager().set(this, 20*5);

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if((target == null) || (attacker == null) || (stack == null))
            return false;

        int level = Souls.getCurrentLevel(Nbt.getSoulCount(stack));
        SkyboundDiamondAbility.tryApply(stack, target, attacker, level);
        return super.postHit(stack, target, attacker);
    }

    @Override
    protected void updateEffect(PlayerEntity player) {
        if(player == null)
            return;

        if(player.world.isClient()){
            double x = player.getVelocity().x;
            double z = player.getVelocity().z;
            float hv = (float)Math.sqrt(x*x+z*z);
            player.sendMessage(Text.literal("horizontal velocity: " + String.format("%.2f", hv)), true);
        }
        //- : 0.20 vs 0.12       //  1.67
        //1 : 1.19 vs 0.14       //  8.5
        //2 : 1.39 vs 0.17       //  8.17
        //3 : 1.59 vs 0.19 1.1438//  8.36
        //4 : 1.78 vs 0.21       //  8.47
        //5 : 1.98 vs 0.23 1.2452//  8.25
        //6 : 2.18 vs 0.26       //  8.38
        //7 : 2.38 vs 0.27 1.2020//  8.5
        //8 : 2.58 vs 0.31       //  8.32
        //9 : 2.77 vs 0.33       //  8.39
        //10: 2.97 vs 0.35 1.2478//  8.48571429

        ItemStack stack = player.getMainHandStack();
        if(stack == null)
            return;

        if((stack.getItem() instanceof DiamondKatanaItem)) {
            int level = Souls.getCurrentLevel(Nbt.getSoulCount(stack));
            SwiftnessDiamondAbility.effectTick(player, level);
        }
    }

    @Override
    protected void onKatanaBreak(LivingEntity entity, Item droppedMaterial, boolean preserveNbt)
    {
        ItemStack stack = entity.getStackInHand(Hand.MAIN_HAND);
        if((stack.getItem() instanceof DiamondKatanaItem) && !entity.world.isClient){
            super.onKatanaBreak(entity, droppedMaterial, preserveNbt);
        }
    }

    @Override
    protected void onKilledEntity(LivingEntity adversary){
        if(adversary == null)
            return;
        ItemStack stack = adversary.getStackInHand(Hand.MAIN_HAND);
        if((stack.getItem() instanceof DiamondKatanaItem) && !adversary.world.isClient){
            super.onKilledEntity(adversary);
        }
    }

    public void appendTooltipExtra(Pair<ItemStack, List<Text>> callbackContext){
        Souls.appendTooltipExtraDiamond(callbackContext);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if(Screen.hasShiftDown()){
            appendTooltipExtra(new Pair<>(itemStack, tooltip));
        }else{
            //Katana description
            super.appendTooltip(itemStack, world, tooltip, tooltipContext);
            //Inlaid katana description
            tooltip.add(Text.translatable("item.katanamod.diamond_katana.tooltip.soulgem"));
            tooltip.add(Text.translatable("item.katanamod.diamond_katana.tooltip.element"));
            //Soul count
            super.appendTooltipSoulCount(itemStack, tooltip);
            //More info
            tooltip.add(Text.translatable("item.katanamod.tooltip_display_more_info").formatted(Formatting.GRAY));
        }
    }

}
