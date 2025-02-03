package dev.prangellplays.llgdragons.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface LLGDragonsSounds {
    Map<SoundEvent, Identifier> SOUND_EVENTS = new LinkedHashMap();
    SoundEvent NIGHTFURY_ROAR = createSoundEvent("nightfury_roar");

    static void init() {
        SOUND_EVENTS.keySet().forEach((soundEvent) -> {
            Registry.register(Registries.SOUND_EVENT, (Identifier)SOUND_EVENTS.get(soundEvent), soundEvent);
        });
    }

    private static SoundEvent createSoundEvent(String path) {
        SoundEvent soundEvent = SoundEvent.of(new Identifier("llgdragons", path));
        SOUND_EVENTS.put(soundEvent, new Identifier("llgdragons", path));
        return soundEvent;
    }
}
