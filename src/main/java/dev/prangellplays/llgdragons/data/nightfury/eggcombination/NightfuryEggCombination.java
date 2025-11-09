package dev.prangellplays.llgdragons.data.nightfury.eggcombination;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.data.DragonEggCombinationBuilder;
import dev.prangellplays.llgdragons.data.DragonVariantBuilder;
import dev.prangellplays.llgdragons.data.nightfury.variant.NightfuryVariant;
import dev.prangellplays.llgdragons.data.nightfury.variant.NightfuryVariants;
import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class NightfuryEggCombination {
    public static final Codec<NightfuryEggCombination> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("parent1").forGetter(comb -> comb.parent1),
            Identifier.CODEC.fieldOf("parent2").forGetter(comb -> comb.parent2),
            Identifier.CODEC.listOf().fieldOf("result_eggs").forGetter(comb -> comb.resultEggs),
            Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter(comb -> comb.chance)
    ).apply(instance, NightfuryEggCombination::new));

    public static final Codec<RegistryEntry<NightfuryEggCombination>> ENTRY_CODEC = RegistryElementCodec.of(LLGDragons.NIGHTFURY_EGG_COMBINATION_KEY, CODEC);

    public final Identifier parent1;
    public final Identifier parent2;
    public final List<Identifier> resultEggs;
    public final float chance;

    public NightfuryEggCombination(Identifier parent1, Identifier parent2, List<Identifier> resultEggs, float chance) {
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.resultEggs = resultEggs;
        this.chance = chance;
    }

    public static NightfuryEggCombination NIGHTFURY = new DragonEggCombinationBuilder(
            NightfuryVariants.NIGHTFURY.getValue(),
            NightfuryVariants.NIGHTFURY.getValue(),
            List.of(NightfuryVariants.NIGHTFURY.getValue())
    ).buildNightfury();

    public static NightfuryEggCombination fromId(DynamicRegistryManager registryManager, int id) {
        Optional<RegistryEntry.Reference<NightfuryEggCombination>> holder =
                registryManager.get(LLGDragons.NIGHTFURY_EGG_COMBINATION_KEY).getEntry(id);
        return holder.map(RegistryEntry.Reference::value).orElseGet(() -> NIGHTFURY);
    }

    public static int toId(DynamicRegistryManager registryManager, NightfuryEggCombination combination) {
        return registryManager.get(LLGDragons.NIGHTFURY_EGG_COMBINATION_KEY).getRawId(combination);
    }

    public static NightfuryEggCombination fromNbt(DynamicRegistryManager registryManager, NbtCompound nbt) {
        AtomicReference<NightfuryEggCombination> result = new AtomicReference<>();
        Optional.ofNullable(Identifier.tryParse(nbt.getString("combination")))
                .map(id -> RegistryKey.of(LLGDragons.NIGHTFURY_EGG_COMBINATION_KEY, id))
                .flatMap(key -> registryManager.get(LLGDragons.NIGHTFURY_EGG_COMBINATION_KEY).getEntry(key))
                .ifPresent(entry -> result.set(entry.value()));
        return result.get();
    }

    public Identifier getParent1() {
        return parent1;
    }

    public Identifier getParent2() {
        return parent2;
    }

    public List<Identifier> getResultEggs() {
        return resultEggs;
    }

    public float getChance() {
        return chance;
    }

    public boolean matches(RegistryEntry<NightfuryVariant> parent1, RegistryEntry<NightfuryVariant> parent2) {
        return (this.parent1.equals(parent1) && this.parent2.equals(parent2)) ||
                (this.parent1.equals(parent2) && this.parent2.equals(parent1));
    }

    public int getOffspringCount() {
        return this.resultEggs.size();
    }
}
