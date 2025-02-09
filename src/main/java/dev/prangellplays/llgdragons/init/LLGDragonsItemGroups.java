package dev.prangellplays.llgdragons.init;

import dev.prangellplays.llgdragons.LLGDragons;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LLGDragonsItemGroups {
    public static final ItemGroup LLGDRAGONS = Registry.register(Registries.ITEM_GROUP,
            new Identifier(LLGDragons.MOD_ID, "llgdragons"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.llgdragons.items")).icon(() -> new ItemStack(LLGDragonsItems.DRAGOSPHERE)).entries((displayContext, entries) -> {
                entries.add(LLGDragonsItems.DRAGOSPHERE);
                entries.add(LLGDragonsItems.TELLING_BOOK);
                entries.add(LLGDragonsItems.SCROLL);
                entries.add(LLGDragonsItems.FETCH_BALL);
                entries.add(LLGDragonsItems.STILLNESS_STAFF);
                entries.add(LLGDragonsItems.GRONCKLE_IRON_INGOT);

                entries.add(LLGDragonsItems.GRONCKLE_IRON_PICKAXE);
                entries.add(LLGDragonsItems.GRONCKLE_IRON_AXE);
                entries.add(LLGDragonsItems.GRONCKLE_IRON_SHOVEL);
                entries.add(LLGDragonsItems.GRONCKLE_IRON_SWORD);
                entries.add(LLGDragonsItems.GRONCKLE_IRON_HOE);

                entries.add(LLGDragonsItems.GRONCKLE_IRON_HELMET);
                entries.add(LLGDragonsItems.GRONCKLE_IRON_CHESTPLATE);
                entries.add(LLGDragonsItems.GRONCKLE_IRON_LEGGINGS);
                entries.add(LLGDragonsItems.GRONCKLE_IRON_BOOTS);

                entries.add(LLGDragonsBlocks.NIGHTFURY_HEAD);
                entries.add(LLGDragonsBlocks.HTTYD_NIGHTFURY_HEAD);
                entries.add(LLGDragonsBlocks.ALBINO_NIGHTFURY_HEAD);
                entries.add(LLGDragonsBlocks.WHITE_NIGHTFURY_HEAD);

                entries.add(LLGDragonsItems.NIGHTFURY_SPAWN_EGG);

                //DEV
                entries.add(LLGDragonsItems.PLASMA_PROJECTILE);
            }).build());

    public static void init() {
    }
}
