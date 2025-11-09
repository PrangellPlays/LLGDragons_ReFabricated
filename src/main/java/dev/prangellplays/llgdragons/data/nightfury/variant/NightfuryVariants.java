package dev.prangellplays.llgdragons.data.nightfury.variant;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.data.DragonVariantBuilder;
import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NightfuryVariants {
    public static final RegistryKey<NightfuryVariant> NIGHTFURY = of("nightfury");
    public static final RegistryKey<NightfuryVariant> ALBINO_NIGHTFURY = of("albino_nightfury");
    public static final RegistryKey<NightfuryVariant> WHITE_NIGHTFURY = of("white_nightfury");
    public static final RegistryKey<NightfuryVariant> GALAXY_NIGHTFURY = of("galaxy_nightfury");
    public static final RegistryKey<NightfuryVariant> BLACK_SPARKLES = of("black_sparkles");
    public static final RegistryKey<NightfuryVariant> PINK_SPARKLES = of("pink_sparkles");
    public static final RegistryKey<NightfuryVariant> HTTYD_NIGHTFURY = of("httyd_nightfury");
    public static final RegistryKey<NightfuryVariant> TOOTHLESS_HTTYD1 = of("toothless_httyd1");
    public static final RegistryKey<NightfuryVariant> TOOTHLESS_HTTYD2 = of("toothless_httyd2");

    public static void bootstrap(Registerable<NightfuryVariant> registerable) {
        registerable.register(NIGHTFURY, NightfuryVariant.NIGHTFURY);

        registerable.register(ALBINO_NIGHTFURY, new DragonVariantBuilder(
                LLGDragonsEntities.NIGHTFURY,
                Text.translatable("llgdragons.variant.nightfury.albino_nightfury"),
                Identifier.of(LLGDragons.MOD_ID, "textures/entity/nightfury/albino_nightfury.png"))
                .buildNightfury()
        );

        registerable.register(WHITE_NIGHTFURY, new DragonVariantBuilder(
                LLGDragonsEntities.NIGHTFURY,
                Text.translatable("llgdragons.variant.nightfury.white_nightfury"),
                Identifier.of(LLGDragons.MOD_ID, "textures/entity/nightfury/white_nightfury.png"))
                .buildNightfury()
        );

        registerable.register(GALAXY_NIGHTFURY, new DragonVariantBuilder(
                LLGDragonsEntities.NIGHTFURY,
                Text.translatable("llgdragons.variant.nightfury.galaxy_nightfury"),
                Identifier.of(LLGDragons.MOD_ID, "textures/entity/nightfury/galaxy_nightfury.png"))
                .buildNightfury()
        );

        registerable.register(BLACK_SPARKLES, new DragonVariantBuilder(
                LLGDragonsEntities.NIGHTFURY,
                Text.translatable("llgdragons.variant.nightfury.black_sparkles"),
                Identifier.of(LLGDragons.MOD_ID, "textures/entity/nightfury/black_sparkles.png"))
                .buildNightfury()
        );

        registerable.register(PINK_SPARKLES, new DragonVariantBuilder(
                LLGDragonsEntities.NIGHTFURY,
                Text.translatable("llgdragons.variant.nightfury.pink_sparkles"),
                Identifier.of(LLGDragons.MOD_ID, "textures/entity/nightfury/pink_sparkles.png"))
                .buildNightfury()
        );

        registerable.register(HTTYD_NIGHTFURY, new DragonVariantBuilder(
                LLGDragonsEntities.NIGHTFURY,
                Text.translatable("llgdragons.variant.nightfury.httyd_nightfury"),
                Identifier.of(LLGDragons.MOD_ID, "textures/entity/nightfury/httyd_nightfury.png"))
                .buildNightfury()
        );

        registerable.register(TOOTHLESS_HTTYD1, new DragonVariantBuilder(
                LLGDragonsEntities.NIGHTFURY,
                Text.translatable("llgdragons.variant.nightfury.toothless_httyd1"),
                Identifier.of(LLGDragons.MOD_ID, "textures/entity/nightfury/toothless_httyd1.png"))
                .buildNightfury()
        );

        registerable.register(TOOTHLESS_HTTYD2, new DragonVariantBuilder(
                LLGDragonsEntities.NIGHTFURY,
                Text.translatable("llgdragons.variant.nightfury.toothless_httyd2"),
                Identifier.of(LLGDragons.MOD_ID, "textures/entity/nightfury/toothless_httyd2.png"))
                .buildNightfury()
        );
    }

    public static RegistryKey<NightfuryVariant> of(String path) {
        return RegistryKey.of(LLGDragons.NIGHTFURY_VARIANT_KEY, Identifier.of(LLGDragons.MOD_ID, path));
    }
}
