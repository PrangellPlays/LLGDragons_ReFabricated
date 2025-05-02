package dev.prangellplays.llgdragons.data;

import dev.prangellplays.llgdragons.data.nightfury.NightfuryVariant;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class DragonVariantBuilder {
    private final EntityType<?> entity;
    private final Text name;
    private final Identifier texture;

    public DragonVariantBuilder(EntityType<?> entity, String name, Identifier texture) {
        this(entity, Text.of(name), texture);
    }

    public DragonVariantBuilder(EntityType<?> entity, Text name, Identifier texture) {
        this.entity = entity;
        this.name = name;
        this.texture = texture;
    }

    public NightfuryVariant buildNightfury() {
        return new NightfuryVariant(entity, name, texture);
    }

    /*public NightfuryVariant build() {
        return new NightfuryVariant(entity, name, texture);
    }*/

    private static<T> Optional<T> empty() { //easier to type faster
        return Optional.empty();
    }

    private static<T> Optional<T> of(T value) { //easier to type faster
        return Optional.of(value);
    }
}
