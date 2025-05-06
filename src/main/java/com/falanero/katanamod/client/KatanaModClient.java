package com.falanero.katanamod.client;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.registry.IDs;
import com.falanero.katanamod.registry.enities.Instances;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import static com.falanero.katanamod.registry.IDs.SKYBOUND_S2C_PACKET_ID;
import static com.falanero.katanamod.registry.IDs.WINDBOMB_S2C_PACKET_ID;

public class KatanaModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Instances.FEATHERBLADE_ENTITY, FlyingItemEntityRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(SKYBOUND_S2C_PACKET_ID, (client, handler, buf, responseSender) -> {
            assert client.world != null;
            BlockPos blockPos = buf.readBlockPos();
//            double x = buf.readDouble();
//            double y = buf.readDouble();
//            double z = buf.readDouble();
            client.execute(()->{
//                assert client.player != null;
//                PlayerEntity player = client.player;
//                player.setVelocityClient(x,y,z);
//                player.velocityModified = true;
//                player.sendMessage(Text.literal(x + " " + y + " " + z), true);

                client.world.playSound(blockPos.getX(), blockPos.getY()+1, blockPos.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 4.0f, (1.0f + (client.world.random.nextFloat() - client.world.random.nextFloat()) * 0.2f) * 0.9f, false);
                client.world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX()-1, blockPos.getY()+1, blockPos.getZ()-1, 0.0, 1.2, 0.0);
                client.world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX()-1, blockPos.getY()+1, blockPos.getZ()+1, 0.0, 1.2, 0.0);
                client.world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX()+1, blockPos.getY()+1, blockPos.getZ()-1, 0.0, 1.2, 0.0);
                client.world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX()+1, blockPos.getY()+1, blockPos.getZ()+1, 0.0, 1.2, 0.0);
//                KatanaMod.LOGGER.info("SKYBOUND_S2C_PACKET_ID : {}, {}, {}", blockPos.getX(), blockPos.getY(), blockPos.getZ());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(WINDBOMB_S2C_PACKET_ID, (client, handler, buf, responseSender) -> {
            assert client.world != null;
            BlockPos blockPos = buf.readBlockPos();
            client.execute(()->{
//                client.world.playSound(blockPos.getX(), blockPos.getY()+1, blockPos.getZ(), SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.PLAYERS, 8.0f, (1.3f + (client.world.random.nextFloat() - client.world.random.nextFloat()) * 0.2f), false);
                client.world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX()-3, blockPos.getY()+4, blockPos.getZ()-3, -4.0, +4.0, -4.0);
                client.world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX()-3, blockPos.getY()-2, blockPos.getZ()-3, -4.0, -4.0, -4.0);
                client.world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX()-3, blockPos.getY()+4, blockPos.getZ()+3, -4.0, +4.0, +4.0);
                client.world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX()-3, blockPos.getY()-2, blockPos.getZ()+3, -4.0, -4.0, +4.0);
                client.world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX()+3, blockPos.getY()+4, blockPos.getZ()-3, +4.0, +4.0, -4.0);
                client.world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX()+3, blockPos.getY()-2, blockPos.getZ()-3, +4.0, -4.0, -4.0);
                client.world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX()+3, blockPos.getY()+4, blockPos.getZ()+3, +4.0, +4.0, +4.0);
                client.world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX()+3, blockPos.getY()-2, blockPos.getZ()+3, +4.0, -4.0, +4.0);
            });
        });
    }
}

