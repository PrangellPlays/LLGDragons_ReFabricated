package dev.prangellplays.llgdragons.data.nightfury.eggcombination;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.data.DragonEggCombinationBuilder;
import dev.prangellplays.llgdragons.data.nightfury.variant.NightfuryVariant;
import dev.prangellplays.llgdragons.data.nightfury.variant.NightfuryVariants;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.NightfuryEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NightfuryEggCombinations {
    public static final RegistryKey<NightfuryEggCombination> NIGHTFURY_COMBINATION = of("nightfury");
    public static final RegistryKey<NightfuryEggCombination> SPARKLE_1_COMBINATION = of("sparkle_1");
    public static final RegistryKey<NightfuryEggCombination> SPARKLE_2_COMBINATION = of("sparkle_2");

    public static void bootstrap(Registerable<NightfuryEggCombination> registerable) {
        registerable.register(NIGHTFURY_COMBINATION, new DragonEggCombinationBuilder(
                NightfuryVariants.NIGHTFURY.getValue(),
                NightfuryVariants.NIGHTFURY.getValue(),
                List.of(NightfuryVariants.NIGHTFURY.getValue())
        ).buildNightfury());

        registerable.register(SPARKLE_1_COMBINATION, new DragonEggCombinationBuilder(
                NightfuryVariants.GALAXY_NIGHTFURY.getValue(),
                NightfuryVariants.WHITE_NIGHTFURY.getValue(),
                List.of(NightfuryVariants.BLACK_SPARKLES.getValue(), NightfuryVariants.PINK_SPARKLES.getValue())
        ).chance(1.0f).buildNightfury());

        registerable.register(SPARKLE_2_COMBINATION, new DragonEggCombinationBuilder(
                NightfuryVariants.WHITE_NIGHTFURY.getValue(),
                NightfuryVariants.GALAXY_NIGHTFURY.getValue(),
                List.of(NightfuryVariants.BLACK_SPARKLES.getValue(), NightfuryVariants.PINK_SPARKLES.getValue())
        ).chance(1.0f).buildNightfury());
    }

    public static RegistryKey<NightfuryEggCombination> of(String path) {
        return RegistryKey.of(LLGDragons.NIGHTFURY_EGG_COMBINATION_KEY, Identifier.of(LLGDragons.MOD_ID, path));
    }

    public static NightfuryEggCombination getMatchingCombination(DynamicRegistryManager registryManager, RegistryEntry<NightfuryVariant> parent1, RegistryEntry<NightfuryVariant> parent2) {
        Registry<NightfuryEggCombination> registry = registryManager.get(LLGDragons.NIGHTFURY_EGG_COMBINATION_KEY);

        for (NightfuryEggCombination combo : registry) {
            if (combo.matches(parent1, parent2)) {
                return combo;
            }
        }

        return null;
    }
}
