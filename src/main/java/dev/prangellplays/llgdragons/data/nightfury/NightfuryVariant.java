package dev.prangellplays.llgdragons.data.nightfury;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.data.DragonVariantBuilder;
import dev.prangellplays.llgdragons.init.LLGDragonsEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class NightfuryVariant {
    public static final Codec<Text> TEXT_CODEC = Codec.STRING.xmap(
            json -> Text.Serializer.fromJson(json),
            text -> Text.Serializer.toJson(text)
    );
    public static final Codec<EntityType<?>> ENTITY_CODEC = Registries.ENTITY_TYPE.getCodec();

    public static final Codec<NightfuryVariant> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ENTITY_CODEC.fieldOf("entity").forGetter(dragon -> dragon.entity),
                    TEXT_CODEC.fieldOf("name").forGetter(dragon -> dragon.name),
                    Identifier.CODEC.fieldOf("texture").forGetter(dragon -> dragon.texture)
            ).apply(instance, NightfuryVariant::new)
    );

    public static final Codec<RegistryEntry<NightfuryVariant>> ENTRY_CODEC = RegistryElementCodec.of(LLGDragons.NIGHTFURY_VARIANT_KEY, CODEC);

    public final EntityType<?> entity;
    public final Text name;
    public final Identifier texture;

    public NightfuryVariant(EntityType<?> entity, Text name, Identifier texture) {
        this.entity = entity;
        this.name = name;
        this.texture = texture;
    }

    public static NightfuryVariant NIGHTFURY = new DragonVariantBuilder(
            LLGDragonsEntities.NIGHTFURY,
            Text.translatable("llgdragons.variant.nightfury.nightfury"),
            Identifier.of(LLGDragons.MOD_ID, "textures/entity/nightfury/nightfury.png"))
            .buildNightfury();

    public static NightfuryVariant fromId(DynamicRegistryManager registryManager, int id) {
        Optional<RegistryEntry.Reference<NightfuryVariant>> holder = registryManager.get(LLGDragons.NIGHTFURY_VARIANT_KEY).getEntry(id);
        return holder.map(RegistryEntry.Reference::value).orElseGet(() -> NIGHTFURY);
    }

    public static int toId(DynamicRegistryManager registryManager, NightfuryVariant variant) {
        return registryManager.get(LLGDragons.NIGHTFURY_VARIANT_KEY).getRawId(variant);
    }

    public static NightfuryVariant fromNbt(DynamicRegistryManager registryManager, NbtCompound nbt) {
        AtomicReference<NightfuryVariant> returnVariant = new AtomicReference<>();
        Optional.ofNullable(Identifier.tryParse(nbt.getString("variant")))
                .map(id -> RegistryKey.of(LLGDragons.NIGHTFURY_VARIANT_KEY, id))
                .flatMap(variantKey -> registryManager.get(LLGDragons.NIGHTFURY_VARIANT_KEY).getEntry(variantKey))
                .ifPresent(dragonVariantReference -> returnVariant.set(dragonVariantReference.value()));
        return returnVariant.get();
    }

    public EntityType<?> getEntity() {
        return entity;
    }

    public Text getName() {
        return name;
    }

    public Identifier getTexture() {
        return texture;
    }
}
