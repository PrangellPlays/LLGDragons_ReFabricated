package dev.prangellplays.llgdragons.data;

import dev.prangellplays.llgdragons.data.nightfury.eggcombination.NightfuryEggCombination;
import dev.prangellplays.llgdragons.data.nightfury.variant.NightfuryVariant;
import net.minecraft.util.Identifier;

import java.util.List;

public class DragonEggCombinationBuilder {
    private final Identifier parent1;
    private final Identifier parent2;
    private final List<Identifier> resultEggs;
    private float chance = 1.0f;

    public DragonEggCombinationBuilder(Identifier parent1, Identifier parent2, List<Identifier> resultEggs) {
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.resultEggs = resultEggs;
    }

    public DragonEggCombinationBuilder chance(float chance) {
        this.chance = chance;
        return this;
    }

    public NightfuryEggCombination buildNightfury() {
        return new NightfuryEggCombination(parent1, parent2, resultEggs, chance);
    }

    private static <T> java.util.Optional<T> empty() {
        return java.util.Optional.empty();
    }

    private static <T> java.util.Optional<T> of(T value) {
        return java.util.Optional.of(value);
    }
}