package dev.prangellplays.llgdragons.client.sound;

import dev.prangellplays.llgdragons.entity.DragonEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class DragonSoundHandler {
    private static final Map<UUID, DragonDiveSoundInstance> activeSounds = new HashMap<>();

    public static void onClientTick(MinecraftClient client) {
        if (client.world == null || client.player == null) return;

        for (Entity entity : client.world.getEntities()) {
            if (entity instanceof DragonEntity dragon && dragon.isClientBoosting() && dragon.getVelocity().y < -0.1) {
                UUID id = dragon.getUuid();
                if (!activeSounds.containsKey(id) || activeSounds.get(id).isDone()) {
                    DragonDiveSoundInstance sound = new DragonDiveSoundInstance(dragon);
                    client.getSoundManager().play(sound);
                    activeSounds.put(id, sound);
                }
            }
        }

        activeSounds.entrySet().removeIf(e -> e.getValue().isDone());
    }
}