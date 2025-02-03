package dev.prangellplays.llgdragons.init;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.LinkedHashMap;
import java.util.Map;

public class LLGDragonsItems {
    protected static final Map<Item, Identifier> ITEMS = new LinkedHashMap();
    public static final Item DRAGOSPHERE;
    public static final Item TELLING_BOOK;
    public static final Item SCROLL;
    public static final Item FETCH_BALL;
    public static final Item STILLNESS_STAFF;
    public static final Item GRONCKLE_IRON_INGOT;
    public static final Item GRONCKLE_IRON_PICKAXE;
    public static final Item GRONCKLE_IRON_AXE;
    public static final Item GRONCKLE_IRON_SHOVEL;
    public static final Item GRONCKLE_IRON_SWORD;
    public static final Item GRONCKLE_IRON_HOE;
    public static final Item GRONCKLE_IRON_HELMET;
    public static final Item GRONCKLE_IRON_CHESTPLATE;
    public static final Item GRONCKLE_IRON_LEGGINGS;
    public static final Item GRONCKLE_IRON_BOOTS;

    public static final Item NIGHTFURY_SPAWN_EGG;

    public static void init() {
        ITEMS.forEach((item, id) -> {
            Registry.register(Registries.ITEM, id, item);
        });
    }

    protected static <T extends Item> T register(String name, T item) {
        ITEMS.put(item, LLGDragons.id(name));
        return item;
    }

    public LLGDragonsItems() {
    }

    static {
        DRAGOSPHERE = register((String) "dragosphere", (Item) (new DragosphereItem(new FabricItemSettings().maxCount(1).fireproof().rarity(Rarity.COMMON))));
        TELLING_BOOK = register((String) "telling_book", (Item) (new TellingBookItem(new FabricItemSettings().maxCount(1))));
        SCROLL = register((String) "scroll", (Item) (new TellingBookItem(new FabricItemSettings())));
        FETCH_BALL = register((String) "fetch_ball", (Item) (new FetchBallItem(new FabricItemSettings().maxCount(1).fireproof())));
        STILLNESS_STAFF = register((String) "stillness_staff", (Item) (new StillnessStaffItem(new FabricItemSettings().maxCount(1).fireproof())));
        GRONCKLE_IRON_INGOT = register((String) "gronckle_iron_ingot", (Item) new Item(new FabricItemSettings().fireproof()));

        GRONCKLE_IRON_PICKAXE = register((String) "gronckle_iron_pickaxe", (Item) new PickaxeItem(LLGDragonsToolMaterials.GRONCKLE_IRON, 2, 2f, new FabricItemSettings().fireproof()));
        GRONCKLE_IRON_AXE = register((String) "gronckle_iron_axe", (Item) new AxeItem(LLGDragonsToolMaterials.GRONCKLE_IRON, 3, 1f, new FabricItemSettings().fireproof()));
        GRONCKLE_IRON_SHOVEL = register((String) "gronckle_iron_shovel", (Item) new ShovelItem(LLGDragonsToolMaterials.GRONCKLE_IRON, 0, 0f, new FabricItemSettings().fireproof()));
        GRONCKLE_IRON_SWORD = register((String) "gronckle_iron_sword", (Item) new SwordItem(LLGDragonsToolMaterials.GRONCKLE_IRON, 5, 3f, new FabricItemSettings().fireproof()));
        GRONCKLE_IRON_HOE = register((String) "gronckle_iron_hoe", (Item) new HoeItem(LLGDragonsToolMaterials.GRONCKLE_IRON, 0, 0f, new FabricItemSettings().fireproof()));

        GRONCKLE_IRON_HELMET = register((String) "gronckle_iron_helmet", (Item) new ArmorItem(LLGDragonsArmourMaterials.GRONCKLE_IRON, ArmorItem.Type.HELMET, new FabricItemSettings().fireproof()));
        GRONCKLE_IRON_CHESTPLATE = register((String) "gronckle_iron_chestplate", (Item) new ArmorItem(LLGDragonsArmourMaterials.GRONCKLE_IRON, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().fireproof()));
        GRONCKLE_IRON_LEGGINGS = register((String) "gronckle_iron_leggings", (Item) new ArmorItem(LLGDragonsArmourMaterials.GRONCKLE_IRON, ArmorItem.Type.LEGGINGS, new FabricItemSettings().fireproof()));
        GRONCKLE_IRON_BOOTS = register((String) "gronckle_iron_boots", (Item) new ArmorItem(LLGDragonsArmourMaterials.GRONCKLE_IRON, ArmorItem.Type.BOOTS, new FabricItemSettings().fireproof()));

        NIGHTFURY_SPAWN_EGG = register((String) "nightfury_spawn_egg", (Item) new SpawnEggItem(LLGDragonsEntities.NIGHTFURY, 1315860, 16701501, new FabricItemSettings()));
    }
}
