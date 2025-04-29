package dev.prangellplays.llgdragons.network;

import dev.prangellplays.llgdragons.entity.DragonEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class KeyInputSyncPacket {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(KeyInputSyncS2CPacket.KEY_INPUT_SYNC_PACKET, (client, handler, buffer, sender) -> {
            boolean isMeleeAttackPressed = buffer.readBoolean();
            boolean isRangeAttackPressed = buffer.readBoolean();
            DragonEntity dragon = client.world != null ? (DragonEntity) client.world.getEntityById(buffer.readInt()) : null;
            client.execute(() ->{
                if (dragon != null && dragon.getControllingPassenger() != client.player) {
                    dragon.isSecondaryAttackPressed = isMeleeAttackPressed;
                    dragon.isPrimaryAttackPressed = isRangeAttackPressed;

                    dragon.updateInputs();
                }
            });
        });
    }
}