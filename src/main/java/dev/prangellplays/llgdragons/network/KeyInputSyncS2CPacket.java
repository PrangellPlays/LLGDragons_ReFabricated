package dev.prangellplays.llgdragons.network;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.DragonEntity;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class KeyInputSyncS2CPacket {
    public static final Identifier KEY_INPUT_SYNC_PACKET = new Identifier(LLGDragons.MOD_ID, "key_input_sync_packet");

    public static void send(ServerPlayerEntity player, DragonEntity dragon) {
        if (dragon.hasControllingPassenger()) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBoolean(dragon.isSecondaryAttackPressed); //4 - melee attack
            buf.writeBoolean(dragon.isPrimaryAttackPressed); //5 - range attack

            buf.writeInt(dragon.getId());
            ServerPlayNetworking.send(player, KEY_INPUT_SYNC_PACKET, buf);
        }
    }
}