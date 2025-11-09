package dev.prangellplays.llgdragons.data.datagen;

import dev.prangellplays.llgdragons.init.LLGDragonsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class LLGDragonsRecipeProvider extends FabricRecipeProvider {
    public LLGDragonsRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        //Gronckle Iron Armour
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, LLGDragonsItems.GRONCKLE_IRON_HELMET, 1)
                .pattern("GGG")
                .pattern("G G")
                .input('G', LLGDragonsItems.GRONCKLE_IRON_INGOT)
                .criterion(hasItem(LLGDragonsItems.GRONCKLE_IRON_INGOT), conditionsFromItem(LLGDragonsItems.GRONCKLE_IRON_INGOT))
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(LLGDragonsItems.GRONCKLE_IRON_HELMET)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, LLGDragonsItems.GRONCKLE_IRON_CHESTPLATE, 1)
                .pattern("G G")
                .pattern("GGG")
                .pattern("GGG")
                .input('G', LLGDragonsItems.GRONCKLE_IRON_INGOT)
                .criterion(hasItem(LLGDragonsItems.GRONCKLE_IRON_INGOT), conditionsFromItem(LLGDragonsItems.GRONCKLE_IRON_INGOT))
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(LLGDragonsItems.GRONCKLE_IRON_CHESTPLATE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, LLGDragonsItems.GRONCKLE_IRON_LEGGINGS, 1)
                .pattern("GGG")
                .pattern("G G")
                .pattern("G G")
                .input('G', LLGDragonsItems.GRONCKLE_IRON_INGOT)
                .criterion(hasItem(LLGDragonsItems.GRONCKLE_IRON_INGOT), conditionsFromItem(LLGDragonsItems.GRONCKLE_IRON_INGOT))
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(LLGDragonsItems.GRONCKLE_IRON_LEGGINGS)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, LLGDragonsItems.GRONCKLE_IRON_BOOTS, 1)
                .pattern("G G")
                .pattern("G G")
                .input('G', LLGDragonsItems.GRONCKLE_IRON_INGOT)
                .criterion(hasItem(LLGDragonsItems.GRONCKLE_IRON_INGOT), conditionsFromItem(LLGDragonsItems.GRONCKLE_IRON_INGOT))
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(LLGDragonsItems.GRONCKLE_IRON_BOOTS)));

        //Gronckle Iron Tools
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, LLGDragonsItems.GRONCKLE_IRON_AXE, 1)
                .pattern("GG")
                .pattern("GS")
                .pattern(" S")
                .input('G', LLGDragonsItems.GRONCKLE_IRON_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(LLGDragonsItems.GRONCKLE_IRON_INGOT), conditionsFromItem(LLGDragonsItems.GRONCKLE_IRON_INGOT))
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(LLGDragonsItems.GRONCKLE_IRON_AXE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, LLGDragonsItems.GRONCKLE_IRON_HOE, 1)
                .pattern("GG")
                .pattern(" S")
                .pattern(" S")
                .input('G', LLGDragonsItems.GRONCKLE_IRON_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(LLGDragonsItems.GRONCKLE_IRON_INGOT), conditionsFromItem(LLGDragonsItems.GRONCKLE_IRON_INGOT))
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(LLGDragonsItems.GRONCKLE_IRON_HOE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, LLGDragonsItems.GRONCKLE_IRON_PICKAXE, 1)
                .pattern("GGG")
                .pattern(" S ")
                .pattern(" S ")
                .input('G', LLGDragonsItems.GRONCKLE_IRON_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(LLGDragonsItems.GRONCKLE_IRON_INGOT), conditionsFromItem(LLGDragonsItems.GRONCKLE_IRON_INGOT))
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(LLGDragonsItems.GRONCKLE_IRON_PICKAXE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, LLGDragonsItems.GRONCKLE_IRON_SHOVEL, 1)
                .pattern("G")
                .pattern("S")
                .pattern("S")
                .input('G', LLGDragonsItems.GRONCKLE_IRON_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(LLGDragonsItems.GRONCKLE_IRON_INGOT), conditionsFromItem(LLGDragonsItems.GRONCKLE_IRON_INGOT))
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(LLGDragonsItems.GRONCKLE_IRON_SHOVEL)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, LLGDragonsItems.GRONCKLE_IRON_SWORD, 1)
                .pattern("G")
                .pattern("G")
                .pattern("S")
                .input('G', LLGDragonsItems.GRONCKLE_IRON_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(LLGDragonsItems.GRONCKLE_IRON_INGOT), conditionsFromItem(LLGDragonsItems.GRONCKLE_IRON_INGOT))
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(LLGDragonsItems.GRONCKLE_IRON_SWORD)));


        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Items.SADDLE, 1)
                .pattern("LLL")
                .pattern("LIL")
                .pattern("I I")
                .input('L', Items.LEATHER)
                .input('I', Items.IRON_INGOT)
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.SADDLE)));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, LLGDragonsItems.SCROLL, 1)
                .input(Items.PAPER)
                .input(Items.PAPER)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .offerTo(exporter, new Identifier(getRecipeName(LLGDragonsItems.SCROLL)));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, LLGDragonsItems.TELLING_BOOK, 1)
                .input(Items.BOOK)
                .input(Items.NAME_TAG)
                .input(LLGDragonsItems.SCROLL)
                .criterion(hasItem(Items.BOOK), conditionsFromItem(Items.BOOK))
                .criterion(hasItem(Items.NAME_TAG), conditionsFromItem(Items.NAME_TAG))
                .criterion(hasItem(LLGDragonsItems.SCROLL), conditionsFromItem(LLGDragonsItems.SCROLL))
                .offerTo(exporter, new Identifier(getRecipeName(LLGDragonsItems.TELLING_BOOK)));
    }
}
