package dev.prangellplays.llgdragons.data.datagen;

import dev.prangellplays.llgdragons.init.LLGDragonsItems;
import dev.prangellplays.llgdragons.util.LLGDragonsTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class LLGDragonsItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public LLGDragonsItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(LLGDragonsTags.Items.FETCH_BALL)
                .add(LLGDragonsItems.FETCH_BALL);

        getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(LLGDragonsItems.GRONCKLE_IRON_SWORD);
        getOrCreateTagBuilder(ItemTags.PICKAXES)
                .add(LLGDragonsItems.GRONCKLE_IRON_PICKAXE);
        getOrCreateTagBuilder(ItemTags.SHOVELS)
                .add(LLGDragonsItems.GRONCKLE_IRON_SHOVEL);
        getOrCreateTagBuilder(ItemTags.AXES)
                .add(LLGDragonsItems.GRONCKLE_IRON_AXE);
        getOrCreateTagBuilder(ItemTags.HOES)
                .add(LLGDragonsItems.GRONCKLE_IRON_HOE);

        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(LLGDragonsItems.GRONCKLE_IRON_HELMET)
                .add(LLGDragonsItems.GRONCKLE_IRON_CHESTPLATE)
                .add(LLGDragonsItems.GRONCKLE_IRON_LEGGINGS)
                .add(LLGDragonsItems.GRONCKLE_IRON_BOOTS);
    }
}
