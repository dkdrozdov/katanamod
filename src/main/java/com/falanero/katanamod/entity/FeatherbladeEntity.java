package com.falanero.katanamod.entity;

import com.falanero.katanamod.registry.enities.Instances;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.item.Items.FEATHER;

public class FeatherbladeEntity
        extends ThrownItemEntity {
    private static final TrackedData<Float> radius = DataTracker.registerData(FeatherbladeEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public FeatherbladeEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public FeatherbladeEntity(World world, LivingEntity owner) {
        super(Instances.FEATHERBLADE_ENTITY, owner, world);
    }

    public FeatherbladeEntity(World world, double x, double y, double z) {
        super(Instances.FEATHERBLADE_ENTITY, x, y, z, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(radius, 0f);
    }

    @Override
    protected Item getDefaultItem() {
        return FEATHER;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        entityHitResult.getEntity().damage(DamageSource.thrownProjectile(this, this.getOwner()), 0.0f);
    }

    public void setRadius(float newRadius) {
        getDataTracker().set(radius, newRadius);
    }

    public float getRadius() {
        float agePeriodTicks = 20f * 5f; // time for age-radius to get to 75%
        float maxAgeRadius = 5f;
        float ageRadius = maxAgeRadius * (float) age / (agePeriodTicks / 3f + (float) age);
        return getDataTracker().get(radius) + ageRadius;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        float r = getRadius();
        world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, 1.5F, 0.5F); // plays a globalSoundEvent
        areaDamage(getDamage() * 5);

        float velocityRate = 0.2f;
        int particleRate = 2;
        float particleStep = 1f / particleRate;

        for (int i = 0; i < particleRate; ++i) {
            float x = i * particleStep;
            for (int j = 0; j < particleRate; ++j) {
                float y = j * particleStep;
                for (int k = 0; k < particleRate; ++k) {
                    float z = k * particleStep;

                    Vec3d particlePos = new Vec3d(x, y, z).normalize().multiply(r);
                    this.world.addParticle(ParticleTypes.EXPLOSION,
                            this.getX() + particlePos.x,
                            this.getY() + particlePos.y,
                            this.getZ() + particlePos.z,
                            getVelocity().x * velocityRate,
                            getVelocity().y * velocityRate,
                            getVelocity().z * velocityRate);
                }
            }
        }
        if (!this.world.isClient && !this.isRemoved()) {
            age = getStallAgeTicks();
        }
    }

    private void tickMovement() {
        double speedX = getVelocity().getX();
        double speedZ = getVelocity().getZ();
        float radius = getRadius();
        float hoverMinHeight = 6f;

        BlockHitResult blockHitResult = world.raycast(new RaycastContext(
                getPos(),
                getPos().subtract(0, hoverMinHeight, 0),
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.ANY, this
        ));

        double groundDistance = blockHitResult.getPos().distanceTo(getPos());
        double heightFactor = (hoverMinHeight - groundDistance) / hoverMinHeight;

//        double horizontalSpeed = Math.sqrt(speedX*speedX + speedZ*speedZ);
        double verticalSpeed = getVelocity().getY();

        double maxHorizontalSpeed = 0.05f * MathHelper.sqrt(radius);
        double gravity = getGravity();

        double dragPeriod = 5;
        double dragShape = 3.5;
        double upwardDrag = gravity * dragPeriod / 10;
        double dragMultiplier = -Math.pow((Math.cos((float) age / dragPeriod) + 1) / 2, dragShape) + 0.8;
        double horizontalMultiplier = (-(-Math.pow((Math.cos(((float) age - 0.4 * MathHelper.PI * dragPeriod) / dragPeriod) + 1) / 2, dragShape) + 1) + 1) * dragPeriod;
        double newVerticalSpeed = (verticalSpeed + upwardDrag * dragMultiplier) * Math.max(1 - 0.075 * dragMultiplier - 0.45 * heightFactor, 0);

        Vec2f horizontalSpeed;
        if ((horizontalMultiplier * maxHorizontalSpeed) <= 0.01) {
            float newX = (MathHelper.nextFloat(this.random, -0.1f, 0.1f));
            float newZ = (MathHelper.nextFloat(this.random, -0.1f, 0.1f));
            horizontalSpeed = new Vec2f(newX, newZ);
        } else {
            horizontalSpeed = new Vec2f((float) speedX, (float) speedZ);
        }
        horizontalSpeed = horizontalSpeed.normalize().multiply((float) (horizontalMultiplier * maxHorizontalSpeed));

        setVelocity(horizontalSpeed.x, newVerticalSpeed, horizontalSpeed.y);

//        double newHorizontalSpeed = (horizontalSpeed + MathHelper.nextDouble())*(1 - 0.2 * dragMultiplier);
//        setVelocity(speedX, newVerticalSpeed, speedZ);

//        if (getOwner() instanceof PlayerEntity player && player.world.isClient()) {
//            player.sendMessage(Text.literal(
//                    " H: " + String.format("%.2f", radius)), true);
//        }
    }

    private void spawnSmallParticles() {
        float r = getRadius() - 1;
        float velocityRate = 0.2f;

        float x = MathHelper.nextFloat(random, -1, 1);
        float y = MathHelper.nextFloat(random, -1, 1);
        float z = MathHelper.nextFloat(random, -1, 1);

        Vec3d pos = getPos();
        Vec3d velocity = getVelocity().multiply(velocityRate);
        float deviation = MathHelper.nextFloat(random, -1, 1);

        float step = 2/r;
        for (int i = 0; i < r * r * r; i++) {
            x += step;
            y += (float) (int) (x/(1-step))*step;
            z += (float) (int) (y/(1-step))*step;

            x = (x + 1) % 2 - 1;
            y = (y + 1) % 2 - 1;
            z = (z + 1) % 2 - 1;

            deviation = MathHelper.clamp(deviation + (x+y+z) / 3f, -1, 1);
            Vec3d particlePos = new Vec3d(x, y, z).normalize().multiply(r+deviation);
            this.world.addParticle(ParticleTypes.SWEEP_ATTACK,
                    pos.x + particlePos.x,
                    pos.y + particlePos.y,
                    pos.z + particlePos.z,
                    velocity.x,
                    velocity.y,
                    velocity.z);
        }
//        for (int i = 0; i < particlesPerDimension; ++i) {
//            x = (x + 1 + (float) i / (float) particlesPerDimension) % 2 - 1;
//            for (int j = 0; j < particlesPerDimension; ++j) {
//                y = (y + 1 + 2 * (float) i / (float) particlesPerDimension) % 2 - 1;
//                for (int k = 0; k < particlesPerDimension; ++k) {
//                    z = (z + 1 + 3 * (float) i / (float) particlesPerDimension) % 2 - 1;
//                }

//        Entity entity = this.getOwner();
//        if (entity instanceof PlayerEntity player && player.world.isClient()) {
//            player.sendMessage(Text.literal(
//                    "V: " + String.format("%.2f", getRadius())), true);
//        }
//            }
//        }
    }

    private void spawnBigParticle() {
        float velocityRate = 0.8f;
        this.world.addParticle(ParticleTypes.EXPLOSION,
                this.getX(),
                this.getY(),
                this.getZ(),
                getVelocity().x * velocityRate,
                getVelocity().y * velocityRate,
                getVelocity().z * velocityRate);
    }

    private void tickParticles() {
        int mainParticleSpawnPeriodTicks = 8;
        int smallParticleSpawnPeriodTicks = 4;

        if (age % mainParticleSpawnPeriodTicks == 0) {
            spawnBigParticle();
        }
        if (age % smallParticleSpawnPeriodTicks == 0) {
            spawnSmallParticles();
        }
    }

    public float getDamage() {
        //TODO: scale
        return 1.0f;
    }

    private void spawnParticleDamaged(Vec3d damagedPos) {
        Vec3d pos = getPos();
        float radius = getRadius();
        float maxParticleVelocity = 0.7f;
        double dx = damagedPos.x - pos.x;
        double dy = damagedPos.y - pos.y;
        double dz = damagedPos.z - pos.z;
        Vec3d delta = new Vec3d(dx, dy, dz);
        double distance = delta.length();
        delta = delta.normalize().multiply((radius - distance + 1) * maxParticleVelocity / (radius + 1));

        this.world.addParticle(ParticleTypes.POOF,
                damagedPos.x, damagedPos.y, damagedPos.z,
                delta.x, delta.y, delta.z);
    }

    private void areaDamage(float damage) {
        float radius = getRadius();
        Vec3d pos = getPos();
        Entity owner = this.getOwner();

        int x1 = MathHelper.floor(pos.x - (double) radius);
        int x2 = MathHelper.floor(pos.x + (double) radius);
        int y1 = MathHelper.floor(pos.y - (double) radius);
        int y2 = MathHelper.floor(pos.y + (double) radius);
        int z1 = MathHelper.floor(pos.z - (double) radius);
        int z2 = MathHelper.floor(pos.z + (double) radius);
        List<Entity> list = world.getOtherEntities(owner, new Box(x1, y1, z1, x2, y2, z2));

        for (Entity entity : list) {
            if (entity instanceof LivingEntity livingEntity) {
                Vec3d damagedPos = new Vec3d(entity.getX(), entity.getEyeY(), entity.getZ());
                double dx = entity.getX() - pos.x;
                double dy = entity.getEyeY() - pos.y;
                double dz = entity.getZ() - pos.z;

                Vec3d delta = new Vec3d(dx, dy, dz);
                double distance = delta.length();

                if (distance <= radius) {
                    spawnParticleDamaged(damagedPos);
                    livingEntity.damage(DamageSource.player((PlayerEntity) owner), damage);
                }
            }
        }
    }

    private void tickDamage() {
        int damagePeriodTicks = 5;

        if (age % damagePeriodTicks == 0) {
            areaDamage(getDamage());
        }
    }

    private int getStallAgeTicks() {
        return 20 * 20;
    }

    private int getDeathAge() {
        return 20 * 22;
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        super.tick();

        if (age < getStallAgeTicks()) {
            tickMovement();
        }
        tickParticles();
        tickDamage();

        if ((age > getDeathAge()) && (!this.world.isClient) && (!this.isRemoved())) {
            this.discard();
        }
    }

    @Override
    @Nullable
    public Entity moveToWorld(ServerWorld destination) {
        Entity entity = this.getOwner();
        if (entity != null && entity.world.getRegistryKey() != destination.getRegistryKey()) {
            this.setOwner(null);
        }
        return super.moveToWorld(destination);
    }
}
