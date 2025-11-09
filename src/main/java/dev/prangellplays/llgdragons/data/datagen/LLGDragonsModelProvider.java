package dev.prangellplays.llgdragons.data.datagen;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.init.LLGDragonsBlocks;
import dev.prangellplays.llgdragons.init.LLGDragonsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.ArmorItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class LLGDragonsModelProvider extends FabricModelProvider {
    private final FabricDataOutput output;

    public LLGDragonsModelProvider(FabricDataOutput output) {
        super(output);
        this.output = output;
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        /*registerDragonHead(blockStateModelGenerator, LLGDragonsBlocks.NIGHTFURY_HEAD, "nightfury_head");
        registerDragonHead(blockStateModelGenerator, LLGDragonsBlocks.HTTYD_NIGHTFURY_HEAD, "httyd_nightfury_head");
        registerDragonHead(blockStateModelGenerator, LLGDragonsBlocks.WHITE_NIGHTFURY_HEAD, "white_nightfury_head");
        registerDragonHead(blockStateModelGenerator, LLGDragonsBlocks.ALBINO_NIGHTFURY_HEAD, "albino_nightfury_head");*/

        registerDragonHeadBlockModel(blockStateModelGenerator, this.output, "nightfury_head", LLGDragonsBlocks.NIGHTFURY_HEAD, LLGDragonsBlocks.NIGHTFURY_HEAD, LLGDragons.MOD_ID);
        //registerNightfuryHeadBlockModel(blockStateModelGenerator, this.output, LLGDragonsBlocks.NIGHTFURY_HEAD, LLGDragonsBlocks.NIGHTFURY_HEAD, LLGDragons.MOD_ID);
    }

    /*private void registerDragonHead(BlockStateModelGenerator generator, Block block, String name) {
        Identifier parentModel = new Identifier(LLGDragons.MOD_ID, "block/dragon_head/head_nightfury");
        Identifier texture = new Identifier(LLGDragons.MOD_ID, "block/dragon_head/" + name);

        Model model = new Model(Optional.of(parentModel), Optional.empty(), TextureKey.of("0"), TextureKey.PARTICLE);

        TextureMap textures = TextureMap.of(TextureKey.of("0"), texture).put(TextureKey.PARTICLE, texture);

        //Identifier modelId = model.upload(block, TextureMap.of(TextureKey.of("0"), texture).put(TextureKey.PARTICLE, texture), generator.modelCollector);
        Identifier modelId = model.upload(block, textures, generator.modelCollector);

        //generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, modelId)).coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates()));
        generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, modelId));
    }*/

    public static void registerDragonHeadBlockModel(BlockStateModelGenerator blockStateModelGenerator, FabricDataOutput dataOutput, String template_type, Block block, Block templateBlock, String modId) {
        Identifier blockId = Registries.BLOCK.getId(block);
        Identifier templateId = Registries.BLOCK.getId(templateBlock);
        String blockName = blockId.getPath();
        String templateNamespace = templateId.getNamespace();
        String templatePath = templateId.getPath();

        Path outputRoot = dataOutput.getPath();

        String texturePath = templateNamespace + ":block/" + templatePath;
        generateDragonHeadBlockModelJson(outputRoot, blockName, texturePath, modId, template_type);

        Identifier modelId = Identifier.of(modId, "block/dragon_head/" + blockName);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, modelId));

        blockStateModelGenerator.registerParentedItemModel(block, modelId);
    }

    public static void registerDragonHeadBlockModel(BlockStateModelGenerator blockStateModelGenerator, FabricDataOutput dataOutput, String template_type, Block block, Identifier textureIdentifier, String modId) {
        Identifier blockId = Registries.BLOCK.getId(block);
        String blockName = blockId.getPath();

        String texturePath = textureIdentifier.getNamespace() + ":" + textureIdentifier.getPath();

        Path outputRoot = dataOutput.getPath();
        generateDragonHeadBlockModelJson(outputRoot, blockName, texturePath, modId, template_type);

        Identifier modelId = Identifier.of(modId, "block/" + blockName);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, modelId));
        blockStateModelGenerator.registerParentedItemModel(block, modelId);
    }

    public static void generateDragonHeadBlockModelJson(Path outputRoot, String blockName, String texturePath, String modId, String template_type) {
        Path projectRoot = outputRoot.getParent().getParent().getParent();
        Path template = projectRoot.resolve("src/main/resources/assets/" + modId + "/templates/block/" + template_type + ".json");

        Path output = projectRoot.resolve("src/generated/assets/" + modId + "/models/block/dragon_head/" + blockName + ".json");
        try {
            String json = Files.readString(template, StandardCharsets.UTF_8);
            json = json.replace("{texture}", texturePath);
            Files.createDirectories(output.getParent());
            Files.writeString(output, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate path block model for " + blockName, e);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(LLGDragonsItems.FETCH_BALL, Models.GENERATED);
        itemModelGenerator.register(LLGDragonsItems.SCROLL, Models.GENERATED);
        itemModelGenerator.register(LLGDragonsItems.TELLING_BOOK, Models.GENERATED);

        //Dragon Eggs
        itemModelGenerator.register(LLGDragonsItems.NIGHTFURY_SPAWN_EGG, new Model(Optional.of(Identifier.of("minecraft", "item/template_spawn_egg")), Optional.empty()));
        itemModelGenerator.register(LLGDragonsItems.NIGHTFURY_EGG, new Model(Optional.of(Identifier.of("minecraft", "item/template_spawn_egg")), Optional.empty()));
        itemModelGenerator.register(LLGDragonsItems.AZTEC_NIGHTFURY_SPAWN_EGG, new Model(Optional.of(Identifier.of("minecraft", "item/template_spawn_egg")), Optional.empty()));
        itemModelGenerator.register(LLGDragonsItems.AZTEC_NIGHTFURY_EGG, new Model(Optional.of(Identifier.of("minecraft", "item/template_spawn_egg")), Optional.empty()));

        //Gronckle Iron
        itemModelGenerator.register(LLGDragonsItems.GRONCKLE_IRON_INGOT, Models.GENERATED);

        itemModelGenerator.register(LLGDragonsItems.GRONCKLE_IRON_AXE, Models.HANDHELD);
        itemModelGenerator.register(LLGDragonsItems.GRONCKLE_IRON_HOE, Models.HANDHELD);
        itemModelGenerator.register(LLGDragonsItems.GRONCKLE_IRON_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(LLGDragonsItems.GRONCKLE_IRON_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(LLGDragonsItems.GRONCKLE_IRON_SWORD, Models.HANDHELD);

        itemModelGenerator.registerArmor(((ArmorItem) LLGDragonsItems.GRONCKLE_IRON_HELMET));
        itemModelGenerator.registerArmor(((ArmorItem) LLGDragonsItems.GRONCKLE_IRON_CHESTPLATE));
        itemModelGenerator.registerArmor(((ArmorItem) LLGDragonsItems.GRONCKLE_IRON_LEGGINGS));
        itemModelGenerator.registerArmor(((ArmorItem) LLGDragonsItems.GRONCKLE_IRON_BOOTS));
    }
}
