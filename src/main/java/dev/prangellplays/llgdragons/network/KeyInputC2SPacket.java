package dev.prangellplays.llgdragons.network;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.DragonEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class KeyInputC2SPacket {
    public static final Identifier KEY_INPUT_PACKET = new Identifier(LLGDragons.MOD_ID, "key_input");
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(KEY_INPUT_PACKET, (server, player, handler, buffer, sender) -> {
            boolean isSecondaryAttackPressed = buffer.readBoolean();
            boolean isPrimaryAttackPressed = buffer.readBoolean();
            LivingEntity entity = (LivingEntity) player.getServerWorld().getEntityById(buffer.readInt());
            if (entity instanceof DragonEntity dragon) {
                dragon.isSecondaryAttackPressed = isSecondaryAttackPressed;
                dragon.isPrimaryAttackPressed = isPrimaryAttackPressed;

                dragon.updateInputs();
            }
        });
    }
}