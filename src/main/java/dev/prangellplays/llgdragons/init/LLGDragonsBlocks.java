package dev.prangellplays.llgdragons.init;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.block.dragon_head.NightfuryHeadBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class LLGDragonsBlocks {
    protected static final Map<Block, Identifier> BLOCKS = new LinkedHashMap();
    //DRAGON HEAD
    public static final Block NIGHTFURY_HEAD;
    public static final Block HTTYD_NIGHTFURY_HEAD;
    public static final Block ALBINO_NIGHTFURY_HEAD;
    public static final Block WHITE_NIGHTFURY_HEAD;

    public static void init() {
        BLOCKS.forEach((block, id) -> {
            Registry.register(Registries.BLOCK, id, block);
        });
    }

    protected static <T extends Block> T register(String name, T block) {
        BLOCKS.put(block, LLGDragons.id(name));
        return block;
    }

    protected static <T extends Block> T registerWithItem(String name, T block) {
        return registerWithItem(name, block, new FabricItemSettings());
    }

    protected static <T extends Block> T registerWithItem(String name, T block, FabricItemSettings settings) {
        return registerWithItem(name, block, (b) -> {
            return new BlockItem(b, settings);
        });
    }

    protected static <T extends Block> T registerWithItem(String name, T block, Function<T, BlockItem> itemGenerator) {
        LLGDragonsItems.register(name, (BlockItem)itemGenerator.apply(block));
        return register(name, block);
    }

    public LLGDragonsBlocks() {
    }

    static {
        //Dragon Head
        NIGHTFURY_HEAD = registerWithItem("nightfury_head", new NightfuryHeadBlock(AbstractBlock.Settings.create().mapColor(MapColor.BLACK).strength(0.5F).sounds(BlockSoundGroup.BONE).nonOpaque().luminance(value -> 0).notSolid()));
        HTTYD_NIGHTFURY_HEAD = registerWithItem("httyd_nightfury_head", new NightfuryHeadBlock(AbstractBlock.Settings.create().mapColor(MapColor.BLACK).strength(0.5F).sounds(BlockSoundGroup.BONE).nonOpaque().luminance(value -> 0).notSolid()));
        ALBINO_NIGHTFURY_HEAD = registerWithItem("albino_nightfury_head", new NightfuryHeadBlock(AbstractBlock.Settings.create().mapColor(MapColor.WHITE).strength(0.5F).sounds(BlockSoundGroup.BONE).nonOpaque().luminance(value -> 0).notSolid()));
        WHITE_NIGHTFURY_HEAD = registerWithItem("white_nightfury_head", new NightfuryHeadBlock(AbstractBlock.Settings.create().mapColor(MapColor.WHITE).strength(0.5F).sounds(BlockSoundGroup.BONE).nonOpaque().luminance(value -> 0).notSolid()));
    }
}
