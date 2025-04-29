package dev.prangellplays.llgdragons.network;

import dev.prangellplays.llgdragons.entity.DragonEntity;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;

@Environment(EnvType.CLIENT)
public class KeyInputPacket {
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client ->{
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null)
                if (player.getVehicle() instanceof DragonEntity dragon) {
                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                    buf.writeBoolean(dragon.isSecondaryAttackPressed); //4 - melee attack
                    buf.writeBoolean(dragon.isPrimaryAttackPressed); //5 - range attack
                    buf.writeInt(dragon.getId());
                    ClientPlayNetworking.send(KeyInputC2SPacket.KEY_INPUT_PACKET, buf);
                }
        });
    }
}